package me.nathanfallet.website.domain.services

/**
 * Resolves the latest published version of a library, so the install snippets
 * on the website never tell anyone to depend on something outdated.
 */
interface PackageVersionService {

    /**
     * Latest version on Maven Central, or null when it cannot be read.
     */
    suspend fun mavenVersion(path: String): String?

    /**
     * Latest git tag of a repository, used for Swift packages.
     */
    suspend fun latestTag(repo: String): String?
}
