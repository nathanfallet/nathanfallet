package me.nathanfallet.website.domain.services

/**
 * Something of mine that landed in somebody else's repository: either a pull
 * request I authored, or a commit of mine that reached the default branch some
 * other way.
 */
data class Landed(
    /**
     * `#1077` for a pull request, a short sha for a commit.
     */
    val reference: String,
    val title: String,
    val url: String,
    /**
     * ISO date, or null when GitHub does not report one.
     */
    val date: String?,
    /**
     * The pull request that carried this commit, when it was not mine. Null for
     * a pull request I authored myself.
     */
    val via: Reference? = null,
) {
    data class Reference(val label: String, val url: String)
}

/**
 * Lists what I actually shipped to someone else's repository.
 */
interface GitHubContributionsService {

    /**
     * Returns what landed in [repo], most recent first.
     *
     * Merged pull requests come first. When there are none — a maintainer may
     * have cherry-picked the change into their own release branch, which leaves
     * no merged pull request of mine — the commits I authored are used instead.
     *
     * An empty list means either nothing, or that GitHub could not be reached:
     * the page has to render fine in both cases.
     */
    suspend fun landed(repo: String): List<Landed>
}
