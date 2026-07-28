package me.nathanfallet.website.infrastructure.youtube

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import me.nathanfallet.website.domain.services.ThumbnailService
import org.slf4j.LoggerFactory

/**
 * Downloads thumbnails once and keeps them in memory. There are a dozen videos
 * and each thumbnail weighs a few tens of kilobytes, so the whole set costs
 * less than a megabyte and never needs to be fetched again.
 */
class ThumbnailServiceImpl(
    private val client: HttpClient,
) : ThumbnailService {

    private val logger = LoggerFactory.getLogger(ThumbnailServiceImpl::class.java)
    private val mutex = Mutex()
    private val cache = mutableMapOf<String, ByteArray>()

    override suspend fun thumbnail(youtubeId: String): ByteArray? {
        mutex.withLock {
            cache[youtubeId]?.let { return it }
        }

        // `maxresdefault` is missing on older uploads, `hqdefault` always exists.
        val bytes = fetch("https://i.ytimg.com/vi/$youtubeId/maxresdefault.jpg")
            ?: fetch("https://i.ytimg.com/vi/$youtubeId/hqdefault.jpg")
            ?: return null

        mutex.withLock { cache[youtubeId] = bytes }
        return bytes
    }

    private suspend fun fetch(url: String): ByteArray? = runCatching {
        val response = client.get(url)
        if (!response.status.isSuccess()) return null
        response.readRawBytes().takeIf { it.isNotEmpty() }
    }.onFailure {
        logger.warn("Could not fetch thumbnail {}: {}", url, it.message)
    }.getOrNull()
}
