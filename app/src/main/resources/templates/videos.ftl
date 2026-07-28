<#import "layout.ftl" as l>
<#import "videogrid.ftl" as g>
<@l.page view.layout>

<section class="entry">
    <div class="wrap">
        <span class="entry-kind">Videos</span>
        <h1>What I talk about</h1>
        <p class="tagline">Architecture, Kotlin Multiplatform, and the business side of shipping
            mobile apps. Most of them are in French.</p>
    </div>
</section>

<section class="section">
    <div class="wrap">
        <@g.grid view.videos/>
    </div>
</section>

<#if view.appearances?size gt 0>
<section class="section">
    <div class="wrap">
        <div class="section-head">
            <span class="eyebrow">Elsewhere</span>
            <h2>Guest appearances</h2>
            <p>Podcasts and interviews on other people's channels.</p>
        </div>
        <@g.grid view.appearances/>
    </div>
</section>
</#if>

</@l.page>
