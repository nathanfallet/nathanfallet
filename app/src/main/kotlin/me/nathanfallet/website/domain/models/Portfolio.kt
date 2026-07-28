package me.nathanfallet.website.domain.models

/**
 * The kind of an external link, used to pick an icon and a label in the views.
 */
enum class LinkKind {
    WEBSITE,
    GITHUB,
    APP_STORE,
    PLAY_STORE,
    DOWNLOAD,
    ARTICLE,
}

/**
 * An external link attached to an entry.
 */
data class Link(
    val kind: LinkKind,
    val label: String,
    val url: String,
)

/**
 * Whether something is still alive, or kept around for the record.
 */
enum class Status {
    /**
     * Actively maintained and reachable.
     */
    LIVE,

    /**
     * Still around, but not maintained anymore.
     */
    SUNSET,
}

/**
 * Anything that can be reached at `/project/{id}`.
 */
sealed interface Entry {
    val id: String
    val name: String
    val tagline: String
    val description: String?
    val links: List<Link>
    val status: Status

    /**
     * Former identifiers, kept so URLs that have been indexed for years still
     * resolve. They redirect to [id].
     */
    val aliases: List<String>
}

/**
 * An entry that runs on some of the libraries below. Declared on the entry, and
 * only there: the reverse index is derived from it.
 */
sealed interface Powered : Entry {
    val poweredBy: List<String>
}

/**
 * A product: something people actually pay for or use.
 */
data class Product(
    override val id: String,
    override val name: String,
    override val tagline: String,
    override val description: String?,
    override val links: List<Link>,
    override val status: Status,
    override val aliases: List<String>,
    /**
     * Identifiers of the libraries this product runs on.
     */
    override val poweredBy: List<String>,
    /**
     * The company or team behind it.
     */
    val by: String?,
) : Powered

/**
 * An open source library.
 */
data class Library(
    override val id: String,
    override val name: String,
    override val tagline: String,
    override val description: String?,
    override val links: List<Link>,
    override val status: Status,
    override val aliases: List<String>,
    /**
     * The GitHub repository, as `owner/name`.
     */
    val repo: String,
    /**
     * Platforms or targets the library supports.
     */
    val targets: List<String>,
    /**
     * Star count to display when the GitHub API cannot be reached. A value read
     * from the API always wins over this one.
     */
    val stars: Int,
    /**
     * Where the library can be installed from, if it is published anywhere.
     */
    val coordinates: List<Coordinate>,
) : Entry

/**
 * A contribution to someone else's open source project. Everything but the
 * repository name is resolved from the GitHub API, including the list of merged
 * pull requests shown on its page.
 */
data class Contribution(
    override val id: String,
    /**
     * The GitHub repository, as `owner/name`.
     */
    val repo: String,
    /**
     * Fallback name, used when GitHub is unreachable.
     */
    override val name: String,
    /**
     * Fallback description, used when GitHub is unreachable.
     */
    override val tagline: String,
    /**
     * Fallback star count, used when GitHub is unreachable.
     */
    val stars: Int = 0,
    /**
     * Whether I am one of the maintainers, and not only an occasional contributor.
     */
    val maintainer: Boolean = false,
) : Entry {
    override val description: String? = null
    override val status = Status.LIVE
    override val aliases = emptyList<String>()
    override val links = listOf(Link(LinkKind.GITHUB, "GitHub", "https://github.com/$repo"))
}

/**
 * Something from before, kept for the record.
 */
data class Archive(
    override val id: String,
    override val name: String,
    override val tagline: String,
    override val description: String?,
    override val links: List<Link>,
    override val aliases: List<String>,
    override val poweredBy: List<String>,
    /**
     * The year it was last touched.
     */
    val year: String?,
) : Powered {
    override val status = Status.SUNSET
}

/**
 * A video from my YouTube channel, optionally about some of the entries above.
 */
