package me.nathanfallet.website

import io.ktor.server.application.*
import io.ktor.server.netty.*
import me.nathanfallet.website.di.infrastructureModule
import me.nathanfallet.website.di.presentationModule
import me.nathanfallet.website.presentation.config.configureErrorHandling
import me.nathanfallet.website.presentation.config.configureMonitoring
import me.nathanfallet.website.presentation.config.configureRouting
import me.nathanfallet.website.presentation.config.configureTemplating
import org.koin.ktor.plugin.Koin

/**
 * Main entry point of the application.
 */
fun main(args: Array<String>): Unit = EngineMain.main(args)

/**
 * Ktor application module.
 */
fun Application.module() {
    install(Koin) {
        modules(infrastructureModule, presentationModule)
    }
    configureMonitoring()
    configureTemplating()
    configureErrorHandling()
    configureRouting()
}
