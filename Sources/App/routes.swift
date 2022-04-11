import Vapor

func routes(_ app: Application) throws {
    app.get("", use: HomeController.get)
    app.get("project", ":id", use: ProjectController.get)
}
