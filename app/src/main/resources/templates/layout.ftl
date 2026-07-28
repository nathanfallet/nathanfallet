<#macro page layout>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, viewport-fit=cover">
    <title>${layout.title} — Nathan Fallet</title>
    <meta name="description" content="${layout.description}">
    <link rel="canonical" href="${layout.canonical}">

    <meta property="og:type" content="website">
    <meta property="og:site_name" content="Nathan Fallet">
    <meta property="og:title" content="${layout.title}">
    <meta property="og:description" content="${layout.description}">
    <meta property="og:url" content="${layout.canonical}">
    <meta property="og:image" content="https://www.nathanfallet.me/img/profile.jpg">
    <meta name="twitter:card" content="summary">
    <meta name="twitter:title" content="${layout.title}">
    <meta name="twitter:description" content="${layout.description}">
    <meta name="twitter:image" content="https://www.nathanfallet.me/img/profile.jpg">

    <link rel="icon" type="image/jpeg" href="/img/profile.jpg">
    <link rel="apple-touch-icon" href="/img/profile.jpg">
    <link rel="stylesheet" href="/css/styles.css?v=${assets}">
    <#list layout.snippets as snippet>
    <script type="application/ld+json">${snippet?no_esc}</script>
    </#list>
</head>
<body>
<a class="skip" href="#main">Skip to content</a>

<header class="site-header">
    <div class="wrap">
        <a class="brand" href="/">
            <img src="/img/profile.jpg" alt="" width="30" height="30">
            <span>Nathan Fallet</span>
        </a>
        <nav class="nav">
            <a href="/#products">Products</a>
            <a href="/#open-source">Open source</a>
            <a href="/#contributions">Contributions</a>
            <a href="/videos">Videos</a>
            <a href="/#writing">Writing</a>
            <a href="/archives">Archives</a>
        </nav>
        <a class="btn btn-primary" href="mailto:contact@nathanfallet.me">Get in touch</a>
    </div>
</header>

<main id="main">
    <#nested>
</main>

<footer class="site-footer">
    <div class="wrap">
        <div>
            <a href="mailto:contact@nathanfallet.me">contact@nathanfallet.me</a> · France ·
            <a href="/files/cv.pdf">CV</a>
            <span class="built-with">
                Built with <a href="https://kotlinlang.org">Kotlin</a> &amp;
                <a href="https://ktor.io">Ktor</a> ·
                <a href="https://github.com/nathanfallet/nathanfallet">source</a>
            </span>
        </div>
        <div class="socials">
            <a href="https://github.com/nathanfallet">GitHub</a>
            <a href="https://www.linkedin.com/in/nathanfallet/">LinkedIn</a>
            <a href="https://www.youtube.com/@nathanfallet">YouTube</a>
            <a href="https://www.twitch.tv/nathanfallet">Twitch</a>
        </div>
    </div>
</footer>
</body>
</html>
</#macro>
