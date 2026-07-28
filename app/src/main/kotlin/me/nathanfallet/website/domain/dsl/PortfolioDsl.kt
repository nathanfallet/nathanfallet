package me.nathanfallet.website.domain.dsl

import me.nathanfallet.website.domain.models.Archive
import me.nathanfallet.website.domain.models.Contribution
import me.nathanfallet.website.domain.models.Entry
import me.nathanfallet.website.domain.models.Library
import me.nathanfallet.website.domain.models.Link
import me.nathanfallet.website.domain.models.LinkKind
import me.nathanfallet.website.domain.models.Portfolio
import me.nathanfallet.website.domain.models.Powered
import me.nathanfallet.website.domain.models.Product
import me.nathanfallet.website.domain.models.Status
import me.nathanfallet.website.domain.models.Video

@DslMarker
annotation class PortfolioDsl

/**
 * Shared bits of every entry builder.
 */
@PortfolioDsl
sealed class EntryBuilder(protected val id: String) {

    /**
     * Display name. Defaults to the identifier.
     */
    var name: String = id

    /**
     * One line, shown in listings. Keep it short.
     */
    var tagline: String = ""

    /**
     * Optional long form, rendered as HTML on the entry page.
     */
    var description: String? = null

    /**
     * Whether it is still alive. Products and libraries default to [Status.LIVE].
     */
    var status: Status = Status.LIVE

    protected val links = mutableListOf<Link>()
    protected val aliases = mutableListOf<String>()

    /**
     * Declares former identifiers of this entry. Old URLs keep working and
     * redirect to the current one.
     */
    fun aliases(vararg former: String) {
        aliases += former
    }

    fun website(url: String, label: String = "Website") = link(LinkKind.WEBSITE, label, url)

    fun github(repo: String, label: String = "GitHub") =
        link(LinkKind.GITHUB, label, "https://github.com/$repo")

    fun appStore(appId: String, label: String = "App Store") =
        link(LinkKind.APP_STORE, label, "https://apps.apple.com/app/id$appId")

    fun playStore(packageName: String, label: String = "Play Store") =
        link(LinkKind.PLAY_STORE, label, "https://play.google.com/store/apps/details?id=$packageName")

    fun download(fileName: String, label: String = "Download") =
        link(LinkKind.DOWNLOAD, label, "/files/$fileName")

    fun article(url: String, label: String) = link(LinkKind.ARTICLE, label, url)

    fun link(kind: LinkKind, label: String, url: String) {
        links += Link(kind, label, url)
    }
}

/**
 * Shared by the entries that run on the declared libraries.
 */
@PortfolioDsl
sealed class PoweredEntryBuilder(id: String) : EntryBuilder(id) {

    protected val poweredBy = mutableListOf<String>()

    /**
     * Declares which of the libraries below this entry runs on. Identifiers are
     * checked against the declared libraries when the portfolio is built.
     */
    fun poweredBy(vararg libraryIds: String) {
        poweredBy += libraryIds
    }
}

@PortfolioDsl
class ProductBuilder(id: String) : PoweredEntryBuilder(id) {

    /**
     * The company or team shipping it.
     */
    var by: String? = null

    internal fun build() = Product(id, name, tagline, description, links, status, aliases, poweredBy, by)
}

@PortfolioDsl
class LibraryBuilder(id: String) : EntryBuilder(id) {

    /**
     * The GitHub repository, as `owner/name`. Stars and activity are read from it.
     */
    var repo: String = ""

    /**
     * Star count to fall back on when GitHub is unreachable. Keep it roughly in
     * sync by hand: it is only ever shown when the API call fails.
     */
    var stars: Int = 0

    private val targets = mutableListOf<String>()

    fun targets(vararg names: String) {
        targets += names
    }

    internal fun build(): Library {
        require(repo.isNotBlank()) { "Library '$id' is missing its repo" }
        return Library(id, name, tagline, description, links + Link(LinkKind.GITHUB, "GitHub", "https://github.com/$repo"), status, aliases, repo, targets, stars)
    }
}

