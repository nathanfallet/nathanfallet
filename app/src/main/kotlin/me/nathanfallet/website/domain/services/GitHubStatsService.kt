package me.nathanfallet.website.domain.services

/**
 * What we display about a GitHub repository. Everything here is refreshed from
 * the API, so it never goes stale in the source code.
 */
data class RepositoryStats(
    val repo: String,
    val stars: Int,
    val description: String?,
    val language: String?,
    val archived: Boolean,
)

/**
 * Reads live repository statistics.
 */
interface GitHubStatsService {

    /**
     * Returns the stats of every requested repository, indexed by `owner/name`.
     * Repositories that could not be read are simply absent from the result:
     * the website has to render fine without GitHub.
     */
    suspend fun stats(repos: Collection<String>): Map<String, RepositoryStats>
}
