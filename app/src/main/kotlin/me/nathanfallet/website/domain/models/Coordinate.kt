package me.nathanfallet.website.domain.models

/**
 * Where a library can be installed from. A library can be published in several
 * places at once, so an entry may carry more than one of these.
 */
sealed interface Coordinate {

    /**
     * Published on Maven Central.
     */
    data class Maven(val group: String, val artifact: String) : Coordinate {
        val path: String get() = group.replace('.', '/') + "/" + artifact
    }

    /**
     * A Gradle plugin, resolved through the plugin portal.
     */
    data class GradlePlugin(val id: String) : Coordinate

    /**
     * A Swift package, added by URL. [indexed] says whether it is listed on the
     * Swift Package Index: not all of them are, and linking to a "package not
     * found" page would be worse than not linking at all.
     */
    data class SwiftPackage(val url: String, val indexed: Boolean) : Coordinate

    /**
     * A GitHub Action, referenced by repository and tag.
     */
    data class GitHubAction(val repo: String, val version: String) : Coordinate

    /**
     * Published on npm.
     */
    data class NpmPackage(val name: String) : Coordinate
}
