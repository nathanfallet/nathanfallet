package me.nathanfallet.website.presentation.mappers

import me.nathanfallet.website.data.Profile
import me.nathanfallet.website.domain.models.Archive
import me.nathanfallet.website.domain.models.Contribution
import me.nathanfallet.website.domain.models.Entry
import me.nathanfallet.website.domain.models.Library
import me.nathanfallet.website.domain.models.Link
import me.nathanfallet.website.domain.models.Portfolio
import me.nathanfallet.website.domain.models.Powered
import me.nathanfallet.website.domain.models.Product
import me.nathanfallet.website.domain.models.Status
import me.nathanfallet.website.domain.models.Video
import me.nathanfallet.website.domain.services.PullRequest
import me.nathanfallet.website.domain.services.RepositoryStats
import me.nathanfallet.website.presentation.views.ArchiveView
import me.nathanfallet.website.presentation.views.ContributionView
import me.nathanfallet.website.presentation.views.EntryPageView
import me.nathanfallet.website.presentation.views.EntryRefView
import me.nathanfallet.website.presentation.views.LayoutView
import me.nathanfallet.website.presentation.views.LibraryView
import me.nathanfallet.website.presentation.views.LinkView
import me.nathanfallet.website.presentation.views.MetaView
import me.nathanfallet.website.presentation.views.ProductView
import me.nathanfallet.website.presentation.views.PullRequestView
import me.nathanfallet.website.presentation.views.RelatedGroupView
import me.nathanfallet.website.presentation.views.VideoPageView
import me.nathanfallet.website.presentation.views.VideoView

/**
 * The canonical path of an entry. `/project/{id}` still resolves, and redirects here.
 */
fun Entry.path(): String = "/projects/$id"

fun Link.toLinkView() = LinkView(kind.name, label, url)

fun Entry.toRefView() = EntryRefView(name, path(), tagline)

fun Product.toProductView(portfolio: Portfolio) = ProductView(
    name = name,
    tagline = tagline,
    url = path(),
    by = by,
    sunset = status == Status.SUNSET,
    poweredBy = portfolio.librariesOf(this).map { it.toRefView() },
)

fun Library.toLibraryView(portfolio: Portfolio, stats: Map<String, RepositoryStats>) = LibraryView(
    name = name,
    tagline = tagline,
    url = path(),
    repo = repo,
    stars = stats[repo]?.stars ?: stars,
    language = stats[repo]?.language,
    targets = targets,
    sunset = status == Status.SUNSET || stats[repo]?.archived == true,
    powers = portfolio.poweredBy(this).map { it.toRefView() },
)

fun Contribution.toContributionView(portfolio: Portfolio, stats: Map<String, RepositoryStats>) = ContributionView(
    name = name,
    // The description on GitHub is always fresher than anything hardcoded here.
    tagline = stats[repo]?.description ?: tagline,
    repo = repo,
    url = path(),
    stars = stats[repo]?.stars ?: stars,
    maintainer = maintainer,
    powers = portfolio.poweredBy(this).map { it.toRefView() },
)

fun Video.toVideoView(portfolio: Portfolio) = VideoView(
    title = title,
    channel = channel,
    url = path,
    thumbnail = "$path/thumbnail.jpg",
    watchUrl = watchUrl,
    publishedAt = publishedAt,
    about = about.mapNotNull(portfolio::entry).map { it.toRefView() },
)

fun Video.toVideoPageView(portfolio: Portfolio) = VideoPageView(
    layout = LayoutView(
        title = title,
        description = "A video from my YouTube channel, published on $publishedAt.",
        canonical = Profile.BASE_URL + path,
        snippets = listOf(breadcrumbSnippet(title, Profile.BASE_URL + path)),
    ),
    title = title,
    channel = channel,
    description = description,
    thumbnail = "$path/thumbnail.jpg",
    watchUrl = watchUrl,
    publishedAt = publishedAt,
    about = about.mapNotNull(portfolio::entry).map { it.toRefView() },
)

fun Archive.toArchiveView() = ArchiveView(
    name = name,
    tagline = tagline,
    url = path(),
    year = year,
)

