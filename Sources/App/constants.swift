//
//  constats.swift
//  
//
//  Created by Nathan Fallet on 11/04/2022.
//

import Foundation

// MARK: - Projets

let projects: [Project] = [
    Project(
        id: "converty",
        name: "BaseConverter: Converty",
        description_little: "The fast and easy way to convert numbers with tons of possibilities!",
        description: "",
        last_update: "2022-02-12 10:00:00",
        data: .app(
            "https://play.google.com/store/apps/details?id=me.nathanfallet.converty",
            "https://apps.apple.com/app/base-converter-converty/id1609456234"
        ),
        img: "https://www.nathanfallet.me/img/project-converty.jpg",
        version: "1.1.2"
    ),
    Project(
        id: "craftsearch",
        name: "CraftSearch",
        description_little: "CraftSearch est le premier moteur de recherche Minecraft innovant qui trouve des serveurs in-game depuis 2016. Connectez-vous sur le serveur que VOUS voulez en un clic.",
        description: "",
        last_update: "2018-03-08 14:15:15",
        data: .link("https://www.craftsearch.net/"),
        img: "https://www.craftsearch.net/img/logo.png",
        version: "5.0",
        github: "https://github.com/GroupeMINASTE/CraftSearch"
    ),
    Project(
        id: "deltaalgorithms",
        name: "Delta: Algorithms",
        description_little: "The new math app for students! Discover and create your algorithms to play with maths, with a natural and easy-to-use language.",
        description: "",
        last_update: "2019-10-25 11:10:14",
        data: .link("https://www.delta-algorithms.com/"),
        img: "https://www.nathanfallet.me/img/project-delta.jpg",
        version: "3.6.4"
    ),
    Project(
        id: "extopy",
        name: "Extopy",
        description_little: "Extopy is a completely new kind of NON-PROFIT social network that cares about your privacy/safety.",
        description: "",
        last_update: "2018-04-29 17:19:59",
        data: .link("https://extopy.com/"),
        img: "https://extopy.com/img/logo.png",
        version: "1.0"
    ),
    Project(
        id: "fmobile",
        name: "FMobile",
        description_little: "La première application iOS tout-en-un pour gérer son réseau Free Mobile depuis son iPhone, tel que l'itinérance nationale avec le réseau d'Orange, le suivi consommation, l'accès rapide au code Field Tests et aux numéros utiles, etc...",
        description: "",
        last_update: "2019-07-28 15:22:51",
        data: .link("http://raccourcis.ios.free.fr/fmobile/"),
        img: "https://www.nathanfallet.me/img/project-fmobile.jpg",
        version: "5.0",
        github: "https://github.com/GroupeMINASTE/FMobileS1"
    ),
    Project(
        id: "latexcards",
        name: "LaTeX Cards",
        description_little: "Create cards to learn your formulas and theorems using LaTeX!",
        description: "",
        last_update: "2022-02-27 10:50:00",
        data: .link("https://latexcards.app/"),
        img: "https://www.nathanfallet.me/img/project-latexcards.jpg",
        version: "1.5.5"
    ),
    Project(
        id: "ocaml",
        name: "OCaml: Learn & Code",
        description_little: "An OCaml editor for iOS & macOS",
        description: "",
        last_update: "2021-03-28 19:12:00",
        data: .link("https://ocaml-learn-code.com"),
        img: "https://www.nathanfallet.me/img/project-ocaml.jpg",
        version: "1.2.7",
        github: "https://github.com/NathanFallet/OCaml"
    ),
    Project(
        id: "pickfalling",
        name: "PickFalling",
        description_little: "PickFalling est un jeu mobile. Attrapez les objets qui tombent dans les différents niveaux !",
        description: "<h3>Un jeu simple</h3>\n<p>PickFalling est un jeu simple : son but consiste à attraper les objets qui tombent avec le personnage.</p>\n<h3>Différents niveaux</h3>\n<p>Autour de son but simple, jouez avec différents personnages, comme une licorne, un poisson, un chat, ...</p>",
        last_update: "2018-03-01 00:00:00",
        data: .app(
            "https://play.google.com/store/apps/details?id=fr.zabricraft.pickfalling",
            "https://apps.apple.com/app/pickfalling/id1378426985"
        ),
        img: "https://www.nathanfallet.me/img/project-pickfalling.jpg",
        version: "1.8",
        github: "https://github.com/GroupeMINASTE/PickFalling-iOS"
    ),
    Project(
        id: "plugncraft",
        name: "API PlugNCraft",
        description_little: "Une API complète de serveur minecraft automatique",
        description: "",
        last_update: "2018-08-21 12:37:18",
        data: .link("https://github.com/GroupeMINASTE/PlugNCraft"),
        img: "https://www.nathanfallet.me/img/project-plugncraft.png",
        version: "3.0",
        github: "https://github.com/GroupeMINASTE/PlugNCraft"
    ),
    Project(
        id: "replica",
        name: "Replica",
        description_little: "Le Replica est un mini-jeu dont le but est de reproduire l'image sur le tableau face à vous le plus rapidement possible.",
        description: "<div class=\"page-header\">\n\t<h2 id=\"i\">I. Presentation</h2>\n</div>\n<p>The Replica is a mini game where you have to replica the picture on the table before you the most quickly. The game spends in differents sleeves: the last player who finishe is eradicated.</p>\n<div class=\"page-header\">\n\t<h2 id=\"ii\">II. Installation</h2>\n</div>\n<div class=\"alert alert-danger\">You have to add <a href=\"https://www.spigotmc.org/resources/protocollib.1997/\">ProtocolLib</a> on your server else the Replica doesn't work!<br/>Take the version which corresponds to your server.</div>\n<div class=\"page-header\">\n\t<h3 id=\"ii-1\">1. Download</h3>\n</div>\n<p>To install this mini game on your server, download it <a href=\"https://www.nathanfallet.me/project/replica/download\">here</a>. Move the downloaded file in the <code>/plugins/</code> folder of your server.</p>\n<div class=\"page-header\">\n\t<h3 id=\"ii-2\">2. Configuration</h3>\n</div>\n<p>To configure the plugin, start your server a first time to generate the <code>/plugins/Replica/config.yml</code> file. When the file is generated, open it.<br/>\nYou will find all the options in this table:</p>\n<table class=\"table\">\n\t<thead>\n\t\t<tr>\n\t\t\t<th>Property</th>\n\t\t\t<th>Description</th>\n\t\t</tr>\n\t</thead>\n\t<tbody>\n\t\t<tr>\n\t\t\t<td><code>games-amount</code></td>\n\t\t\t<td>The number of games to load</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>countdown</code></td>\n\t\t\t<td>The delay before the game starts</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>spawn-command</code></td>\n\t\t\t<td>The command the player will dispatch automatically to teleport to the spawn at the end of the game. It can be spawn, hub or anything else depending of your server.</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>reward-commands</code></td>\n\t\t\t<td>The commands the console executes when a player win a game.<br/>Use <code>%player%</code> like the player nickname.</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>pictures</code></td>\n\t\t\t<td>Here you can find all the pictures made with <a href=\"https://www.nathanfallet.me/project/replicapicturemaker\">ReplicaPictureMaker</a></td>\n\t\t</tr>\n\t</tbody>\n</table>\n<p>You have to reload/restart your server to apply changes.</p>\n<div class=\"page-header\">\n\t<h2>III. Commands</h2>\n</div>\n<table class=\"table\">\n\t<thead>\n\t\t<tr>\n\t\t\t<th>Syntax</th>\n\t\t\t<th>Description</th>\n\t\t</tr>\n\t</thead>\n\t<tbody>\n\t\t<tr>\n\t\t\t<td><code>/replica goto</code></td>\n\t\t\t<td>Allow to teleport in the Replica's world</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>/replica buildmode</code></td>\n\t\t\t<td>Allow to enable/disable the buildmode.<br/>The buildmode allow to build in the Replica's area to edit the build area of the game.</td>\n\t\t</tr>\n\t</tbody>\n</table>\n<div class=\"page-header\">\n\t<h2>IV. Video tutorial (Only in French)</h2>\n</div>\n<div class=\"embed-responsive embed-responsive-16by9\"><iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/EOxJrOYIPDw\" frameborder=\"0\" allowfullscreen></iframe></div>",
        last_update: "2017-01-28 08:23:47",
        data: .file("Replica.jar"),
        img: "https://www.nathanfallet.me/img/project-replica.jpg",
        version: "1.3.1",
        github: "https://github.com/NathanFallet/Replica"
    ),
    Project(
        id: "replicapicturemaker",
        name: "ReplicaPictureMaker",
        description_little: "Logiciel permettant de créer des images pour le mini-jeu Replica",
        description: "<div class=\"embed-responsive embed-responsive-16by9\"><iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/EOxJrOYIPDw\" frameborder=\"0\" allowfullscreen></iframe></div>",
        last_update: "2017-01-15 09:31:43",
        data: .file("ReplicaPictureMaker.jar"),
        img: "https://www.nathanfallet.me/img/project-replica.jpg",
        version: "1.0",
        github: "https://github.com/NathanFallet/Replica"
    ),
    Project(
        id: "ringify",
        name: "Ringify: Competition",
        description_little: "Create teams and compete your friends!",
        description: "",
        last_update: "2021-07-17 08:00:00",
        data: .link("https://ringify.app"),
        img: "https://www.nathanfallet.me/img/project-ringify.jpg",
        version: "1.3.5"
    ),
    Project(
        id: "tictaitoe",
        name: "Tic TAI Toe",
        description_little: "A tic tac toe with AI and PRO",
        description: "<p>Discover all the features provided by Tic TAI Toe:</p>\n<ul>\n<li>Play to Tic Tac Toe with you friends</li>\n<li>Play to Tic Tac Toe with an AI</li>\n<li>See how the AI plays against itself</li>\n<li>Enable the dark mode (PRO feature) for fun!</li>\n</ul>\n<p>This game was developed by Nathan FALLET and published as Groupe MINASTE</p>",
        last_update: "2019-04-11 15:20:06",
        data: .app(
            "https://play.google.com/store/apps/details?id=me.nathanfallet.morpiontpe",
            "https://apps.apple.com/app/tic-tai-toe/id1459186328"
        ),
        img: "https://www.nathanfallet.me/img/project-tictaitoe.jpg",
        version: "1.6",
        github: "https://github.com/NathanFallet/MorpionTPE-iOS"
    ),
    Project(
        id: "vitemadose",
        name: "Vite Ma Dose de Vaccin",
        description_little: "Vite Ma Dose est un outil permettant de détecter les rendez-vous de vaccination pour la COVID-19.",
        description: "<p>Vite Ma Dose est un outil permettant de détecter les rendez-vous de vaccination. Cette application rapide, simple et fiable permet de trouver l'ensemble des rendez-vous de vaccination disponibles sur les plateformes de réservation comme Doctolib, Maiia, Keldoc ou Ordoclic, sous réserve d’éligibilité.</p><p>Vite Ma Dose ne collecte aucune donnée personnelle. Ce point d’entrée unique vous met simplement en relation avec l’une des plateformes de réservation, sur laquelle vous pouvez vous inscrire à un créneau dans un centre de vaccination. L’application est gratuite, indépendante, et open-source.</p>",
        last_update: "2021-04-21 22:17:29",
        data: .app(
            "https://play.google.com/store/apps/details?id=com.cvtracker.vmd2",
            "https://apps.apple.com/app/vite-ma-dose/id1563630754"
        ),
        img: "https://www.nathanfallet.me/img/project-vitemadose.jpg",
        version: "1.2.1",
        github: "https://github.com/CovidTrackerFr/vitemadose-ios"
    ),
    Project(
        id: "zabripermission",
        name: "ZabriPermission",
        description_little: "ZabriPermission est un plugin qui va vous permettre de contrôler les permissions de vos joueurs grâce à un système de groupes",
        description: "<div class=\"page-header\">\n\t<h2 id=\"i\">I. Presentation</h2>\n</div>\n<p>ZabriPermission is a plugin that allows you to manage player permissions with a group system.</p>\n<div class=\"page-header\">\n\t<h2 id=\"ii\">II. Installation</h2>\n</div>\n<div class=\"page-header\">\n\t<h3 id=\"ii-1\">1. Download</h3>\n</div>\n<p>To install ZabriPermission on your server, download it <a href=\"https://www.nathanfallet.me/project/zabripermission/download\">here</a>. Move the downloaded file in the <code>/plugins/</code> folder of your server.</p>\n<div class=\"page-header\">\n\t<h3 id=\"ii-2\">2. Configuration</h3>\n</div>\n<p>To configure the plugin, start your server a first time to generate the <code>/plugins/ZabriPermission/config.yml</code>file. When the file is generated, open it.<br/>\nYou will find all the options in this table:</p>\n<table class=\"table\">\n\t<thead>\n\t\t<tr>\n\t\t\t<th>Property</th>\n\t\t\t<th>Description</th>\n\t\t</tr>\n\t</thead>\n\t<tbody>\n\t\t<tr>\n\t\t\t<td><code>change-tablist-name</code></td>\n\t\t\t<td>Show prefix and suffix in the tablist (<code>true</code>/<code>false</code>)</td>\n\t\t</tr>\n\t</tbody>\n</table>\n<p>You have to reload/restart your server to apply changes.</p>\n<div class=\"page-header\">\n\t<h2>III. Commands</h2>\n</div>\n<table class=\"table\">\n\t<thead>\n\t\t<tr>\n\t\t\t<th>Syntax</th>\n\t\t\t<th>Description</th>\n\t\t</tr>\n\t</thead>\n\t<tbody>\n\t\t<tr>\n\t\t\t<td><code>/zp add &lt;group name&gt;</code></td>\n\t\t\t<td>Add a group</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>/zp del &lt;group&gt;</code></td>\n\t\t\t<td>Delete a group</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>/zp info &lt;group&gt;</code></td>\n\t\t\t<td>Show group's informations</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>/zp prefix &lt;group&gt; &lt;prefix&gt;</code></td>\n\t\t\t<td>Change group's prefix</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>/zp addp &lt;group&gt; &lt;permission&gt;</code></td>\n\t\t\t<td>Add a permission to a group</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>/zp delp &lt;group&gt; &lt;permission&gt;</code></td>\n\t\t\t<td>Remove a permission to a group</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>/zp addi &lt;group&gt; &lt;inheritance&gt;</code></td>\n\t\t\t<td>Add an inheritance to a group</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>/zp deli &lt;group&gt; &lt;inheritance&gt;</code></td>\n\t\t\t<td>Remove an inheritance to a group</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>/zp player &lt;name&gt; &lt;group&gt;</code></td>\n\t\t\t<td>Change player's group</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>/zp playeraddp &lt;name&gt; &lt;permission&gt;</code></td>\n\t\t\t<td>Add a permission to a player</td>\n\t\t</tr>\n\t\t<tr>\n\t\t\t<td><code>/zp playerdelp &lt;name&gt; &lt;permission&gt;</code></td>\n\t\t\t<td>Remove a permission to a player</td>\n\t\t</tr>\n\t</tbody>\n</table>\n<div class=\"page-header\">\n\t<h2>IV. Video tutorial (Only in french)</h2>\n</div>\n<div class=\"embed-responsive embed-responsive-16by9\"><iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/XTdyBbTDj-c\" frameborder=\"0\" allowfullscreen></iframe></div>",
        last_update: "2016-12-15 18:30:06",
        data: .file("ZabriPermission.jar"),
        img: "https://via.placeholder.com/350x200.png?text=ZabriPermission",
        version: "1.1.1",
        github: "https://github.com/NathanFallet/ZabriPermission"
    )
].sorted(by: { $0.last_update > $1.last_update })

