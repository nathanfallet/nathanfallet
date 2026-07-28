package me.nathanfallet.website.presentation.views

/**
 * Everything below is what the templates see. Freemarker is happier with plain
 * types than with domain objects, and it keeps the templates free of any logic.
 */

data class LinkView(
    val kind: String,
    val label: String,
    val url: String,
)

data class EntryRefView(
    val name: String,
    val url: String,
    val tagline: String,
)

data class ProductView(
    val name: String,
    val tagline: String,
    val url: String,
    val by: String?,
    val sunset: Boolean,
    val poweredBy: List<EntryRefView>,
)

data class LibraryView(
    val name: String,
    val tagline: String,
    val url: String,
    val repo: String,
    val stars: Int,
    val language: String?,
    val targets: List<String>,
    val sunset: Boolean,
    val powers: List<EntryRefView>,
)

data class ContributionView(
    val name: String,
    val tagline: String,
    val repo: String,
    /**
     * Its page on this website, which lists the merged pull requests.
     */
    val url: String,
    val stars: Int,
    val maintainer: Boolean,
)

data class ArchiveView(
    val name: String,
    val tagline: String,
    val url: String,
    val year: String?,
)

data class MetaView(
    val label: String,
    val value: String,
)

data class PullRequestView(
    val number: Int,
    val title: String,
    val url: String,
    val mergedAt: String?,
)

data class RelatedGroupView(
    val title: String,
    val entries: List<EntryRefView>,
)

/**
 * The bits every page needs: head tags, canonical URL and structured data.
 */
data class LayoutView(
    val title: String,
    val description: String,
    val canonical: String,
    val snippets: List<String>,
)

data class HomeView(
    val layout: LayoutView,
    val name: String,
    val role: String,
    val intro: String,
    val email: String,
    val location: String,
    val socials: List<LinkView>,
    val products: List<ProductView>,
    val libraries: List<LibraryView>,
    val contributions: List<ContributionView>,
    val archives: List<ArchiveView>,
    val totalStars: Int,
)

data class EntryPageView(
    val layout: LayoutView,
    val kind: String,
    val name: String,
    val tagline: String,
    val description: String?,
    val sunset: Boolean,
    val links: List<LinkView>,
    val meta: List<MetaView>,
    val related: List<RelatedGroupView>,
    val pullRequests: List<PullRequestView>,
)

data class ArchivesPageView(
    val layout: LayoutView,
    val archives: List<ArchiveView>,
)

data class ErrorPageView(
    val layout: LayoutView,
    val status: String,
    val message: String,
)
