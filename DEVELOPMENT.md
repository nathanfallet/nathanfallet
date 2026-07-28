# nathanfallet.me

The source of [nathanfallet.me](https://www.nathanfallet.me): a Ktor application rendering Freemarker
templates. There is no database. The whole content of the website is declared with a Kotlin DSL in
[`Content.kt`](app/src/main/kotlin/me/nathanfallet/website/data/Content.kt), and star counts are read
from the GitHub API at runtime and cached in memory.

## Running it

```shell
./gradlew :app:run          # http://localhost:8080
./gradlew :app:test         # routes, redirections and content checks
./gradlew :app:buildImage   # Docker image, through the Ktor plugin
```

Set `GITHUB_TOKEN` to raise the GitHub API rate limit used for the star counts (60 requests per hour
without it, 5000 with). The site renders either way: without stats, it simply hides the numbers.

## Adding a project

Everything lives in `Content.kt`, nowhere else.

```kotlin
product("controlresell") {
    name = "ControlResell"
    tagline = "Sell more, in less time."
    website("https://controlresell.com")
    appStore("6447619941")
    poweredBy("kdriver", "kourier", "zodable")  // must match declared libraries
}

library("kdriver") {
    repo = "cdpdriver/kdriver"                  // stars and language come from here
    stars = 107                                 // only shown if the API call fails
    tagline = "Browser automation for Kotlin."
    targets("JVM", "Android", "Native")
    maven("dev.kdriver", "core")                // version resolved at runtime
}

library("apirequest") {
    repo = "nathanfallet/apirequest"
    maven("me.nathanfallet.apirequest", "apirequest")
    swiftPackage(indexed = true)                // indexed = listed on Swift Package Index
}

contribution("ktorio/ktor", stars = 14495)      // name and description come from GitHub
contribution("twostraws/CodeScanner", maintainer = true)  // flags the ones I co-maintain

video("clean-architecture") {
    youtubeId = "1s4w-lyQj44"                   // the `v=` part of the watch URL
    title = "Ma CLEAN ARCHITECTURE projet KMM + KTOR"
    publishedAt = "2024-05-27"
    about("extopy")                             // must match declared entries
}

video("cross-platform-show") {
    youtubeId = "IiPEvKMtxuY"
    channel = "David Leuliette (Cross Platform Show)"  // someone else's channel: a guest appearance
    about("controlresell")
}

archive("replica") {
    year = "2023"
    download("Replica.jar")
    poweredBy("kdriver")                        // archives run on libraries too
    aliases("replicapicturemaker")              // old URLs keep working
}
```

`poweredBy` is declared once, on the product or the archive, and accepts both my own libraries and
the upstream projects I contribute to. The reverse index — what runs on a given project — is derived
from it, so a library or contribution page never repeats the relation. The two are kept apart in the
views: writing a library and sending patches to one are not the same claim.

A library can declare where it is published: `maven`, `gradlePlugin`, `swiftPackage`, `githubAction`
or `npm`. Versions are never written by hand — they are read from `maven-metadata.xml` on Maven
Central and from the git tags for Swift packages, then cached for 24 hours. Failures are cached too,
so an unreachable registry hides the version for a while instead of being retried on every visit.
Each coordinate also links to where the package can be browsed: klibs.io, the Gradle Plugin Portal,
npm, or the Swift Package Index when `indexed = true` — not every Swift package is listed there, and
linking to a "package not found" page would be worse than not linking.

`writing(...)` records something I wrote elsewhere — Medium, dev.to — with `about(...)` to link it
to the projects it discusses. A tutorial split across several posts is declared once, pointing at
the series URL, with `parts = 6`: six near-identical rows would bury everything else. It is kept
apart from `article(...)`: writing something and being written about are two different claims.

`article(...)` records something somebody else wrote about me. Articles have no page of their own —
the card links straight to the publisher — and they sit next to the guest appearances. An `image` is
proxied through `/articles/{id}/thumbnail.jpg` like the video thumbnails: everything is downscaled
and re-encoded as JPEG server side, so a 1.3 MB PNG becomes a 76 KB card and nothing is hotlinked.

Videos declared with a `channel` are guest appearances and land in their own section; the others are
mine. `about(...)` links a video to the projects it talks about, and the project page shows the
videos back. Thumbnails are proxied through `/videos/{id}/thumbnail.jpg` and cached in memory, so
the browser never talks to Google.

Contributions get their own page, listing what of mine landed in the repository. Merged pull requests
come first, read from the GitHub search API when the page is viewed — one request, cached for 24
hours, because that endpoint is rate limited far more aggressively than the rest. When there is no
merged pull request of mine, the commits I authored are listed instead: a maintainer cherry-picking
a change into their own release branch leaves no pull request behind, and the commit tells the story
better than the release pull request that happened to carry it. `note = "..."` adds a paragraph when
neither is self-explanatory.

The `stars` values are a fallback, not the source of truth: a successful API call always wins. They
exist so a rate limited or unreachable GitHub degrades into slightly stale numbers instead of no
numbers at all. Results are cached for 24 hours, and a failed refresh is not retried for 30 minutes.

The portfolio is validated when the application starts: an unknown library in `poweredBy`, a
duplicate identifier or an alias shadowing a real entry fails the boot instead of shipping a broken
page.

## Layout

```
app/src/main/kotlin/me/nathanfallet/website/
├── Application.kt            entry point
├── data/Content.kt           the whole content of the website
├── di/Modules.kt             Koin modules
├── domain/
│   ├── models/               Portfolio, Product, Library, Contribution, Archive
│   ├── dsl/                  the DSL used by Content.kt
│   └── services/             GitHubStatsService
├── infrastructure/github/    the GitHub API client and its cache
└── presentation/
    ├── config/               templating, routing, error handling, monitoring
    ├── routes/               home, entry pages, archives, sitemap, redirections
    ├── mappers/              domain to view
    └── views/                what the templates see
```

Templates are in `app/src/main/resources/templates`, static assets in
`app/src/main/resources/static` — including `robots.txt` and `app-ads.txt`, which have to stay served
at the root.

## URLs

`/projects/{id}` is canonical. `/project/{id}`, served for years and still indexed, redirects with a
301, and former identifiers declared through `aliases(...)` do the same.

## Deployment

Pushing to `main` runs the tests, builds the image with the Ktor Gradle plugin and publishes it to
Docker Hub as `nathanfallet/nathanfallet`. The Helm chart lives in `helm/nathanfallet`.

The GitHub token is read from a secret, which the `gh` CLI can fill in directly. The command below is
idempotent, so the same one creates the secret and rotates it:

```shell
kubectl create secret generic nathanfallet-github \
  --from-literal=token="$(gh auth token)" \
  --dry-run=client -o yaml | kubectl apply -f -

helm upgrade -i nathanfallet ./helm/nathanfallet \
  --set github.tokenSecret=nathanfallet-github
```

Add `-n <namespace>` to both commands to deploy somewhere other than the current context. After
rotating the token, restart the pods so they pick it up:

```shell
kubectl rollout restart deploy nathanfallet
```

Deploying without the secret works too — drop the `--set` and the site falls back to unauthenticated
GitHub calls, which means 60 requests per hour and per IP.

> **On which token to use.** `gh auth token` returns the personal token of the `gh` CLI, which
> usually carries `repo`, `workflow` and `gist` scopes. This service only ever reads public
> repositories, so a fine-grained token with no scope at all is enough, and far less to lose if the
> cluster secret leaks. Generate one at https://github.com/settings/tokens and pass it instead:
>
> ```shell
> kubectl create secret generic nathanfallet-github \
>   --from-literal=token="$GITHUB_READONLY_TOKEN" \
>   --dry-run=client -o yaml | kubectl apply -f -
> ```
