package me.nathanfallet.website.infrastructure.packages

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable
import io.ktor.client.call.*
import me.nathanfallet.website.domain.services.PackageVersionService
import org.slf4j.LoggerFactory
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant

@Serializable
private data class GitHubTag(val name: String)

private data class Cached(val value: String?, val at: Instant)

/**
 * Reads versions from the authoritative sources: the `maven-metadata.xml` served
 * by Maven Central itself, and the git tags of a repository for Swift packages.
 *
 * The search API of Maven Central is deliberately avoided: its index lags behind,
 * and several of these libraries are simply missing from it.
 */
class PackageVersionServiceImpl(
    private val client: HttpClient,
    private val token: String? = System.getenv("GITHUB_TOKEN"),
    private val ttl: Duration = 24.hours,
) : PackageVersionService {

    private val logger = LoggerFactory.getLogger(PackageVersionServiceImpl::class.java)
    private val mutex = Mutex()
    private val cache = mutableMapOf<String, Cached>()

    override suspend fun mavenVersion(path: String): String? = cached("maven:$path") {
        val response = client.get("https://repo1.maven.org/maven2/$path/maven-metadata.xml")
        if (!response.status.isSuccess()) return@cached null
        val xml = response.bodyAsText()
        // `release` is the latest stable one, `latest` may point at a snapshot.
        Regex("<release>(.*?)</release>").find(xml)?.groupValues?.get(1)
            ?: Regex("<latest>(.*?)</latest>").find(xml)?.groupValues?.get(1)
    }

    override suspend fun latestTag(repo: String): String? = cached("tag:$repo") {
        val response = client.get("https://api.github.com/repos/$repo/tags") {
            header(HttpHeaders.Accept, "application/vnd.github+json")
            token?.takeIf(String::isNotBlank)?.let { bearerAuth(it) }
            parameter("per_page", 1)
        }
        if (!response.status.isSuccess()) return@cached null
        response.body<List<GitHubTag>>().firstOrNull()?.name?.removePrefix("v")
    }

    private suspend fun cached(key: String, block: suspend () -> String?): String? {
        val now = Clock.System.now()
        mutex.withLock {
            cache[key]?.let { if (now - it.at < ttl) return it.value }
            val value = runCatching { block() }
                .onFailure { logger.warn("Could not resolve {}: {}", key, it.message) }
                .getOrNull()
            // A failure is cached too, so an unreachable registry is not retried
            // on every page view. It simply hides the version for a while.
            cache[key] = Cached(value, now)
            return value
        }
    }
}
