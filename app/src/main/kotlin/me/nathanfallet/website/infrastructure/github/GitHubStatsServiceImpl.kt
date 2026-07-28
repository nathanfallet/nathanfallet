package me.nathanfallet.website.infrastructure.github

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.nathanfallet.website.domain.services.GitHubStatsService
import me.nathanfallet.website.domain.services.RepositoryStats
import org.slf4j.LoggerFactory
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

@Serializable
private data class GitHubRepository(
    @SerialName("full_name") val fullName: String,
    @SerialName("stargazers_count") val stargazersCount: Int,
    val description: String? = null,
    val language: String? = null,
    val archived: Boolean = false,
)

/**
 * Reads repository stats from the GitHub REST API, and keeps them in memory for
 * [ttl]. There is no database here: a cold start just means the first visitor
 * triggers the fetch, and any failure falls back to the previous values.
 */
class GitHubStatsServiceImpl(
    private val client: HttpClient,
    private val token: String? = System.getenv("GITHUB_TOKEN"),
    private val ttl: Duration = 24.hours,
    private val retryDelay: Duration = 30.minutes,
) : GitHubStatsService {

    private val logger = LoggerFactory.getLogger(GitHubStatsServiceImpl::class.java)
    private val mutex = Mutex()
    private val cache = mutableMapOf<String, RepositoryStats>()
    private var refreshedAt: Instant? = null
    private var attemptedAt: Instant? = null

    override suspend fun stats(repos: Collection<String>): Map<String, RepositoryStats> {
        val now = Clock.System.now()
        mutex.withLock {
            if (!shouldRefresh(now, repos)) return cache.toMap()

            attemptedAt = now
            val fetched = fetch(repos)
            // Keep previously known values for the repositories we failed to read,
            // so a GitHub outage degrades into slightly stale numbers, not zeros.
            cache.putAll(fetched)
            if (fetched.isNotEmpty()) refreshedAt = now
            return cache.toMap()
        }
    }

    private fun shouldRefresh(now: Instant, repos: Collection<String>): Boolean {
        val refreshed = refreshedAt
        if (refreshed != null && now - refreshed < ttl && cache.keys.containsAll(repos)) return false

        // Without this, a rate limited or unreachable API would be hammered again
        // on every single page view, since nothing ever lands in the cache.
        val attempted = attemptedAt
        return attempted == null || now - attempted >= retryDelay
    }

    private suspend fun fetch(repos: Collection<String>): Map<String, RepositoryStats> = coroutineScope {
        repos.distinct().map { repo ->
            async {
                runCatching {
                    val response = client.get("https://api.github.com/repos/$repo") {
                        header(HttpHeaders.Accept, "application/vnd.github+json")
                        token?.takeIf(String::isNotBlank)?.let { bearerAuth(it) }
                    }
                    if (!response.status.isSuccess()) error("${response.status} for $repo")
                    val body = response.body<GitHubRepository>()
                    RepositoryStats(
                        repo = repo,
                        stars = body.stargazersCount,
                        description = body.description,
                        language = body.language,
                        archived = body.archived,
                    )
                }.onFailure {
                    logger.warn("Could not read GitHub stats for {}: {}", repo, it.message)
                }.getOrNull()
            }
        }.mapNotNull { it.await() }.associateBy(RepositoryStats::repo)
    }
}
