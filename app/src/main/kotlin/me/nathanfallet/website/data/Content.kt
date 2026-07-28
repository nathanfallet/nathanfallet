package me.nathanfallet.website.data

import me.nathanfallet.website.domain.dsl.portfolio
import me.nathanfallet.website.domain.models.Status

/**
 * Who I am, shown in the header and in the structured data.
 */
object Profile {
    const val NAME = "Nathan Fallet"
    const val ROLE = "Kotlin engineer, building businesses on my own open source"
    const val EMAIL = "contact@nathanfallet.me"
    const val LOCATION = "France"
    const val BASE_URL = "https://www.nathanfallet.me"
    const val GITHUB_LOGIN = "NathanFallet"

    val intro = """
        I have been writing code since I was 10. Today I run
        <a href="https://guimauve.digital">Guimauve Digital</a>, an app studio, and I build the
        products it sells on libraries I write and release in the open.
    """.trimIndent()

    val socials = listOf(
        "GitHub" to "https://github.com/NathanFallet",
        "LinkedIn" to "https://www.linkedin.com/in/nathanfallet/",
        "YouTube" to "https://www.youtube.com/@nathanfallet",
        "Twitch" to "https://www.twitch.tv/nathanfallet",
    )
}

/**
 * Everything the website renders. Add a project here, nowhere else.
 *
 * Stars, descriptions and languages of anything with a `repo` are read from the
 * GitHub API at runtime, so they never go stale in this file. The `stars` values
 * declared below are only a fallback, shown when the API cannot be reached.
 */
