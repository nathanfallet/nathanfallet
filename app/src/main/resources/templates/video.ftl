<#import "layout.ftl" as l>
<@l.page view.layout>

<article class="entry">
    <div class="wrap">
        <span class="entry-kind">Video</span>
        <h1>${view.title}</h1>
        <p class="tagline">
            <#if view.channel??>
            Published ${view.publishedAt} with ${view.channel}, on their channel.
            <#else>
            Published ${view.publishedAt} on my YouTube channel.
            </#if>
        </p>

        <div class="entry-layout">
            <div class="prose">
                <a class="video-hero" href="${view.watchUrl}" rel="noopener">
                    <img src="${view.thumbnail}" alt="" loading="lazy" width="1280" height="720">
                    <span class="play" aria-hidden="true">▶</span>
                </a>
                <#if view.description??>${view.description?no_esc}</#if>
            </div>

            <aside class="aside">
                <div>
                    <h2>Watch</h2>
                    <div class="links">
                        <a class="btn btn-primary" href="${view.watchUrl}" rel="noopener">Watch on YouTube</a>
                    </div>
                </div>

                <#if view.about?size gt 0>
                <div>
                    <h2>What it talks about</h2>
                    <div class="related">
                        <#list view.about as entry>
                        <a class="item" href="${entry.url}">
                            <span class="name">${entry.name}</span>
                            <span class="desc">${entry.tagline}</span>
                        </a>
                        </#list>
                    </div>
                </div>
                </#if>
            </aside>
        </div>
    </div>
</article>

</@l.page>
