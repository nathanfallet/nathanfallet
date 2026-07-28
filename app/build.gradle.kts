plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktor)
    application
}

application {
    mainClass.set("me.nathanfallet.website.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_21)
        localImageName.set("nathanfallet")
        findProperty("imageTag")?.let { imageTag.set(it.toString()) }

        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
                appName = provider { "nathanfallet" },
                username = provider { "nathanfallet" },
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
            )
        )
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.datetime)

    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverFreemarker)
    implementation(libs.ktor.serverResources)
    implementation(libs.ktor.serverStatusPages)
    implementation(libs.ktor.serverCallLogging)
    implementation(libs.ktor.serverCompression)
    implementation(libs.ktor.serverAutoHeadResponse)
    implementation(libs.ktor.serverCachingHeaders)
    implementation(libs.ktor.serverDefaultHeaders)

    implementation(libs.ktor.clientCore)
    implementation(libs.ktor.clientCio)
    implementation(libs.ktor.clientContentNegotiation)
    implementation(libs.ktor.serializationKotlinxJson)

    implementation(libs.koin.core)
    implementation(libs.koin.ktor)

    implementation(libs.logback)

    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit5)
}

tasks.test {
    useJUnitPlatform()
}
