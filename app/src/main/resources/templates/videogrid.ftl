<#macro grid videos>
<div class="videos">
    <#list videos as video>
    <a class="video" href="${video.url}">
        <span class="thumb">
            <img src="${video.thumbnail}" alt="" loading="lazy" width="480" height="270">
            <span class="play" aria-hidden="true">▶</span>
        </span>
        <span class="video-title">${video.title}</span>
        <span class="video-meta">
            ${video.publishedAt}<#if video.channel??> · with ${video.channel}</#if><#list video.about as entry> · ${entry.name}</#list>
        </span>
    </a>
    </#list>
</div>
</#macro>

<#macro elsewhere videos articles>
<div class="videos">
    <#list videos as video>
    <a class="video" href="${video.url}">
        <span class="thumb">
            <img src="${video.thumbnail}" alt="" loading="lazy" width="480" height="270">
            <span class="play" aria-hidden="true">▶</span>
        </span>
        <span class="video-title">${video.title}</span>
        <span class="video-meta">
            ${video.publishedAt}<#if video.channel??> · with ${video.channel}</#if><#list video.about as entry> · ${entry.name}</#list>
        </span>
    </a>
    </#list>
    <#list articles as article>
    <a class="video" href="${article.url}" rel="noopener">
        <#if article.thumbnail??>
        <span class="thumb article-thumb">
            <img src="${article.thumbnail}" alt="" loading="lazy" width="480" height="270">
        </span>
        <#else>
        <span class="thumb article-cover">
            <span class="article-kind">Article</span>
            <span class="article-publisher">${article.publisher}</span>
        </span>
        </#if>
        <span class="video-title">${article.title}</span>
        <span class="video-meta">${article.publishedAt} · ${article.publisher}</span>
    </a>
    </#list>
</div>
</#macro>