/**
 * Builds the page of any entry, whatever its kind: the differences are only in
 * the metadata and the related entries.
 */
fun Entry.toEntryPageView(
    portfolio: Portfolio,
    stats: Map<String, RepositoryStats>,
    pullRequests: List<PullRequest> = emptyList(),
): EntryPageView {
    val videos = portfolio.videosAbout(this).map { it.toVideoView(portfolio) }
    val meta = mutableListOf<MetaView>()
    val related = mutableListOf<RelatedGroupView>()
    val kind: String

    when (this) {
        is Product -> {
            kind = "Product"
            by?.let { meta += MetaView("By", it) }
        }

        is Library -> {
            kind = "Open source"
            (stats[repo]?.stars ?: stars).takeIf { it > 0 }?.let {
                meta += MetaView("Stars", it.toString())
            }
            stats[repo]?.language?.let { meta += MetaView("Language", it) }
            targets.takeIf { it.isNotEmpty() }?.let { meta += MetaView("Targets", it.joinToString(", ")) }
            portfolio.poweredBy(this).takeIf { it.isNotEmpty() }?.let {
                related += RelatedGroupView("Powers", it.map(Entry::toRefView))
            }
        }

        is Contribution -> {
            kind = if (maintainer) "Maintainer" else "Contributor"
            portfolio.poweredBy(this).takeIf { it.isNotEmpty() }?.let {
                related += RelatedGroupView("Powers", it.map(Entry::toRefView))
            }
            stats[repo]?.let { repository ->
                meta += MetaView("Stars", repository.stars.toString())
                repository.language?.let { meta += MetaView("Language", it) }
            } ?: stars.takeIf { it > 0 }?.let { meta += MetaView("Stars", it.toString()) }
            meta += MetaView("Repository", repo)
        }

        is Archive -> {
            kind = "Archive"
            year?.let { meta += MetaView("Last updated", it) }
        }
    }

    // Products and archives alike run on things, mine and other people's.
    if (this is Powered) {
        portfolio.librariesOf(this).takeIf { it.isNotEmpty() }?.let {
            related += RelatedGroupView("Powered by my open source", it.map(Entry::toRefView))
        }
        portfolio.contributionsOf(this).takeIf { it.isNotEmpty() }?.let {
            related += RelatedGroupView("Built on projects I work on", it.map(Entry::toRefView))
        }
    }

    return EntryPageView(
        layout = LayoutView(
            title = name,
            description = tagline,
            canonical = Profile.BASE_URL + path(),
            snippets = listOf(breadcrumbSnippet(name, Profile.BASE_URL + path())),
        ),
        kind = kind,
        name = name,
        tagline = (this as? Contribution)?.let { stats[it.repo]?.description } ?: tagline,
        description = description,
        sunset = status == Status.SUNSET,
        links = links.map(Link::toLinkView),
        meta = meta,
        related = related,
        pullRequests = pullRequests.map {
            PullRequestView(it.number, it.title, it.url, it.mergedAt)
        },
        videos = videos,
    )
}

fun breadcrumbSnippet(name: String, url: String) = """
    {
      "@context": "https://schema.org",
      "@type": "BreadcrumbList",
      "itemListElement": [
        {"@type": "ListItem", "position": 1, "name": "Home", "item": "${Profile.BASE_URL}/"},
        {"@type": "ListItem", "position": 2, "name": "${name.escapeJson()}", "item": "$url"}
      ]
    }
""".trimIndent()

fun personSnippet() = """
    {
      "@context": "https://schema.org",
      "@type": "Person",
      "name": "${Profile.NAME}",
      "description": "${Profile.ROLE}",
      "url": "${Profile.BASE_URL}/",
      "email": "${Profile.EMAIL}",
      "image": "${Profile.BASE_URL}/img/profile.jpg",
      "sameAs": [${Profile.socials.joinToString(", ") { "\"${it.second}\"" }}]
    }
""".trimIndent()

private fun String.escapeJson() = replace("\\", "\\\\").replace("\"", "\\\"")
