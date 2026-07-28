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
import me.nathanfallet.website.domain.services.Landed
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

@Serializable
private data class CommitEntry(
    val sha: String,
    @SerialName("html_url") val htmlUrl: String,
    val commit: CommitDetails,
)

@Serializable
private data class CommitDetails(val message: String, val author: CommitAuthor? = null)

@Serializable
private data class CommitAuthor(val date: String? = null)

private data class CacheEntry(val landed: List<Landed>, val at: Instant)

/**
 * Reads contributions through the GitHub API.
 *
 * The search endpoint has a much tighter rate limit than the rest (30 requests
 * per minute authenticated, 10 without a token), so repositories are queried one
 * at a time, when their page is actually viewed, and the result is kept for
 * [ttl].
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

    override suspend fun landed(repo: String): List<Landed> {
        val now = Clock.System.now()
        mutex.withLock {
            cache[repo]?.let { if (now - it.at < ttl) return it.landed }
            failures[repo]?.let { if (now - it < retryDelay) return cache[repo]?.landed.orEmpty() }

            val fetched = fetchPullRequests(repo)?.takeIf { it.isNotEmpty() }
                ?: fetchCommits(repo)

            if (fetched == null) {
                failures[repo] = now
                return cache[repo]?.landed.orEmpty()
            }
            failures.remove(repo)
            cache[repo] = CacheEntry(fetched, now)
            return fetched
        }
    }

    private suspend fun fetchPullRequests(repo: String): List<Landed>? = runCatching {
        val response = client.get("https://api.github.com/search/issues") {
            gitHub()
            parameter("q", "repo:$repo author:$author type:pr is:merged")
            parameter("sort", "created")
            parameter("order", "desc")
            parameter("per_page", limit)
        }
        if (!response.status.isSuccess()) error("${response.status}")
        response.body<SearchResults>().items.map {
            Landed(
                reference = "#${it.number}",
                title = it.title,
                url = it.htmlUrl,
                date = it.pullRequest?.mergedAt?.take(10),
            )
        }
    }.onFailure {
        logger.warn("Could not read merged pull requests for {}: {}", repo, it.message)
    }.getOrNull()

    /**
     * Used when no pull request of mine was merged, typically because the change
     * was cherry-picked by a maintainer. The commit itself is what tells the
     * story, far better than the release pull request that happened to carry it.
     */
    private suspend fun fetchCommits(repo: String): List<Landed>? = runCatching {
        val response = client.get("https://api.github.com/repos/$repo/commits") {
            gitHub()
            parameter("author", author)
            parameter("per_page", limit)
        }
        if (!response.status.isSuccess()) error("${response.status}")
        response.body<List<CommitEntry>>().map {
            Landed(
                reference = it.sha.take(7),
                title = it.commit.message.lineSequence().first(),
                url = it.htmlUrl,
                date = it.commit.author?.date?.take(10),
            )
        }
    }.onFailure {
        logger.warn("Could not read commits for {}: {}", repo, it.message)
    }.getOrNull()

    private fun HttpRequestBuilder.gitHub() {
        header(HttpHeaders.Accept, "application/vnd.github+json")
        token?.takeIf(String::isNotBlank)?.let { bearerAuth(it) }
    }
}
