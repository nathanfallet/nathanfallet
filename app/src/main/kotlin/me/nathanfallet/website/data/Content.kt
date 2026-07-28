package me.nathanfallet.website.data

import me.nathanfallet.website.domain.dsl.portfolio
import me.nathanfallet.website.domain.models.LinkKind
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
    const val GITHUB_LOGIN = "nathanfallet"

    val intro = """
        I have been writing code since I was 10. Today I run
        <a href="https://guimauve.digital">Guimauve Digital</a>, an app studio, and I build the
        products it sells on libraries I write and release in the open.
    """.trimIndent()

    val openSourceInvite = """
        Something here you could use? It is all open source: take it, open an issue, send a
        pull request.
    """.trimIndent()

    val socials = listOf(
        "GitHub" to "https://github.com/nathanfallet",
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
        poweredBy(
            "kdriver", "kourier", "zodable", "pkg", "flareon", "webhooks",
            "stripe-to-amplitude", "ktor", "koog", "cube",
        )
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
        maven("dev.kdriver", "core")
        name = "kdriver"
        tagline = "Blazing fast, coroutine-first, undetectable browser automation for Kotlin."
        repo = "cdpdriver/kdriver"
        stars = 107
        targets("JVM", "Android", "Native")
        website("https://cdpdriver.github.io/kdriver/")
        description = """
            <p>A CDP-based browser automation library for Kotlin, in the same family as
            <a href="/projects/zendriver">zendriver</a>, which I co-maintain on the Python side.
            It drives every marketplace connector behind ControlResell.</p>
            <p>It comes with satellites: <a href="https://github.com/cdpdriver/kdriver-proxy">kdriver-proxy</a>
            (a pure Kotlin SOCKS5 proxy), <a href="https://github.com/cdpdriver/kdriver-mcp">kdriver-mcp</a>
            (an MCP server exposing it to LLMs) and
            <a href="https://github.com/cdpdriver/kdriver-nextjs">kdriver-nextjs</a>.</p>
        """.trimIndent()
    }

    library("kourier") {
        maven("dev.kourier", "amqp-client")
        name = "kourier"
        tagline = "Pure Kotlin AMQP 0.9.1 client for RabbitMQ. Coroutines, multiplatform, auto-reconnection, no Java client."
        repo = "kourier-amqp/kourier"
        stars = 44
        targets("JVM", "Native", "Multiplatform")
        website("https://kourier.dev")
    }

    library("zodable") {
        gradlePlugin("dev.zodable")
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
        maven("me.nathanfallet.amplitude", "analytics-kmp")
        name = "amplitude-kmp"
        tagline = "The official Kotlin Multiplatform SDK for Amplitude Analytics."
        repo = "nathanfallet/amplitude-kmp"
        stars = 19
        targets("Android", "iOS", "JVM", "Multiplatform")
    }

    library("experiment-kmp") {
        maven("me.nathanfallet.amplitude", "experiment-kmp-client")
        name = "experiment-kmp"
        tagline = "Kotlin Multiplatform SDK for Amplitude Experiment."
        repo = "nathanfallet/experiment-kmp"
        stars = 1
        targets("Android", "iOS", "JVM", "Multiplatform")
    }

    library("ant-design-kmp") {
        maven("digital.guimauve.antdesign", "ui")
        name = "ant-design-kmp"
        tagline = "Ant Design components for Compose Multiplatform."
        repo = "guimauvedigital/ant-design-kmp"
        stars = 25
        targets("Android", "iOS", "Desktop", "Compose")
    }

    library("ocaml") {
        name = "OCaml: Learn & Code"
        tagline = "An OCaml editor, top level and learning place for iOS, iPadOS and macOS."
        repo = "nathanfallet/ocaml"
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
        repo = "nathanfallet/shortt"
        stars = 9
        targets("Ktor", "Kotlin Multiplatform")
        description = """
            <p>My reference implementation for a Kotlin backend: Ktor, Koin, Exposed, RabbitMQ through
            kourier, OpenTelemetry, and a Compose Multiplatform client sharing the API definitions.
            This website is built on the same skeleton.</p>
        """.trimIndent()
    }

    library("streamdeck-kotlin-sdk") {
        maven("me.nathanfallet.streamdeck", "streamdeck-kotlin-sdk")
        gradlePlugin("me.nathanfallet.streamdeck")
        name = "streamdeck-kotlin-sdk"
        tagline = "A Kotlin SDK to create Stream Deck plugins."
        repo = "nathanfallet/streamdeck-kotlin-sdk"
        stars = 8
        targets("JVM")
    }

    library("cloudflare-api-client") {
        maven("me.nathanfallet.cloudflare", "cloudflare-api-client")
        name = "cloudflare-api-client"
        tagline = "Kotlin client for the Cloudflare API."
        repo = "nathanfallet/cloudflare-api-client"
        stars = 6
        targets("Multiplatform")
    }

    library("customerio-api-client") {
        maven("me.nathanfallet.customerio", "customerio-api-client")
        name = "customerio-api-client"
        tagline = "Kotlin client for the Customer.io server API."
        repo = "nathanfallet/customerio-api-client"
        stars = 1
        targets("Multiplatform")
    }

    library("flareon") {
        maven("me.nathanfallet.flareon", "core")
        name = "flareon"
        tagline = "A Firebase Admin SDK for Kotlin Multiplatform."
        repo = "nathanfallet/flareon"
        stars = 1
        targets("Multiplatform")
    }

    library("pkg") {
        gradlePlugin("digital.guimauve.pkg")
        name = "pkg"
        tagline = "An open source Maven, npm and PyPI package manager."
        repo = "nathanfallet/pkg"
        stars = 2
        targets("JVM")
    }

    library("webhooks") {
        maven("digital.guimauve.webhooks", "discord")
        name = "webhooks"
        tagline = "Kotlin clients to push events to Discord, Slack and friends."
        repo = "guimauvedigital/webhooks"
        stars = 2
        targets("Multiplatform")
    }

    library("stripe-to-amplitude") {
        name = "stripe-to-amplitude"
        tagline = "Load all your Stripe data, past and live, into Amplitude with a consistent event format."
        repo = "guimauvedigital/stripe-to-amplitude"
        stars = 1
        targets("Node.js", "Docker")
    }

    library("generate-sitemap") {
        githubAction("guimauvedigital/generate-sitemap", "v1")
        name = "generate-sitemap"
        tagline = "A GitHub Action to generate a sitemap for GitHub Pages websites."
        repo = "nathanfallet/generate-sitemap"
        stars = 2
        targets("GitHub Actions")
    }

    library("kotlinds") {
        maven("dev.kotlinds", "nds-all")
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
        maven("me.nathanfallet.apirequest", "apirequest")
        swiftPackage(indexed = true)
        name = "APIRequest"
        tagline = "A Swift package and Android library to talk to a REST API."
        repo = "nathanfallet/apirequest"
        stars = 15
        targets("iOS", "macOS", "Android")
        status = Status.SUNSET
    }

    library("unlockpremium") {
        maven("me.nathanfallet.unlockpremium", "unlockpremium")
        swiftPackage()
        name = "UnlockPremium"
        tagline = "A ready-made \"unlock premium\" screen for iOS and Android apps."
        repo = "nathanfallet/unlockpremium"
        stars = 8
        targets("iOS", "Android")
        status = Status.SUNSET
    }

    library("guimauveui") {
        maven("software.guimauve", "guimauveui")
        swiftPackage()
        name = "GuimauveUI"
        tagline = "Reusable UI components for Compose and SwiftUI."
        repo = "nathanfallet/guimauveui"
        stars = 2
        targets("Compose", "SwiftUI")
        status = Status.SUNSET
    }

    library("donateviewcontroller") {
        swiftPackage(indexed = true)
        name = "DonateViewController"
        tagline = "A view controller to collect donations inside an iOS app."
        repo = "groupeminaste/DonateViewController"
        stars = 5
        targets("iOS")
        status = Status.SUNSET
    }

    library("keychain-swift") {
        name = "Keychain.swift"
        tagline = "The easiest way to store data securely in the keychain, with a UserDefaults-like API."
        repo = "groupeminaste/Keychain.swift"
        stars = 7
        targets("iOS", "macOS")
        swiftPackage()
        status = Status.SUNSET
    }

    library("fmnetwork") {
        name = "FMNetwork"
        tagline = "The FMobile developer pack: read the mobile network state from Swift."
        repo = "groupeminaste/FMNetwork"
        stars = 2
        targets("iOS")
        swiftPackage()
        status = Status.SUNSET
    }

    library("digianalytics") {
        name = "DigiAnalytics"
        tagline = "Realtime web analytics with privacy and simplicity at its core."
        repo = "groupeminaste/DigiAnalytics"
        targets("iOS", "macOS")
        swiftPackage()
        status = Status.SUNSET
    }

    library("makth") {
        maven("dev.makth", "core")
        name = "makth"
        tagline = "A Kotlin library for algebra."
        repo = "nathanfallet/makth"
        stars = 9
        targets("Multiplatform")
        status = Status.SUNSET
    }

    library("kaccelero") {
        maven("dev.kaccelero", "core")
        name = "kaccelero"
        tagline = "An all-in-one toolkit for mobile and web development in Kotlin and Swift."
        repo = "nathanfallet/kaccelero"
        stars = 7
        targets("Multiplatform")
        status = Status.SUNSET
    }

    // MARK: - Contributions
    //
    // Only the repository is declared: name, description and stars come from GitHub.
    // `maintainer = true` marks the ones I co-maintain, not just contributed to.

    contribution(
        "cdpdriver/zendriver", "zendriver",
        "A blazing fast, async-first, undetectable web scraping framework for Python.",
        1375, maintainer = true,
    )
    contribution(
        "cube-js/cube", "Cube",
        "The open source semantic layer for analytics.", 20501,
        note = """
            <p>I migrated the MySQL driver from <code>mysql</code> to <code>mysql2</code>, which
            fixed the <code>caching_sha2_password</code> failures on recent MySQL servers. A
            maintainer cherry-picked the change into their own release branch, so it landed as a
            commit rather than a pull request of mine.</p>
        """.trimIndent(),
    )
    contribution("ktorio/ktor", "Ktor", "The Kotlin framework for connected applications.", 14495)
    contribution("ktorio/ktor-documentation", "Ktor documentation", "Documentation for the Ktor framework.", 538)
    contribution("JetBrains/koog", "Koog", "JetBrains' JVM framework for building AI agents.", 4477)
    contribution("oshai/kotlin-logging", "kotlin-logging", "Lightweight multiplatform logging framework for Kotlin.", 3095)
    contribution("rabbitmq/rabbitmq-tutorials", "RabbitMQ tutorials", "Tutorials for using RabbitMQ in various ways.", 6889)
    contribution("rabbitmq/rabbitmq-website", "RabbitMQ website", "The RabbitMQ website.", 1214)
    contribution("DamirDenis-Tudor/ktor-server-rabbitmq", "ktor-server-rabbitmq", "The RabbitMQ plugin for Ktor.", 35)
    contribution("fabrikt-io/fabrikt", "Fabrikt", "Generates Kotlin code from OpenAPI 3 specifications.", 281)
    contribution("vapor-community/Lingo-Vapor", "Lingo-Vapor",
        "The Vapor provider for Lingo, the Swift localization library.", 59)
    contribution("vapor-community/google-cloud-kit", "google-cloud-kit",
        "A Swift toolkit for the Google Cloud Platform APIs.", 57)
    contribution("vapor-community/vapor-sitemap", "vapor-sitemap",
        "A dynamic sitemap generator for Vapor.", 8, maintainer = true)
    contribution("krzyzanowskim/CryptoSwift", "CryptoSwift", "Cryptographic algorithms implemented in Swift.", 10559, maintainer = true)
    contribution("stephencelis/SQLite.swift", "SQLite.swift", "A type-safe Swift layer over SQLite3.", 10187, maintainer = true)
    contribution("twostraws/CodeScanner", "CodeScanner", "A SwiftUI view that scans barcodes and QR codes.", 1220, maintainer = true)
    contribution("twostraws/Sourceful", "Sourceful", "A syntax highlighting source editor for iOS and macOS.", 714)
    contribution("firebase/firebase-ios-sdk", "Firebase iOS SDK", "The Firebase SDK for Apple platforms.", 6646)
    contribution("funcmike/rabbitmq-nio", "rabbitmq-nio", "A Swift implementation of the AMQP 0.9.1 protocol.", 49)
    contribution("flutter/website", "Flutter website", "The Flutter documentation website.", 3105)
    contribution("translate/translate", "Translate Toolkit", "The localization tools behind Weblate.", 963)
    contribution("ocaml/v2.ocaml.org", "ocaml.org", "The official OCaml website.", 320)
    contribution("pret/pokeheartgold", "pokeheartgold", "Decompilation of Pokémon HeartGold and SoulSilver.", 559)
    contribution("CovidTrackerFr/vitemadose-ios", "Vite Ma Dose", "The COVID-19 vaccination slot finder used by millions in France.", 33)

    // MARK: - Archives

    archive("flashup") {
        poweredBy("ktor", "kaccelero", "makth", "guimauveui")
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
        poweredBy("ktor", "kaccelero", "guimauveui")
        name = "iAPush"
        tagline = "Get notified of your App Store and Play Store sales in realtime."
        year = "2025"
        github("nathanfallet/iapush-app")
        github("nathanfallet/iapush-backend", "Backend on GitHub")
        description = """
            <p>A push notification for every in-app purchase, straight from the stores. Shipped by
            Guimauve Digital. Delisted, but the app and its Ktor backend are open source.</p>
        """.trimIndent()
    }

    archive("deltaalgorithms") {
        poweredBy("sqlite-swift", "makth", "apirequest", "unlockpremium")
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
        poweredBy("ktor", "kaccelero")
        name = "Extopy"
        tagline = "A non-profit social network that cares about your privacy."
        year = "2025"
        website("https://extopy.com")
        github("groupeminaste/extopy-backend")
        github("groupeminaste/extopy-app", "App on GitHub")
    }

    archive("fmobile") {
        poweredBy("fmnetwork")
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
        github("nathanfallet/MorpionTPE-iOS")
    }

    archive("converty") {
        poweredBy("sqlite-swift", "kaccelero", "guimauveui", "unlockpremium")
        name = "BaseConverter: Converty"
        tagline = "The fast and easy way to convert numbers between bases."
        year = "2024"
        github("nathanfallet/converty-app")
        description = "<p>Delisted from both stores.</p>"
    }

    archive("ringify") {
        poweredBy("ktor", "kaccelero", "guimauveui", "apirequest")
        name = "Ringify"
        tagline = "Create teams and compete with your friends."
        year = "2024"
        github("nathanfallet/ringify-app")
        github("nathanfallet/ringify-backend", "Backend on GitHub")
    }

    archive("suitebde") {
        poweredBy("ktor", "kaccelero", "guimauveui")
        name = "Suite BDE"
        tagline = "An all-in-one tool for French student unions."
        year = "2024"
        github("nathanfallet/suitebde-backend")
        github("nathanfallet/suitebde-app", "App on GitHub")
    }

    archive("code-community") {
        name = "code.community"
        tagline = "A developer community on Instagram, grown to 80,000 followers."
        year = "2020"
        link(LinkKind.WEBSITE, "Instagram", "https://www.instagram.com/code.community/")
        description = """
            <p>I ran code.community for a few years as a teenager: daily posts about programming,
            reposts from the people who followed it, and a handful of small projects built for the
            audience. It reached around 80,000 followers before I stopped posting in 2020.</p>
            <p>It is also where I learned that shipping something is only half the work, and that
            an audience is built one post at a time. That lesson is still what the products above
            run on.</p>
        """.trimIndent()
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
        github("nathanfallet/replica")
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
        github("nathanfallet/ZabriPermission")
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
        github("nathanfallet/pickfalling-ds")
        description = "<p>Delisted from both stores. A Nintendo DS port was started, and never finished.</p>"
    }

    // MARK: - Videos
    //
    // The links to projects come from the video descriptions and, where those
    // were silent, from the transcripts.

    // MARK: - Writing

    writing(
        id = "kotlin-multiplatform-libraries",
        title = "Why We Don't Need Libraries in Kotlin Multiplatform",
        url = "https://nathanfallet.medium.com/why-we-dont-need-libraries-in-kotlin-multiplatform-298053c79a77",
        publisher = "Medium",
        publishedAt = "2025-11-06",
    )

    writing(
        id = "python-to-kotlin",
        title = "From Python to Kotlin: Why We Rewrote Our Scraping Framework in Kotlin",
        url = "https://nathanfallet.medium.com/from-python-to-kotlin-why-we-rewrote-our-scraping-framework-in-kotlin-aebfd69d5838",
        publisher = "Medium",
        publishedAt = "2025-09-22",
        about = listOf("kdriver", "zodable"),
    )

    writing(
        id = "i-changed-the-way-i-study",
        title = "I changed the way I study",
        url = "https://nathanfallet.medium.com/i-changed-the-way-i-study-317a15aff10c",
        publisher = "Medium",
        publishedAt = "2022-07-01",
        about = listOf("flashup"),
    )

    writing(
        id = "rabbitmq-runblocking",
        title = "Stop wrapping your RabbitMQ code in runBlocking",
        url = "https://dev.to/nathanfallet/stop-wrapping-your-rabbitmq-code-in-runblocking-18c2",
        publisher = "dev.to",
        publishedAt = "2025-12-01",
        about = listOf("kourier"),
    )

    writing(
        id = "ktor-native-worker-tutorial",
        title = "Ktor Native Worker Tutorial",
        url = "https://dev.to/nathanfallet/series/34643",
        publisher = "dev.to",
        publishedAt = "2025-12-21",
        parts = 6,
        about = listOf("kourier"),
    )

    // MARK: - Written about me

    article(
        id = "code-community-interview",
        title = "Chit-chat with Nathan Fallet, the manager of code.community",
        url = "https://www.creative-tim.com/blog/web-design/chit-chat-nathan-fallet-founder-code-community/",
        publisher = "Creative Tim",
        publishedAt = "2019-04-12",
        summary = "An interview at 16, about running code.community and learning to build in public.",
        image = "https://www.creative-tim.com/blog/content/images/wordpress/2019/04/" +
                "Screen-Shot-2019-04-12-at-19.45.31.png",
    )

    video("controlresell-podcast") {
        youtubeId = "9v5xs-kHddk"
        title = "Il a QUITTÉ le freelance pour lancer son SaaS"
        publishedAt = "2026-06-21"
        channel = "Nicolas Diot"
        about("controlresell")
        description = """
            <p>A long interview about going from freelance mobile developer to SaaS founder:
            what made my earlier projects fail, how ControlResell grew entirely organically,
            and what running it actually looks like day to day.</p>
        """.trimIndent()
    }

    video("cross-platform-show") {
        youtubeId = "IiPEvKMtxuY"
        title = "ControlResell automatise la vente en ligne : React Native ou Kotlin Multiplatform ?"
        publishedAt = "2025-05-22"
        channel = "David Leuliette (Cross Platform Show)"
        about("controlresell")
        description = """
            <p>An hour and a half on how ControlResell is actually built: why the app ended up in
            React Native while everything behind it is Kotlin, and what that trade-off costs.</p>
        """.trimIndent()
    }

    video("on-t-parle-app") {
        youtubeId = "JjtRqHm1X4Y"
        title = "Ils veulent passer de 400 à 10 000€/mois avec leurs apps mobiles"
        publishedAt = "2025-04-09"
        channel = "Les Ignobles (On t'parle app !)"
        about("flashup", "controlresell")
        description = """
            <p>A long conversation about making a living from mobile apps: what worked on FlashUp,
            what did not, and why ControlResell became the one I bet on.</p>
        """.trimIndent()
    }

    video("paywall") {
        youtubeId = "jAu_nqKwuvY"
        title = "CONSEILS pour améliorer son PAYWALL dans une app mobile"
        publishedAt = "2024-06-19"
        about("iapush")
        description = """
            <p>What I changed on my paywalls to raise the revenue of my mobile apps: what to leave
            free, what to charge for, and how to present it. I take iAPush as the working example.</p>
        """.trimIndent()
    }

    video("tiktok") {
        youtubeId = "UtH0Dn4NVUs"
        title = "Je comprends RIEN à TikTok !"
        publishedAt = "2024-06-04"
        about("ringify")
        description = """
            <p>I installed TikTok to try organic acquisition for Ringify. It did not go as planned,
            and I tell the whole story.</p>
        """.trimIndent()
    }

    video("aso") {
        youtubeId = "dFWgDFFIGFQ"
        title = "Explose les téléchargements de ton app mobile ! (ASO + astuces)"
        publishedAt = "2024-06-01"
        description = """
            <p>My complete ASO strategy to make the most of organic traffic, with a detailed look at
            how the App Store and Play Store algorithms actually rank apps.</p>
        """.trimIndent()
    }

    video("clean-architecture") {
        youtubeId = "1s4w-lyQj44"
        title = "Ma CLEAN ARCHITECTURE projet KMM + KTOR"
        publishedAt = "2024-05-27"
        about("extopy")
        description = """
            <p>How I structure a Kotlin Multiplatform project with a Ktor backend, from the modules
            down to the Helm chart. Extopy is the open source example I walk through.</p>
        """.trimIndent()
    }

    video("kotlinconf-2024") {
        youtubeId = "McVRgV10ScI"
        title = "La #KotlinConf2024 vue par un dev mobile indépendant"
        publishedAt = "2024-05-23"
        description = "<p>What KotlinConf 2024 looked like from the seat of an independent mobile developer.</p>"
    }

    video("kotlin-multiplatform-mobile") {
        youtubeId = "iujXLCbKRNU"
        title = "Kotlin Multiplatform Mobile : C'est quoi ?"
        publishedAt = "2023-07-12"
        description = "<p>A short explanation of what Kotlin Multiplatform Mobile is, and why I use it.</p>"
    }

    video("live-jeu-ds") {
        youtubeId = "FfhIF9qatBk"
        title = "LIVE - On développe un JEU DS #1"
        publishedAt = "2023-05-27"
        about("pickfalling")
        description = """
            <p>Live coding session porting PickFalling to the Nintendo DS. The repository was created
            the same day, and the port was never finished.</p>
        """.trimIndent()
    }

    video("apirequest") {
        youtubeId = "HBbrZJ0f5gg"
        title = "APIRequest.swift: Fetch data from a REST API in your iOS app"
        publishedAt = "2020-05-09"
        about("apirequest")
        description = "<p>How to call a REST API from an iOS app using my APIRequest package.</p>"
    }

    video("live-tic-tai-toe-android") {
        youtubeId = "f_URCGFxfzw"
        title = "LIVE CODE - Tic TAI Toe for Android? Let's create it! (Part 1)"
        publishedAt = "2019-04-12"
        about("tictaitoe")
        description = "<p>Live coding the Android version of Tic TAI Toe, after a lot of people asked for it.</p>"
    }

    video("live-tic-tac-toe-swift") {
        youtubeId = "mRbCu4uizYc"
        title = "LIVE CODE - Create a tic tac toe game with an AI in Swift"
        publishedAt = "2019-04-08"
        about("tictaitoe")
        description = "<p>Building a tic tac toe game with an AI from scratch to a working iOS app, live.</p>"
    }

    video("tpe-morpion") {
        youtubeId = "29M3_ot8dU4"
        title = "TPE - L'intelligence artificielle : une IA imbattable au Morpion"
        publishedAt = "2019-01-09"
        about("tictaitoe")
        description = """
            <p>A school project with Raoul Doyez: building an unbeatable tic tac toe AI. This is where
            everything that became Tic TAI Toe started.</p>
        """.trimIndent()
    }
}
