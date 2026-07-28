package me.nathanfallet.website.infrastructure.github

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import me.nathanfallet.website.domain.services.GitHubContributionsService
import me.nathanfallet.website.domain.services.PullRequest
import org.slf4j.LoggerFactory
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

@Serializable
private data class SearchResults(val items: List<SearchItem> = emptyList())

@Serializable
private data class SearchItem(
    val number: Int,
    val title: String,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("pull_request") val pullRequest: PullRequestRef? = null,
)

@Serializable
private data class PullRequestRef(@SerialName("merged_at") val mergedAt: String? = null)

private data class CacheEntry(val pullRequests: List<PullRequest>, val at: Instant)

/**
 * Lists merged pull requests through the GitHub search API.
 *
 * The search endpoint has a much tighter rate limit than the rest of the API
 * (30 requests per minute authenticated, 10 without a token), so repositories
 * are queried one at a time, when their page is actually viewed, and the result
 * is kept for [ttl].
 */
class GitHubContributionsServiceImpl(
    private val client: HttpClient,
    private val author: String,
    private val token: String? = System.getenv("GITHUB_TOKEN"),
    private val ttl: Duration = 24.hours,
    private val retryDelay: Duration = 30.minutes,
    private val limit: Int = 20,
) : GitHubContributionsService {

    private val logger = LoggerFactory.getLogger(GitHubContributionsServiceImpl::class.java)
    private val mutex = Mutex()
    private val cache = mutableMapOf<String, CacheEntry>()
    private val failures = mutableMapOf<String, Instant>()

    override suspend fun pullRequests(repo: String): List<PullRequest> {
        val now = Clock.System.now()
        mutex.withLock {
            cache[repo]?.let { if (now - it.at < ttl) return it.pullRequests }
            failures[repo]?.let { if (now - it < retryDelay) return cache[repo]?.pullRequests.orEmpty() }

            val fetched = fetch(repo)
            if (fetched == null) {
                failures[repo] = now
                return cache[repo]?.pullRequests.orEmpty()
            }
            failures.remove(repo)
            cache[repo] = CacheEntry(fetched, now)
            return fetched
        }
    }

    private suspend fun fetch(repo: String): List<PullRequest>? = runCatching {
        val response = client.get("https://api.github.com/search/issues") {
            header(HttpHeaders.Accept, "application/vnd.github+json")
            token?.takeIf(String::isNotBlank)?.let { bearerAuth(it) }
            parameter("q", "repo:$repo author:$author type:pr is:merged")
            parameter("sort", "created")
            parameter("order", "desc")
            parameter("per_page", limit)
        }
        if (!response.status.isSuccess()) error("${response.status} for $repo")
        response.body<SearchResults>().items.map {
            PullRequest(
                number = it.number,
                title = it.title,
                url = it.htmlUrl,
                mergedAt = it.pullRequest?.mergedAt?.take(10),
            )
        }
    }.onFailure {
        logger.warn("Could not read merged pull requests for {}: {}", repo, it.message)
    }.getOrNull()
}
