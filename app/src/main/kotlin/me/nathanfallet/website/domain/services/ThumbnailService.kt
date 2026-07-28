package me.nathanfallet.website.domain.services

/**
 * Serves YouTube thumbnails through this website rather than letting the
 * browser hit Google directly: the page keeps making no third party request,
 * and visitors are not exposed to YouTube just for looking at a grid.
 */
interface ThumbnailService {

    /**
     * Returns the JPEG bytes of a video thumbnail, or null when it could not be
     * fetched.
     */
    suspend fun thumbnail(youtubeId: String): ByteArray?

    /**
     * Same, for an arbitrary image URL: an article illustration, for instance.
     */
    suspend fun image(url: String): ByteArray?
}
