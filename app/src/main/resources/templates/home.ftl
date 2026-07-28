<#import "layout.ftl" as l>
<#import "videogrid.ftl" as g>
<@l.page view.layout>

<section class="hero">
    <div class="wrap">
        <h1>${view.name}</h1>
        <p class="role">Kotlin engineer. I build businesses on <em>my own open source</em>.</p>
        <div class="intro">${view.intro?no_esc}</div>
        <div class="hero-actions">
            <a class="btn btn-primary" href="#products">See what I ship</a>
            <a class="btn" href="#open-source">Browse the libraries</a>
        </div>
        <div class="stats">
            <div class="stat">
                <div class="value">${view.openSourceProjects}</div>
                <div class="label">Open source projects</div>
            </div>
            <#if (view.totalStars > 0)>
            <div class="stat">
                <div class="value">${view.totalStars}</div>
                <div class="label">Stars on what I maintain</div>
            </div>
            </#if>
            <div class="stat">
                <div class="value">${view.videoCount}</div>
                <div class="label">Videos &amp; podcasts</div>
            </div>
            <div class="stat">
                <div class="value">15</div>
                <div class="label">Years writing code</div>
            </div>
        </div>
    </div>
</section>

<section class="section" id="products">
    <div class="wrap">
        <div class="section-head">
            <span class="eyebrow">Products</span>
            <h2>What the open source pays for</h2>
            <p>What I ship, and the libraries it runs on.</p>
        </div>
        <div class="products">
            <#list view.products as product>
            <a class="product" href="${product.url}">
                <div class="product-head">
                    <h3>${product.name}</h3>
                    <#if product.by??><span class="by">${product.by}</span></#if>
                    <#if product.sunset><span class="tag tag-sunset">Sunset</span></#if>
                </div>
                <p class="tagline">${product.tagline}</p>
                <#if product.poweredBy?size gt 0>
                <div class="powered">
                    <div class="label">Powered by my open source</div>
                    <div class="chips">
                        <#list product.poweredBy as library>
                        <span class="chip">${library.name}</span>
                        </#list>
                    </div>
                </div>
                </#if>
            </a>
            </#list>
        </div>
    </div>
</section>

<section class="section" id="open-source">
    <div class="wrap">
        <div class="section-head">
            <span class="eyebrow">Open source</span>
            <h2>Libraries I maintain</h2>
            <p>Written for a product that needed them, then released. Star counts come straight
                from the GitHub API.</p>
        </div>
        <div class="libraries">
            <#list view.libraries as library>
            <a class="library" href="${library.url}">
                <div class="library-head">
                    <h3>${library.name}</h3>
                    <#if (library.stars > 0)>
                    <span class="stars" title="${library.stars} stars on GitHub">★ ${library.stars}</span>
                    </#if>
                </div>
                <p class="tagline">${library.tagline}</p>
                <div class="library-foot">
                    <#list library.powers as product>
                    <span class="tag tag-powers">Powers ${product.name}</span>
                    </#list>
                    <#if library.language??><span class="tag">${library.language}</span></#if>
                    <#if library.sunset><span class="tag tag-sunset">Archived</span></#if>
                </div>
            </a>
            </#list>
        </div>

        <#if view.archivedLibraries?size gt 0>
        <h3 class="subhead">No longer maintained</h3>
        <div class="libraries">
            <#list view.archivedLibraries as library>
            <a class="library" href="${library.url}">
                <div class="library-head">
                    <h3>${library.name}</h3>
                    <#if (library.stars > 0)>
                    <span class="stars" title="${library.stars} stars on GitHub">★ ${library.stars}</span>
                    </#if>
                </div>
                <p class="tagline">${library.tagline}</p>
                <div class="library-foot">
                    <#list library.powers as project>
                    <span class="tag tag-powers">Powers ${project.name}</span>
                    </#list>
                    <#if library.language??><span class="tag">${library.language}</span></#if>
                </div>
            </a>
            </#list>
        </div>
        </#if>

        <div class="invite">
            <p>${view.openSourceInvite?no_esc}</p>
            <a class="btn" href="https://github.com/nathanfallet" rel="noopener">Follow on GitHub</a>
        </div>
    </div>
</section>

<section class="section" id="contributions">
    <div class="wrap">
        <div class="section-head">
            <span class="eyebrow">Contributions</span>
            <h2>Other people's projects I have shipped code to</h2>
            <p>Kotlin, Swift, and the tools I use every day. A few of them I help maintain.</p>
        </div>
        <div class="contributions">
            <#list view.contributions as contribution>
            <a class="contribution" href="${contribution.url}">
                <span class="repo">
                    ${contribution.repo}
                    <#if contribution.maintainer><span class="tag tag-maintainer">Maintainer</span></#if>
                </span>
                <span class="desc">
                    ${contribution.tagline}
                    <#list contribution.powers as project>
                    <span class="tag tag-powers">Powers ${project.name}</span>
                    </#list>
                </span>
                <#if (contribution.stars > 0)>
                <span class="stars">★ ${contribution.stars}</span>
                </#if>
            </a>
            </#list>
        </div>
    </div>
</section>

<#if view.videos?size gt 0>
<section class="section" id="videos">
    <div class="wrap">
        <div class="section-head">
            <span class="eyebrow">Videos</span>
            <h2>What I talk about</h2>
            <p>Architecture, Kotlin Multiplatform, and the business side of shipping mobile apps.
                Most of them are in French.</p>
        </div>
        <@g.grid view.videos/>
    </div>
</section>
</#if>

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
<section class="section" id="appearances">
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

<section class="section" id="archives">
    <div class="wrap">
        <div class="section-head">
            <span class="eyebrow">Archives</span>
            <h2>Where I come from</h2>
            <p>Minecraft plugins at 14, apps with friends, a vaccination slot finder during a
                pandemic. Not maintained, kept for the record.</p>
        </div>
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