data class Video(
    val id: String,
    /**
     * The YouTube identifier, used for the thumbnail and the watch link.
     */
    val youtubeId: String,
    val title: String,
    val description: String?,
    /**
     * ISO date of publication.
     */
    val publishedAt: String,
    /**
     * Identifiers of the entries this video talks about.
     */
    val about: List<String>,
    /**
     * The channel it was published on, when it is not mine. Being a guest is
     * not the same thing as publishing something myself.
     */
    val channel: String? = null,
) {
    val path: String get() = "/videos/$id"
    val watchUrl: String get() = "https://www.youtube.com/watch?v=$youtubeId"
}

/**
 * Something somebody else wrote about me. Not hosted here, so it has no page of
 * its own: the row links straight out.
 */
data class Article(
    val id: String,
    val title: String,
    val url: String,
    /**
     * Who published it.
     */
    val publisher: String,
    /**
     * ISO date of publication.
     */
    val publishedAt: String,
    val summary: String?,
    /**
     * The illustration to show, served through this website rather than hotlinked.
     */
    val image: String?,
) {
    val thumbnailPath: String get() = "/articles/$id/thumbnail.jpg"
}

/**
 * The whole content of the website.
 */
data class Portfolio(
    val products: List<Product>,
    val libraries: List<Library>,
    val contributions: List<Contribution>,
    val archives: List<Archive>,
    val videos: List<Video>,
    val articles: List<Article>,
) {

    private val entriesById: Map<String, Entry> =
        (products + libraries + contributions + archives).associateBy(Entry::id)

    private val entriesByAlias: Map<String, Entry> =
        entriesById.values.flatMap { entry -> entry.aliases.map { it to entry } }.toMap()

    /**
     * Finds any entry by its identifier, whatever its kind.
     */
    fun entry(id: String): Entry? = entriesById[id]

    /**
     * Finds the entry a former identifier used to point to, or null if the
     * identifier is not a known alias.
     */
    fun entryByAlias(alias: String): Entry? = entriesByAlias[alias]

    /**
     * All entries, used to generate the sitemap.
     */
    val entries: Collection<Entry> get() = entriesById.values

    private val videosById: Map<String, Video> = videos.associateBy(Video::id)

    /**
     * The videos I published myself, most recent first.
     */
    val ownVideos: List<Video> get() = videos.filter { it.channel == null }.sortedByDescending(Video::publishedAt)

    /**
     * The ones I only take part in, on somebody else's channel.
     */
    val appearances: List<Video> get() = videos.filter { it.channel != null }.sortedByDescending(Video::publishedAt)

    private val videosByEntry: Map<String, List<Video>> =
        videos.flatMap { video -> video.about.map { it to video } }
            .groupBy({ it.first }, { it.second })

    /**
     * Finds a video by its identifier.
     */
    fun video(id: String): Video? = videosById[id]

    /**
     * Finds an article by its identifier.
     */
    fun article(id: String): Article? = articles.firstOrNull { it.id == id }

    /**
     * The videos talking about an entry, most recent first.
     */
    fun videosAbout(entry: Entry): List<Video> =
        videosByEntry[entry.id].orEmpty().sortedByDescending(Video::publishedAt)

    /**
     * The libraries of mine an entry runs on.
     */
    fun librariesOf(entry: Powered): List<Library> =
        entry.poweredBy.mapNotNull { id -> libraries.firstOrNull { it.id == id } }

    /**
     * The upstream projects an entry runs on, among those I work on. Kept apart
     * from [librariesOf]: writing a library and sending patches to one are not
     * the same claim.
     */
    fun contributionsOf(entry: Powered): List<Contribution> =
        entry.poweredBy.mapNotNull { id -> contributions.firstOrNull { it.id == id } }

    /**
     * What runs on a given library or contribution. Derived from the
     * [Powered.poweredBy] declared on the products and archives, so the
     * relation is written once and read from both ends.
     */
    private val poweredIndex: Map<String, List<Entry>> =
        (products + archives).flatMap { powered -> powered.poweredBy.map { it to powered } }
            .groupBy({ it.first }, { it.second as Entry })

    /**
     * Resolves the entries running on a library or a contribution.
     */
    fun poweredBy(entry: Entry): List<Entry> = poweredIndex[entry.id].orEmpty()
}
