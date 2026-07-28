<#import "layout.ftl" as l>
<@l.page view.layout>

<section class="entry">
    <div class="wrap">
        <span class="entry-kind">Archives</span>
        <h1>Where I come from</h1>
        <p class="tagline">Projects I am not maintaining anymore. Some are still online, most are
            not. They are here because they are part of the story.</p>
    </div>
</section>

<section class="section">
    <div class="wrap">
        <div class="archives">
            <#list view.archives as archive>
            <a class="archive" href="${archive.url}">
                <span>${archive.name}</span>
                <#if archive.year??><span class="year">${archive.year}</span></#if>
            </a>
            </#list>
        </div>
    </div>
</section>

</@l.page>
