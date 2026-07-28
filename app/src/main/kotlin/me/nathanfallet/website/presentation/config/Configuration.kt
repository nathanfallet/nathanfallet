package me.nathanfallet.website.presentation.config

import freemarker.cache.ClassTemplateLoader
import freemarker.core.HTMLOutputFormat
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import me.nathanfallet.website.data.Profile
import me.nathanfallet.website.presentation.routes.WebsiteRoutesDependencies
import me.nathanfallet.website.presentation.routes.websiteRoutes
import me.nathanfallet.website.presentation.views.ErrorPageView
import me.nathanfallet.website.presentation.views.LayoutView
import org.koin.ktor.ext.get
import org.slf4j.event.Level
import kotlin.time.Clock

/**
 * Changes on every boot, and therefore on every deployment. Appended to the
 * static asset URLs so they can be cached hard without ever going stale.
 */
private val assetVersion = Clock.System.now().epochSeconds.toString()

fun Application.configureTemplating() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        outputFormat = HTMLOutputFormat.INSTANCE
        setSharedVariable("assets", assetVersion)
    }
}

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> !call.request.path().startsWith("/css") }
    }
    install(DefaultHeaders) {
        header("X-Content-Type-Options", "nosniff")
        header("Referrer-Policy", "strict-origin-when-cross-origin")
    }
    install(Compression)
    // Without it, a HEAD on any static file answers 404 instead of the headers.
    install(AutoHeadResponse)
    // No options here on purpose: it is the plugin that writes the header, and
    // the values come from the `cacheControl` declared on the static routes.
    install(CachingHeaders)
}

fun Application.configureErrorHandling() {
    install(StatusPages) {
        status(HttpStatusCode.NotFound, HttpStatusCode.InternalServerError) { call, status ->
            call.respond(
                status,
                FreeMarkerContent(
                    "error.ftl",
                    mapOf(
                        "view" to ErrorPageView(
                            layout = LayoutView(
                                title = status.description,
                                description = status.description,
                                canonical = Profile.BASE_URL + call.request.path(),
                                snippets = emptyList(),
                            ),
                            status = status.value.toString(),
                            message = when (status) {
                                HttpStatusCode.NotFound -> "This page does not exist. It may have moved, or never existed at all."
                                else -> "Something went wrong on my side. Sorry about that."
                            },
                        )
                    )
                )
            )
        }
    }
}

fun Application.configureRouting() {
    routing {
        websiteRoutes(get<WebsiteRoutesDependencies>())

        // robots.txt, app-ads.txt, images, downloadable files and the stylesheet.
        // They live in the resources, so the jar is the only thing to ship.
        staticResources("/", "static") {
            cacheControl { listOf(CacheControl.MaxAge(maxAgeSeconds = 60 * 60 * 24 * 7)) }
        }
    }
}
