<#import "layout.ftl" as l>
<@l.page view.layout>

<section class="error">
    <div class="wrap">
        <div class="code">${view.status}</div>
        <p>${view.message}</p>
        <a class="btn btn-primary" href="/">Back home</a>
    </div>
</section>

</@l.page>
