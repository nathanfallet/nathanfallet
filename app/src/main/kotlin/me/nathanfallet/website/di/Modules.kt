package me.nathanfallet.website.di

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import me.nathanfallet.website.data.Profile
import me.nathanfallet.website.data.portfolio
import me.nathanfallet.website.domain.models.Portfolio
import me.nathanfallet.website.domain.services.GitHubContributionsService
import me.nathanfallet.website.domain.services.GitHubStatsService
import me.nathanfallet.website.domain.services.ThumbnailService
import me.nathanfallet.website.infrastructure.github.GitHubContributionsServiceImpl
import me.nathanfallet.website.infrastructure.github.GitHubStatsServiceImpl
import me.nathanfallet.website.infrastructure.youtube.ThumbnailServiceImpl
import me.nathanfallet.website.presentation.routes.WebsiteRoutesDependencies
import org.koin.dsl.module

val infrastructureModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }
    single<GitHubStatsService> { GitHubStatsServiceImpl(get()) }
    single<GitHubContributionsService> { GitHubContributionsServiceImpl(get(), Profile.GITHUB_LOGIN) }
    single<ThumbnailService> { ThumbnailServiceImpl(get()) }
}

val presentationModule = module {
    single<Portfolio> { portfolio }
    single { WebsiteRoutesDependencies(get(), get(), get(), get()) }
}
