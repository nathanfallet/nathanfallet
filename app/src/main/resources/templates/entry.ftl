<#import "layout.ftl" as l>
<@l.page view.layout>

<article class="entry">
    <div class="wrap">
        <span class="entry-kind">${view.kind}</span>
        <h1>${view.name}</h1>
        <p class="tagline">${view.tagline}</p>

        <#if view.sunset>
        <div class="banner">This project is not maintained anymore. It stays here for the record.</div>
        </#if>

        <div class="entry-layout">
            <div class="prose">
                <#if view.description??>${view.description?no_esc}</#if>

                <#if view.pullRequests?size gt 0>
                <h2>What I shipped there</h2>
                <p>My merged pull requests on this repository, straight from the GitHub API.</p>
                <ol class="pull-requests">
                    <#list view.pullRequests as pr>
                    <li>
                        <a href="${pr.url}" rel="noopener">
                            <span class="pr-title">${pr.title}</span>
                            <span class="pr-meta">
                                #${pr.number?c}<#if pr.mergedAt??> · merged ${pr.mergedAt}</#if>
                            </span>
                        </a>
                    </li>
                    </#list>
                </ol>
                </#if>
            </div>

            <aside class="aside">
                <#if view.links?size gt 0>
                <div>
                    <h2>Links</h2>
                    <div class="links">
                        <#list view.links as link>
                        <a class="btn" href="${link.url}"<#if link.url?starts_with("http")> rel="noopener"</#if>>${link.label}</a>
                        </#list>
                    </div>
                </div>
                </#if>

                <#if view.meta?size gt 0>
                <div>
                    <h2>Details</h2>
                    <div class="meta-list">
                        <#list view.meta as meta>
                        <div class="row"><span class="k">${meta.label}</span><span class="v">${meta.value}</span></div>
                        </#list>
                    </div>
                </div>
                </#if>

                <#list view.related as group>
                <div>
                    <h2>${group.title}</h2>
                    <div class="related">
                        <#list group.entries as entry>
                        <a class="item" href="${entry.url}">
                            <span class="name">${entry.name}</span>
                            <span class="desc">${entry.tagline}</span>
                        </a>
                        </#list>
                    </div>
                </div>
                </#list>
            </aside>
        </div>
    </div>
</article>

</@l.page>
