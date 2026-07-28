package me.nathanfallet.website.presentation.routes

import io.ktor.http.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nathanfallet.website.data.Profile
import me.nathanfallet.website.domain.models.Contribution
import me.nathanfallet.website.domain.models.Coordinate
import me.nathanfallet.website.domain.models.Library
import me.nathanfallet.website.domain.models.Portfolio
import me.nathanfallet.website.domain.services.GitHubContributionsService
import me.nathanfallet.website.domain.services.GitHubStatsService
import me.nathanfallet.website.domain.services.PackageVersionService
import me.nathanfallet.website.domain.services.ThumbnailService
import me.nathanfallet.website.presentation.mappers.*
import me.nathanfallet.website.presentation.views.*

data class WebsiteRoutesDependencies(
    val portfolio: Portfolio,
    val gitHubStatsService: GitHubStatsService,
    val gitHubContributionsService: GitHubContributionsService,
    val thumbnailService: ThumbnailService,
    val packageVersionService: PackageVersionService,
)

/**
 * Every repository we display a star count for.
 */
private fun Portfolio.repositories(): List<String> =
    libraries.map(Library::repo) + contributions.map(Contribution::repo)

fun Route.websiteRoutes(dependencies: WebsiteRoutesDependencies) = with(dependencies) {

    get("/") {
        val stats = gitHubStatsService.stats(portfolio.repositories())
        val libraries = portfolio.libraries
            .map { it.toLibraryView(portfolio, stats) }
            .sortedWith(compareBy({ it.sunset }, { -it.stars }))
        val contributions = portfolio.contributions
            .map { it.toContributionView(portfolio, stats) }
            .sortedByDescending { it.stars }

        // Co-maintaining someone else's library counts as maintaining it, so its
        // stars belong in the total. It is then not counted again as a mere
        // contribution.
        val maintained = contributions.filter { it.maintainer }

        call.respond(
            FreeMarkerContent(
                "home.ftl",
                mapOf(
                    "view" to HomeView(
                        layout = LayoutView(
                            title = "${Profile.NAME} — ${Profile.ROLE}",
                            description = "${Profile.NAME}. ${Profile.ROLE}. ${Profile.EMAIL}",
                            canonical = "${Profile.BASE_URL}/",
                            snippets = listOf(personSnippet()),
                        ),
                        name = Profile.NAME,
                        role = Profile.ROLE,
                        intro = Profile.intro,
                        email = Profile.EMAIL,
                        location = Profile.LOCATION,
                        socials = Profile.socials.map { LinkView("WEBSITE", it.first, it.second) },
                        products = portfolio.products.map { it.toProductView(portfolio) },
                        libraries = libraries,
                        contributions = contributions,
                        archives = portfolio.archives.map { it.toArchiveView() },
                        videos = portfolio.ownVideos.map { it.toVideoView(portfolio) },
                        appearances = portfolio.appearances.map { it.toVideoView(portfolio) },
                        articles = portfolio.articles.map { it.toArticleView() },
                        openSourceProjects = libraries.size + contributions.size,
                        totalStars = libraries.sumOf { it.stars } + maintained.sumOf { it.stars },
                        videoCount = portfolio.videos.size,
                    )
                )
            )
        )
    }

    get("/archives") {
        call.respond(
            FreeMarkerContent(
                "archives.ftl",
                mapOf(
                    "view" to ArchivesPageView(
                        layout = LayoutView(
                            title = "Archives",
                            description = "Projects I am not maintaining anymore, kept for the record.",
                            canonical = "${Profile.BASE_URL}/archives",
                            snippets = listOf(breadcrumbSnippet("Archives", "${Profile.BASE_URL}/archives")),
                        ),
                        archives = portfolio.archives.map { it.toArchiveView() },
                    )
                )
            )
        )
    }

    get("/videos") {
        call.respond(
            FreeMarkerContent(
                "videos.ftl",
                mapOf(
                    "view" to VideosPageView(
                        layout = LayoutView(
                            title = "Videos",
                            description = "Videos from my YouTube channel, about the projects I build.",
                            canonical = "${Profile.BASE_URL}/videos",
                            snippets = listOf(breadcrumbSnippet("Videos", "${Profile.BASE_URL}/videos")),
                        ),
                        videos = portfolio.ownVideos.map { it.toVideoView(portfolio) },
                        appearances = portfolio.appearances.map { it.toVideoView(portfolio) },
                        articles = portfolio.articles.map { it.toArticleView() },
                    )
                )
            )
        )
    }

    get("/videos/{id}") {
        val video = portfolio.video(call.parameters["id"]!!)
            ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(FreeMarkerContent("video.ftl", mapOf("view" to video.toVideoPageView(portfolio))))
    }

    get("/videos/{id}/thumbnail.jpg") {
        val video = portfolio.video(call.parameters["id"]!!)
            ?: return@get call.respond(HttpStatusCode.NotFound)
        val bytes = thumbnailService.thumbnail(video.youtubeId)
            ?: return@get call.respond(HttpStatusCode.NotFound)
        call.response.cacheControl(CacheControl.MaxAge(maxAgeSeconds = 60 * 60 * 24 * 7))
        call.respondBytes(bytes, ContentType.Image.JPEG)
    }

    get("/articles/{id}/thumbnail.jpg") {
        val image = portfolio.article(call.parameters["id"]!!)?.image
            ?: return@get call.respond(HttpStatusCode.NotFound)
        val bytes = thumbnailService.image(image)
            ?: return@get call.respond(HttpStatusCode.NotFound)
        call.response.cacheControl(CacheControl.MaxAge(maxAgeSeconds = 60 * 60 * 24 * 7))
        call.respondBytes(bytes, ContentType.Image.JPEG)
    }

    get("/projects/{id}") {
        val id = call.parameters["id"]!!
        val entry = portfolio.entry(id)

        if (entry == null) {
            // The identifier may be a former one: keep old links alive.
            portfolio.entryByAlias(id)?.let {
                return@get call.respondRedirect(it.path(), permanent = true)
            }
            return@get call.respond(HttpStatusCode.NotFound)
        }

        val stats = gitHubStatsService.stats(portfolio.repositories())
        // Only a contribution page needs the pull requests, and the search API is
        // rate limited far more aggressively than the rest.
        val pullRequests = (entry as? Contribution)
            ?.let { gitHubContributionsService.pullRequests(it.repo) }
            .orEmpty()

        // Only a library declares where it can be installed from.
        val install = (entry as? Library)?.let { library ->
            library.coordinates.map { coordinate ->
                val version = when (coordinate) {
                    is Coordinate.Maven -> packageVersionService.mavenVersion(coordinate.path)
                    is Coordinate.GradlePlugin ->
                        packageVersionService.mavenVersion(
                            coordinate.id.replace('.', '/') + "/" + coordinate.id + ".gradle.plugin"
                        )

                    is Coordinate.SwiftPackage -> packageVersionService.latestTag(library.repo)
                    is Coordinate.GitHubAction -> coordinate.version
                    is Coordinate.NpmPackage -> null
                }
                coordinate.toInstallView(library.repo, version)
            }
        }.orEmpty()

        call.respond(
            FreeMarkerContent(
                "entry.ftl",
                mapOf("view" to entry.toEntryPageView(portfolio, stats, pullRequests, install))
            )
        )
    }

    // The website served `/project/{id}` for years, and those URLs are indexed.
    get("/project/{id}") {
        val id = call.parameters["id"]!!
        val entry = portfolio.entry(id) ?: portfolio.entryByAlias(id)
        call.respondRedirect(entry?.path() ?: "/", permanent = true)
    }

    get("/sitemap.xml") {
        val urls = listOf(
            "${Profile.BASE_URL}/",
            "${Profile.BASE_URL}/archives",
            "${Profile.BASE_URL}/videos",
        ) +
                portfolio.entries.map { Profile.BASE_URL + it.path() } +
                portfolio.videos.map { Profile.BASE_URL + it.path }
        call.respondText(contentType = ContentType.Text.Xml) {
            buildString {
                appendLine("""<?xml version="1.0" encoding="UTF-8"?>""")
                appendLine("""<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">""")
                urls.forEach { appendLine("  <url><loc>$it</loc></url>") }
                appendLine("</urlset>")
            }
        }
    }
}
