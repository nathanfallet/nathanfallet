import Vapor
import Leaf
import LeafErrorMiddleware
import VaporSitemap

// configures your application
public func configure(_ app: Application) throws {
    // Get port
    app.http.server.configuration.port = Int(Environment.get("PORT") ?? "8080") ?? 8080
    
    // Middlewares
    app.middleware.use(FileMiddleware(
        publicDirectory: app.directory.publicDirectory
    ))
    app.middleware.use(SitemapMiddleware(
        isSitemap: SitemapController.isSitemap,
        generateURLs: SitemapController.generateURLs
    ))
    app.middleware.use(LeafErrorMiddleware(
        contextGenerator: ErrorController.contextGenerator
    ))
    
    // Register Leaf as view renderer
    app.views.use(.leaf)

    // register routes
    try routes(app)
}
