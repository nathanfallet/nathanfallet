package me.nathanfallet.website.infrastructure.youtube

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.nathanfallet.website.domain.services.ThumbnailService
import org.slf4j.LoggerFactory
import java.awt.Color
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/**
 * Downloads illustrations once, shrinks them to what the page actually displays,
 * and keeps the result in memory. There are a couple of dozen of them and each
 * one ends up weighing tens of kilobytes, so the whole set costs about a
 * megabyte and is never fetched again.
 *
 * Everything is re-encoded to JPEG: the sources are a mix of JPEG and PNG, and
 * serving a PNG as `image/jpeg` would be refused by the browser, since the site
 * sends `X-Content-Type-Options: nosniff`.
 */
class ThumbnailServiceImpl(
    private val client: HttpClient,
    private val maxWidth: Int = 960,
) : ThumbnailService {

    private val logger = LoggerFactory.getLogger(ThumbnailServiceImpl::class.java)
    private val mutex = Mutex()
    private val cache = mutableMapOf<String, ByteArray>()

    override suspend fun thumbnail(youtubeId: String): ByteArray? {
        mutex.withLock { cache[youtubeId]?.let { return it } }

        // `maxresdefault` is missing on older uploads, `hqdefault` always exists.
        val bytes = fetch("https://i.ytimg.com/vi/$youtubeId/maxresdefault.jpg")
            ?: fetch("https://i.ytimg.com/vi/$youtubeId/hqdefault.jpg")
            ?: return null

        mutex.withLock { cache[youtubeId] = bytes }
        return bytes
    }

    override suspend fun image(url: String): ByteArray? {
        mutex.withLock { cache[url]?.let { return it } }
        val bytes = fetch(url) ?: return null
        mutex.withLock { cache[url] = bytes }
        return bytes
    }

    private suspend fun fetch(url: String): ByteArray? = runCatching {
        val response = client.get(url)
        if (!response.status.isSuccess()) return null
        response.readRawBytes().takeIf { it.isNotEmpty() }?.let(::shrink)
    }.onFailure {
        logger.warn("Could not fetch thumbnail {}: {}", url, it.message)
    }.getOrNull()

    /**
     * Scales down to [maxWidth] and re-encodes as JPEG. Returns null when the
     * bytes are not a readable image, so nothing unexpected is ever served.
     */
    private fun shrink(bytes: ByteArray): ByteArray? {
        val source = runCatching { ImageIO.read(bytes.inputStream()) }.getOrNull() ?: return null
        val width = minOf(source.width, maxWidth)
        val height = (width.toDouble() / source.width * source.height).toInt().coerceAtLeast(1)

        val target = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        target.createGraphics().apply {
            setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
            setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
            // Transparent sources would otherwise turn black once flattened.
            color = Color.WHITE
            fillRect(0, 0, width, height)
            drawImage(source.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH), 0, 0, null)
            dispose()
        }

        return ByteArrayOutputStream().use { out ->
            if (!ImageIO.write(target, "jpg", out)) return null
            out.toByteArray()
        }
    }
}
