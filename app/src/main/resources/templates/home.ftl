<#import "layout.ftl" as l>
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
                <div class="value">${view.libraries?size}</div>
                <div class="label">Libraries maintained</div>
            </div>
            <#if (view.totalStars > 0)>
            <div class="stat">
                <div class="value">${view.totalStars}</div>
                <div class="label">Stars on my own repos</div>
            </div>
            </#if>
            <div class="stat">
                <div class="value">${view.contributions?size}</div>
                <div class="label">Projects contributed to</div>
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
                <span class="desc">${contribution.tagline}</span>
                <#if (contribution.stars > 0)>
                <span class="stars">★ ${contribution.stars}</span>
                </#if>
            </a>
            </#list>
        </div>
    </div>
</section>

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
