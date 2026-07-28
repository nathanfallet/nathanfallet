package me.nathanfallet.website

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.testing.*
import me.nathanfallet.website.data.portfolio
import me.nathanfallet.website.domain.dsl.portfolio
import me.nathanfallet.website.domain.models.Contribution
import me.nathanfallet.website.domain.models.Library
import me.nathanfallet.website.domain.models.Portfolio
import me.nathanfallet.website.domain.services.GitHubContributionsService
import me.nathanfallet.website.domain.services.GitHubStatsService
import me.nathanfallet.website.domain.services.PullRequest
import me.nathanfallet.website.domain.services.RepositoryStats
import me.nathanfallet.website.presentation.config.configureErrorHandling
import me.nathanfallet.website.presentation.config.configureRouting
import me.nathanfallet.website.presentation.config.configureTemplating
import me.nathanfallet.website.presentation.routes.WebsiteRoutesDependencies
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Stands in for the real GitHub API: the tests must not depend on the network.
 */
private class FakeGitHubStatsService : GitHubStatsService {
    override suspend fun stats(repos: Collection<String>) =
        repos.associateWith { RepositoryStats(it, 42, "A description", "Kotlin", false) }
}

/**
 * Returns one merged pull request per repository, so the contribution pages have
 * something to render without touching the network.
 */
private class FakeGitHubContributionsService : GitHubContributionsService {
    override suspend fun pullRequests(repo: String) =
        listOf(PullRequest(42, "Fix something in $repo", "https://github.com/$repo/pull/42", "2026-01-15"))
}

/**
 * Stands in for a GitHub API that cannot be reached at all.
 */
private class OfflineGitHubStatsService : GitHubStatsService {
    override suspend fun stats(repos: Collection<String>) = emptyMap<String, RepositoryStats>()
}

private fun Application.testModule(statsService: GitHubStatsService = FakeGitHubStatsService()) {
    install(Koin) {
        modules(
            module {
                single<Portfolio> { portfolio }
                single<GitHubStatsService> { statsService }
                single<GitHubContributionsService> { FakeGitHubContributionsService() }
                single { WebsiteRoutesDependencies(get(), get(), get()) }
            }
        )
    }
    configureTemplating()
    configureErrorHandling()
    configureRouting()
}

class ApplicationTest {

    @Test
    fun homeRendersEveryProductAndLibrary() = testApplication {
        application { testModule() }
        val response = client.get("/")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        // Names go through Freemarker's HTML escaping, so compare on escaped values.
        portfolio.products.forEach { assertTrue(body.contains(it.name.escaped()), "home is missing ${it.name}") }
        portfolio.libraries.forEach { assertTrue(body.contains(it.name.escaped()), "home is missing ${it.name}") }
    }

    private fun String.escaped() = replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")

    @Test
    fun everyEntryHasItsOwnPage() = testApplication {
        application { testModule() }
        portfolio.entries.forEach { entry ->
            val response = client.get("/projects/${entry.id}")
            assertEquals(HttpStatusCode.OK, response.status, "/projects/${entry.id} is broken")
        }
    }

    @Test
    fun legacyProjectUrlsRedirectPermanently() = testApplication {
        application { testModule() }
        val response = client.config { followRedirects = false }.get("/project/kdriver")
        assertEquals(HttpStatusCode.MovedPermanently, response.status)
        assertEquals("/projects/kdriver", response.headers[HttpHeaders.Location])
    }

    @Test
    fun formerIdentifiersRedirectToTheCurrentOne() = testApplication {
        application { testModule() }
        val response = client.config { followRedirects = false }.get("/projects/latexcards")
        assertEquals(HttpStatusCode.MovedPermanently, response.status)
        assertEquals("/projects/flashup", response.headers[HttpHeaders.Location])
    }

    @Test
    fun unknownProjectReturnsNotFound() = testApplication {
        application { testModule() }
        assertEquals(HttpStatusCode.NotFound, client.get("/projects/does-not-exist").status)
    }

    @Test
    fun sitemapListsEveryPage() = testApplication {
        application { testModule() }
        val body = client.get("/sitemap.xml").bodyAsText()
        portfolio.entries.forEach {
            assertTrue(body.contains("/projects/${it.id}"), "sitemap is missing ${it.id}")
        }
    }

    @Test
    fun starsFallBackToTheDeclaredValuesWhenGitHubIsUnreachable() = testApplication {
        application { testModule(OfflineGitHubStatsService()) }
        val body = client.get("/").bodyAsText()
        val kdriver = portfolio.libraries.first { it.id == "kdriver" }
        assertTrue(kdriver.stars > 0, "kdriver has no fallback star count declared")
        assertTrue(body.contains("★ ${kdriver.stars}"), "the declared fallback is not displayed")
    }

    @Test
    fun aContributionPageListsTheMergedPullRequests() = testApplication {
        application { testModule() }
        val contribution = portfolio.contributions.first { it.repo == "ktorio/ktor" }
        val body = client.get("/projects/${contribution.id}").bodyAsText()
        assertTrue(body.contains("Fix something in ktorio/ktor"), "the pull requests are missing")
        assertTrue(body.contains("merged 2026-01-15"), "the merge date is missing")
    }

    @Test
    fun everyContributionIsReachableFromTheHome() = testApplication {
        application { testModule() }
        val body = client.get("/").bodyAsText()
        portfolio.contributions.forEach {
            assertTrue(body.contains("/projects/${it.id}"), "home does not link to ${it.repo}")
        }
    }

    @Test
    fun staticFilesAreServed() = testApplication {
        application { testModule() }
        listOf("/robots.txt", "/app-ads.txt", "/css/styles.css", "/img/profile.jpg").forEach {
            assertEquals(HttpStatusCode.OK, client.get(it).status, "$it is not served")
        }
    }
}

class PortfolioTest {

    @Test
    fun everyRepositoryLooksLikeAGitHubSlug() {
        val repos = portfolio.libraries.map(Library::repo) + portfolio.contributions.map(Contribution::repo)
        repos.forEach {
            assertTrue(Regex("""^[\w.-]+/[\w.-]+$""").matches(it), "'$it' is not a valid owner/name")
        }
    }

    @Test
    fun theReverseIndexIsBuiltFromTheEntries() {
        val kdriver = portfolio.libraries.first { it.id == "kdriver" }
        assertTrue(kdriver.powers.contains("controlresell"))
        assertNotNull(portfolio.entry("controlresell"))
    }

    @Test
    fun archivesCanRunOnLibrariesToo() {
        val built = portfolio {
            library("lib") { repo = "owner/lib" }
            archive("old") { poweredBy("lib") }
        }
        assertEquals(listOf("old"), built.libraries.first().powers)
    }

    @Test
    fun nothingCanBePoweredByAnUnknownLibrary() {
        assertFailsWith<IllegalArgumentException> {
            portfolio {
                archive("ghost") { poweredBy("nothing") }
            }
        }
    }

    @Test
    fun identifiersCannotClash() {
        assertFailsWith<IllegalArgumentException> {
            portfolio {
                library("one") { repo = "owner/one" }
                archive("one") {}
            }
        }
    }

    @Test
    fun aliasesCannotShadowARealEntry() {
        assertFailsWith<IllegalArgumentException> {
            portfolio {
                library("one") { repo = "owner/one" }
                archive("two") { aliases("one") }
            }
        }
    }
}