@PortfolioDsl
class ArchiveBuilder(id: String) : PoweredEntryBuilder(id) {

    /**
     * The year it was last touched.
     */
    var year: String? = null

    internal fun build() = Archive(id, name, tagline, description, links, aliases, poweredBy, year)
}

@PortfolioDsl
class VideoBuilder(private val id: String) {

    /**
     * The YouTube identifier, the `v=` part of the watch URL.
     */
    var youtubeId: String = ""

    var title: String = ""

    /**
     * What the video is about, in my own words. Rendered as HTML.
     */
    var description: String? = null

    /**
     * ISO date of publication.
     */
    var publishedAt: String = ""

    /**
     * Set it when the video lives on somebody else's channel.
     */
    var channel: String? = null

    private val about = mutableListOf<String>()

    /**
     * Declares which projects this video talks about. Identifiers are checked
     * against the declared entries when the portfolio is built.
     */
    fun about(vararg entryIds: String) {
        about += entryIds
    }

    internal fun build(): Video {
        require(youtubeId.isNotBlank()) { "Video '$id' is missing its youtubeId" }
        return Video(id, youtubeId, title, description, publishedAt, about, channel)
    }
}

@PortfolioDsl
class PortfolioBuilder {

    private val products = mutableListOf<Product>()
    private val libraries = mutableListOf<Library>()
    private val contributions = mutableListOf<Contribution>()
    private val archives = mutableListOf<Archive>()
    private val videos = mutableListOf<Video>()

    fun product(id: String, block: ProductBuilder.() -> Unit) {
        products += ProductBuilder(id).apply(block).build()
    }

    fun library(id: String, block: LibraryBuilder.() -> Unit) {
        libraries += LibraryBuilder(id).apply(block).build()
    }

    fun contribution(
        repo: String,
        name: String = repo.substringAfter('/'),
        tagline: String = "",
        stars: Int = 0,
        maintainer: Boolean = false,
        id: String = repo.substringAfter('/').lowercase().replace('.', '-'),
    ) {
        contributions += Contribution(id, repo, name, tagline, stars, maintainer)
    }

    fun archive(id: String, block: ArchiveBuilder.() -> Unit) {
        archives += ArchiveBuilder(id).apply(block).build()
    }

    fun video(id: String, block: VideoBuilder.() -> Unit) {
        videos += VideoBuilder(id).apply(block).build()
    }

    internal fun build(): Portfolio {
        val known = libraries.map(Library::id).toSet()
        val powered: List<Powered> = products + archives
        powered.forEach { entry ->
            entry.poweredBy.forEach { id ->
                require(id in known) { "'${entry.id}' is powered by unknown library '$id'" }
            }
        }

        // The reverse index is derived, so the DSL only ever declares the link once.
        val powers = powered.flatMap { entry -> entry.poweredBy.map { it to entry.id } }
            .groupBy({ it.first }, { it.second })
        val resolved = libraries.map { it.copy(powers = powers[it.id].orEmpty()) }

        val ids = (products + resolved + contributions + archives).map(Entry::id)
        val duplicates = ids.groupBy { it }.filterValues { it.size > 1 }.keys
        require(duplicates.isEmpty()) { "Duplicate entry identifiers: ${duplicates.joinToString()}" }

        val clashing = (products + resolved + archives).flatMap(Entry::aliases).filter { it in ids }
        require(clashing.isEmpty()) { "Aliases clashing with real identifiers: ${clashing.joinToString()}" }

        videos.forEach { video ->
            video.about.forEach { id ->
                require(id in ids) { "Video '${video.id}' is about unknown entry '$id'" }
            }
        }
        val videoIds = videos.map(Video::id)
        require(videoIds.size == videoIds.toSet().size) { "Duplicate video identifiers" }

        return Portfolio(products, resolved, contributions, archives, videos)
    }
}

/**
 * Entry point of the DSL declaring the whole content of the website.
 */
fun portfolio(block: PortfolioBuilder.() -> Unit): Portfolio = PortfolioBuilder().apply(block).build()
