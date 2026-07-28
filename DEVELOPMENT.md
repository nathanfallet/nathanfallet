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
}

contribution("ktorio/ktor", stars = 14495)      // name and description come from GitHub
contribution("twostraws/CodeScanner", maintainer = true)  // flags the ones I co-maintain

archive("replica") {
    year = "2023"
    download("Replica.jar")
    poweredBy("kdriver")                        // archives run on libraries too
    aliases("replicapicturemaker")              // old URLs keep working
}
```

`poweredBy` is declared once, on the product or the archive. The reverse index — which entries a
library powers — is derived from it, so a library page never has to repeat the relation.

Contributions get their own page, listing my merged pull requests on the repository. They are read
from the GitHub search API when the page is viewed — one request, cached for 24 hours — because that
endpoint is rate limited far more aggressively than the rest of the API.

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
