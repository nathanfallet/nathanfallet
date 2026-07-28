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

<#if view.writings?size gt 0>
<section class="section" id="writing">
    <div class="wrap">
        <div class="section-head">
            <span class="eyebrow">Writing</span>
            <h2>Things I wrote down</h2>
            <p>Posts on Medium and dev.to, mostly about the Kotlin decisions behind the libraries
                above.</p>
        </div>
        <div class="writings">
            <#list view.writings as post>
            <a class="writing" href="${post.url}" rel="noopener">
                <span class="writing-title">
                    ${post.title}
                    <#if post.parts??><span class="tag">${post.parts} parts</span></#if>
                </span>
                <span class="writing-about">
                    <#list post.about as entry>
                    <span class="tag tag-powers">${entry.name}</span>
                    </#list>
                </span>
                <span class="writing-meta">${post.publisher} · ${post.publishedAt}</span>
            </a>
            </#list>
        </div>
    </div>
</section>
</#if>

<#if view.appearances?size gt 0>
<section class="section">
    <div class="wrap">
        <div class="section-head">
            <span class="eyebrow">Elsewhere</span>
            <h2>Guest appearances</h2>
            <p>Podcasts, interviews and articles on other people's channels.</p>
        </div>
        <@g.elsewhere view.appearances view.articles/>
    </div>
</section>
</#if>

</@l.page>