// MARK: - Open source repositories

let opensource_maintainer: [OpenSourceRepository] = [
    OpenSourceRepository(
        id: "makth",
        name: "Makth",
        description: "A Kotlin library for algebra",
        github: "https://github.com/NathanFallet/Makth"
    ),
    OpenSourceRepository(
        id: "apirequest",
        name: "APIRequest",
        description: "A swift package/android library to interact with a REST API",
        github: "https://github.com/NathanFallet/APIRequest"
    ),
    OpenSourceRepository(
        id: "digianalytics",
        name: "DigiAnalytics",
        description: "Realtime web analytics with privacy and simplicity at its core.",
        github: "https://github.com/GroupeMINASTE/DigiAnalytics"
    ),
    OpenSourceRepository(
        id: "unlockpremium",
        name: "UnlockPremium",
        description: "A package to include a standard \"Unlock premium\" view in iOS and Android apps",
        github: "https://github.com/NathanFallet/UnlockPremium"
    ),
    OpenSourceRepository(
        id: "donateviewcontroller",
        name: "DonateViewController",
        description: "A view controller to make donations.",
        github: "https://github.com/GroupeMINASTE/DonateViewController"
    ),
    OpenSourceRepository(
        id: "sqlite",
        name: "SQLite.swift",
        description: "A type-safe, Swift-language layer over SQLite3.",
        github: "https://github.com/stephencelis/SQLite.swift"
    ),
    OpenSourceRepository(
        id: "cryptoswift",
        name: "CryptoSwift",
        description: "CryptoSwift is a growing collection of standard and secure cryptographic algorithms implemented in Swift",
        github: "https://github.com/krzyzanowskim/CryptoSwift"
    ),
    OpenSourceRepository(
        id: "codescanner",
        name: "CodeScanner",
        description: "A SwiftUI view that is able to scan barcodes, QR codes, and more, and send back what was found.",
        github: "https://github.com/twostraws/CodeScanner"
    ),
    OpenSourceRepository(
        id: "vapor-sitemap",
        name: "vapor-sitemap",
        description: "A dynamic sitemap generator for Vapor.",
        github: "https://github.com/vapor-community/vapor-sitemap"
    )
]

let opensource_contributor: [OpenSourceRepository] = [
    OpenSourceRepository(
        id: "sourceful",
        name: "Sourceful",
        description: "A syntax highlighting source editor for iOS and macOS using UITextView and NSTextView.",
        github: "https://github.com/twostraws/Sourceful"
    ),
    OpenSourceRepository(
        id: "kodeeditor",
        name: "KodeEditor",
        description: "A simple code editor with syntax highlighting and pinch to zoom",
        github: "https://github.com/markusressel/KodeEditor"
    ),
    OpenSourceRepository(
        id: "kodehighlighter",
        name: "KodeHighlighter",
        description: "Simple, extendable code highlighting for Spannables on Android.",
        github: "https://github.com/markusressel/KodeHighlighter"
    ),
    OpenSourceRepository(
        id: "lingo-vapor",
        name: "Lingo-Vapor",
        description: "Vapor provider for Lingo - the Swift localization library",
        github: "https://github.com/vapor-community/Lingo-Vapor"
    )
]
