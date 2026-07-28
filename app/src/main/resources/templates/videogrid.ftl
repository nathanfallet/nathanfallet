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
