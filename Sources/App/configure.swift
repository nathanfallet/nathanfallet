import Vapor
import Leaf
import LeafErrorMiddleware

// configures your application
public func configure(_ app: Application) throws {
    // Error middleware
    app.middleware.use(LeafErrorMiddleware(contextGenerator: ErrorController.contextGenerator))
    
    // Public folder middleware
    app.middleware.use(FileMiddleware(publicDirectory: app.directory.publicDirectory))
    
    // Register Leaf as view renderer
    app.views.use(.leaf)

    // register routes
    try routes(app)
}