val portfolio = portfolio {

    // MARK: - Products

    product("controlresell") {
        name = "ControlResell"
        tagline = "Sell more, in less time. The reselling automation platform that saves sellers 20+ hours a week."
        by = "Guimauve Digital"
        website("https://controlresell.com")
        appStore("6447619941")
        poweredBy("kdriver", "kourier", "zodable")
        description = """
            <p>ControlResell connects to the marketplaces resellers sell on — Vinted, eBay, Shopify,
            Etsy, Vestiaire Collective — and takes over the repetitive half of the job: publishing,
            answering buyers, negotiating, tracking orders.</p>
            <p>It runs on a fleet of Kotlin services: a Ktor backend, one connector service per
            marketplace, a scraping layer, an image processing API and a Cube.js analytics stack.
            Most of the plumbing underneath is open source, and listed below.</p>
        """.trimIndent()
    }

    // MARK: - Libraries

    library("kdriver") {
        name = "kdriver"
        tagline = "Blazing fast, coroutine-first, undetectable browser automation for Kotlin."
        repo = "cdpdriver/kdriver"
        stars = 107
        targets("JVM", "Android", "Native")
        website("https://cdpdriver.github.io/kdriver/")
        description = """
            <p>A CDP-based browser automation library for Kotlin, in the same family as
            <a href="https://github.com/cdpdriver/zendriver">zendriver</a> on the Python side.
            It drives every marketplace connector behind ControlResell.</p>
            <p>It comes with satellites: <a href="https://github.com/cdpdriver/kdriver-proxy">kdriver-proxy</a>
            (a pure Kotlin SOCKS5 proxy), <a href="https://github.com/cdpdriver/kdriver-mcp">kdriver-mcp</a>
            (an MCP server exposing it to LLMs) and
            <a href="https://github.com/cdpdriver/kdriver-nextjs">kdriver-nextjs</a>.</p>
        """.trimIndent()
    }

    library("kourier") {
        name = "kourier"
        tagline = "Pure Kotlin AMQP 0.9.1 client for RabbitMQ. Coroutines, multiplatform, auto-reconnection, no Java client."
        repo = "kourier-amqp/kourier"
        stars = 44
        targets("JVM", "Native", "Multiplatform")
        website("https://kourier.dev")
    }

    library("zodable") {
        name = "zodable"
        tagline = "Generate zod schemas from Kotlin data classes."
        repo = "zodable/zodable"
        stars = 29
        targets("Kotlin", "TypeScript")
        description = """
            <p>A Gradle plugin that keeps a Kotlin backend and a TypeScript frontend in sync: annotate
            your data classes, get zod schemas out. It is the contract between the ControlResell
            backend and its React app.</p>
        """.trimIndent()
    }

    library("amplitude-kmp") {
        name = "amplitude-kmp"
        tagline = "The official Kotlin Multiplatform SDK for Amplitude Analytics."
        repo = "NathanFallet/amplitude-kmp"
        stars = 19
        targets("Android", "iOS", "JVM", "Multiplatform")
    }

    library("experiment-kmp") {
        name = "experiment-kmp"
        tagline = "Kotlin Multiplatform SDK for Amplitude Experiment."
        repo = "NathanFallet/experiment-kmp"
        stars = 1
        targets("Android", "iOS", "JVM", "Multiplatform")
    }

    library("ant-design-kmp") {
        name = "ant-design-kmp"
        tagline = "Ant Design components for Compose Multiplatform."
        repo = "guimauvedigital/ant-design-kmp"
        stars = 25
        targets("Android", "iOS", "Desktop", "Compose")
    }

    library("ocaml") {
        name = "OCaml: Learn & Code"
        tagline = "An OCaml editor, top level and learning place for iOS, iPadOS and macOS."
        repo = "NathanFallet/ocaml"
        stars = 86
        targets("iOS", "iPadOS", "macOS")
        appStore("1547506826")
        website("https://ocaml-learn-code.com")
        description = """
            <p>A full OCaml environment on iOS: editor, top level, and a course to learn the language.
            Written in Swift, and still the most starred thing I have shipped on my own account.</p>
        """.trimIndent()
    }

    library("shortt") {
        name = "shortt"
        tagline = "A URL shortener built with clean architecture in mind."
        repo = "NathanFallet/shortt"
        stars = 9
        targets("Ktor", "Kotlin Multiplatform")
        description = """
            <p>My reference implementation for a Kotlin backend: Ktor, Koin, Exposed, RabbitMQ through
            kourier, OpenTelemetry, and a Compose Multiplatform client sharing the API definitions.
            This website is built on the same skeleton.</p>
        """.trimIndent()
    }

    library("streamdeck-kotlin-sdk") {
        name = "streamdeck-kotlin-sdk"
        tagline = "A Kotlin SDK to create Stream Deck plugins."
        repo = "NathanFallet/streamdeck-kotlin-sdk"
        stars = 8
        targets("JVM")
    }

    library("cloudflare-api-client") {
        name = "cloudflare-api-client"
        tagline = "Kotlin client for the Cloudflare API."
        repo = "NathanFallet/cloudflare-api-client"
        stars = 6
        targets("Multiplatform")
    }

    library("customerio-api-client") {
        name = "customerio-api-client"
        tagline = "Kotlin client for the Customer.io server API."
        repo = "NathanFallet/customerio-api-client"
        stars = 1
        targets("Multiplatform")
    }

    library("flareon") {
        name = "flareon"
        tagline = "A Firebase Admin SDK for Kotlin Multiplatform."
        repo = "NathanFallet/flareon"
        stars = 1
        targets("Multiplatform")
    }

    library("pkg") {
        name = "pkg"
        tagline = "An open source Maven, npm and PyPI package manager."
        repo = "NathanFallet/pkg"
        stars = 2
        targets("JVM")
    }

    library("stripe-to-amplitude") {
        name = "stripe-to-amplitude"
        tagline = "Load all your Stripe data, past and live, into Amplitude with a consistent event format."
        repo = "guimauvedigital/stripe-to-amplitude"
        stars = 1
        targets("Node.js", "Docker")
    }

    library("generate-sitemap") {
        name = "generate-sitemap"
        tagline = "A GitHub Action to generate a sitemap for GitHub Pages websites."
        repo = "NathanFallet/generate-sitemap"
        stars = 2
        targets("GitHub Actions")
    }

    library("kotlinds") {
        name = "kotlinds"
        tagline = "Kotlin Multiplatform utilities to work with Nintendo DS ROM files."
        repo = "kotlinds/kotlinds"
        stars = 3
        targets("Multiplatform")
        description = """
            <p>The weekend end of the shelf. It powers
            <a href="https://github.com/kotlinds/nds-music-player">nds-music-player</a>, a cross-platform
            player that loads any <code>.nds</code> ROM and plays its full soundtrack, on top of
            <a href="https://github.com/kotlinds/fluidsynth-kmp">fluidsynth-kmp</a>.</p>
        """.trimIndent()
    }

    library("apirequest") {
        name = "APIRequest"
        tagline = "A Swift package and Android library to talk to a REST API."
        repo = "NathanFallet/apirequest"
        stars = 15
        targets("iOS", "macOS", "Android")
        status = Status.SUNSET
    }

    library("unlockpremium") {
        name = "UnlockPremium"
        tagline = "A ready-made \"unlock premium\" screen for iOS and Android apps."
        repo = "NathanFallet/unlockpremium"
        stars = 8
        targets("iOS", "Android")
        status = Status.SUNSET
    }

    library("guimauveui") {
        name = "GuimauveUI"
        tagline = "Reusable UI components for Compose and SwiftUI."
        repo = "NathanFallet/guimauveui"
        stars = 2
        targets("Compose", "SwiftUI")
        status = Status.SUNSET
    }

    library("donateviewcontroller") {
        name = "DonateViewController"
        tagline = "A view controller to collect donations inside an iOS app."
        repo = "groupeminaste/DonateViewController"
        stars = 5
        targets("iOS")
        status = Status.SUNSET
    }

    library("makth") {
        name = "makth"
        tagline = "A Kotlin library for algebra."
        repo = "NathanFallet/makth"
        stars = 9
        targets("Multiplatform")
        status = Status.SUNSET
    }

    library("kaccelero") {
        name = "kaccelero"
        tagline = "An all-in-one toolkit for mobile and web development in Kotlin and Swift."
        repo = "NathanFallet/kaccelero"
        stars = 7
        targets("Multiplatform")
        status = Status.SUNSET
    }

    // MARK: - Contributions
    //
    // Only the repository is declared: name, description and stars come from GitHub.
    // `maintainer = true` marks the ones I co-maintain, not just contributed to.

    contribution("ktorio/ktor", "Ktor", "The Kotlin framework for connected applications.", 14495)
    contribution("ktorio/ktor-documentation", "Ktor documentation", "Documentation for the Ktor framework.", 538)
    contribution("JetBrains/koog", "Koog", "JetBrains' JVM framework for building AI agents.", 4477)
    contribution("Heapy/awesome-kotlin", "awesome-kotlin", "A curated list of awesome Kotlin related stuff.", 11367)
    contribution("oshai/kotlin-logging", "kotlin-logging", "Lightweight multiplatform logging framework for Kotlin.", 3095)
    contribution("rabbitmq/rabbitmq-tutorials", "RabbitMQ tutorials", "Tutorials for using RabbitMQ in various ways.", 6889)
    contribution("rabbitmq/rabbitmq-website", "RabbitMQ website", "The RabbitMQ website.", 1214)
    contribution("DamirDenis-Tudor/ktor-server-rabbitmq", "ktor-server-rabbitmq", "The RabbitMQ plugin for Ktor.", 35)
    contribution("fabrikt-io/fabrikt", "Fabrikt", "Generates Kotlin code from OpenAPI 3 specifications.", 281)
    contribution("krzyzanowskim/CryptoSwift", "CryptoSwift", "Cryptographic algorithms implemented in Swift.", 10559, maintainer = true)
    contribution("stephencelis/SQLite.swift", "SQLite.swift", "A type-safe Swift layer over SQLite3.", 10187, maintainer = true)
    contribution("twostraws/CodeScanner", "CodeScanner", "A SwiftUI view that scans barcodes and QR codes.", 1220, maintainer = true)
    contribution("twostraws/Sourceful", "Sourceful", "A syntax highlighting source editor for iOS and macOS.", 714)
    contribution("firebase/firebase-ios-sdk", "Firebase iOS SDK", "The Firebase SDK for Apple platforms.", 6646)
    contribution("funcmike/rabbitmq-nio", "rabbitmq-nio", "A Swift implementation of the AMQP 0.9.1 protocol.", 49)
    contribution("flutter/website", "Flutter website", "The Flutter documentation website.", 3105)
    contribution("translate/translate", "Translate Toolkit", "The localization tools behind Weblate.", 963)
    contribution("ocaml/v2.ocaml.org", "ocaml.org", "The official OCaml website.", 320)
    contribution("ocaml-community/awesome-ocaml", "awesome-ocaml", "A curated collection of OCaml tools and libraries.", 3102)
    contribution("pret/pokeheartgold", "pokeheartgold", "Decompilation of Pokémon HeartGold and SoulSilver.", 559)
    contribution("CovidTrackerFr/vitemadose-ios", "Vite Ma Dose", "The COVID-19 vaccination slot finder used by millions in France.", 33)

    // MARK: - Archives

    archive("flashup") {
        poweredBy("kaccelero", "makth", "guimauveui")
        name = "FlashUp"
        tagline = "Create cards to learn your formulas and theorems using LaTeX."
        year = "2022"
        aliases("latexcards")
        description = """
            <p>Flashcards for people who study things that do not fit in plain text: formulas,
            theorems, proofs. Cards were written in LaTeX and rendered on device.</p>
            <p>Shipped by Guimauve Digital, first released as LaTeX Cards.</p>
        """.trimIndent()
    }

    archive("iapush") {
        poweredBy("kaccelero", "guimauveui")
        name = "iAPush"
        tagline = "Get notified of your App Store and Play Store sales in realtime."
        year = "2025"
        github("NathanFallet/iapush-app")
        github("NathanFallet/iapush-backend", "Backend on GitHub")
        description = """
            <p>A push notification for every in-app purchase, straight from the stores. Shipped by
            Guimauve Digital. Delisted, but the app and its Ktor backend are open source.</p>
        """.trimIndent()
    }

    archive("deltaalgorithms") {
        poweredBy("makth", "apirequest", "unlockpremium")
        name = "Delta: Algorithms"
        tagline = "A math app for students: discover and create algorithms with a natural language."
        year = "2024"
        appStore("1436506800")
        description = "<p>Still on the App Store, no longer maintained. The website is gone.</p>"
    }

    archive("vitemadose") {
        name = "Vite Ma Dose"
        tagline = "The tool that found COVID-19 vaccination slots across every French booking platform."
        year = "2022"
        appStore("1563630754")
        github("CovidTrackerFr/vitemadose-ios")
        description = """
            <p>Built with CovidTracker during the vaccination campaign. Free, independent, open source,
            and collecting no personal data. Still on the App Store, delisted from Google Play.</p>
        """.trimIndent()
    }

    archive("extopy") {
        poweredBy("kaccelero")
        name = "Extopy"
        tagline = "A non-profit social network that cares about your privacy."
        year = "2025"
        website("https://extopy.com")
        github("groupeminaste/extopy-backend")
        github("groupeminaste/extopy-app", "App on GitHub")
    }

    archive("fmobile") {
        name = "FMobile"
        tagline = "The all-in-one iOS app to manage a Free Mobile line: roaming, usage, field test codes."
        year = "2023"
        github("groupeminaste/FMobile-iOS")
        appStore("1483859936")
    }

    archive("tictaitoe") {
        poweredBy("donateviewcontroller")
        name = "Tic TAI Toe"
        tagline = "Tic tac toe against an AI, or watching the AI play itself."
        year = "2022"
        appStore("1459186328")
        playStore("me.nathanfallet.morpiontpe")
        github("NathanFallet/MorpionTPE-iOS")
    }

    archive("converty") {
        poweredBy("kaccelero", "guimauveui", "unlockpremium")
        name = "BaseConverter: Converty"
        tagline = "The fast and easy way to convert numbers between bases."
        year = "2024"
        github("NathanFallet/converty-app")
        description = "<p>Delisted from both stores.</p>"
    }

    archive("ringify") {
        poweredBy("kaccelero", "guimauveui", "apirequest")
        name = "Ringify"
        tagline = "Create teams and compete with your friends."
        year = "2024"
        github("NathanFallet/ringify-app")
        github("NathanFallet/ringify-backend", "Backend on GitHub")
    }

    archive("suitebde") {
        poweredBy("kaccelero", "guimauveui")
        name = "Suite BDE"
        tagline = "An all-in-one tool for French student unions."
        year = "2024"
        github("NathanFallet/suitebde-backend")
        github("NathanFallet/suitebde-app", "App on GitHub")
    }

    archive("craftsearch") {
        name = "CraftSearch"
        tagline = "A Minecraft server search engine, finding servers from inside the game since 2016."
        year = "2020"
        website("https://www.craftsearch.net")
        github("groupeminaste/CraftSearch")
    }

    archive("replica") {
        name = "Replica"
        tagline = "A Minecraft minigame where you race to reproduce the picture in front of you."
        year = "2023"
        github("NathanFallet/replica")
        download("Replica.jar")
        aliases("replicapicturemaker")
        description = """
            <p>One of the first things I ever released, at 14. The picture editor that came with it,
            ReplicaPictureMaker, is <a href="/files/ReplicaPictureMaker.jar">still downloadable</a>.</p>
        """.trimIndent()
    }

    archive("zabripermission") {
        name = "ZabriPermission"
        tagline = "A Minecraft plugin to manage player permissions through groups."
        year = "2018"
        github("NathanFallet/ZabriPermission")
        download("ZabriPermission.jar")
    }

    archive("plugncraft") {
        name = "PlugNCraft"
        tagline = "An API to manage Minecraft servers automatically."
        year = "2022"
        github("groupeminaste/PlugNCraft")
    }

    archive("pickfalling") {
        name = "PickFalling"
        tagline = "A mobile game where you catch falling objects."
        year = "2023"
        github("NathanFallet/pickfalling-ds")
        description = "<p>Delisted from both stores. A Nintendo DS port was started, and never finished.</p>"
    }
}
