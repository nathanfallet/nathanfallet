package me.nathanfallet.website.presentation.routes

import io.ktor.http.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nathanfallet.website.data.Profile
import me.nathanfallet.website.domain.models.Contribution
import me.nathanfallet.website.domain.models.Library
import me.nathanfallet.website.domain.models.Portfolio
import me.nathanfallet.website.domain.services.GitHubContributionsService
import me.nathanfallet.website.domain.services.GitHubStatsService
import me.nathanfallet.website.presentation.mappers.*
import me.nathanfallet.website.presentation.views.*

data class WebsiteRoutesDependencies(
    val portfolio: Portfolio,
    val gitHubStatsService: GitHubStatsService,
    val gitHubContributionsService: GitHubContributionsService,
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
            .map { it.toContributionView(stats) }
            .sortedByDescending { it.stars }

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
                        totalStars = libraries.sumOf { it.stars },
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

        call.respond(
            FreeMarkerContent(
                "entry.ftl",
                mapOf("view" to entry.toEntryPageView(portfolio, stats, pullRequests))
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
        val urls = listOf("${Profile.BASE_URL}/", "${Profile.BASE_URL}/archives") +
                portfolio.entries.map { Profile.BASE_URL + it.path() }
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
