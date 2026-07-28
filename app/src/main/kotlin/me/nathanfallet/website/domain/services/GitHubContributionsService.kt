package me.nathanfallet.website.domain.services

/**
 * A pull request I authored and that got merged upstream.
 */
data class PullRequest(
    val number: Int,
    val title: String,
    val url: String,
    /**
     * ISO date of the merge, or null when GitHub does not report it.
     */
    val mergedAt: String?,
)

/**
 * Lists what I actually shipped to someone else's repository.
 */
interface GitHubContributionsService {

    /**
     * Returns my merged pull requests on [repo], most recent first. An empty
     * list means either none, or that GitHub could not be reached: the page has
     * to render fine in both cases.
     */
    suspend fun pullRequests(repo: String): List<PullRequest>
}
