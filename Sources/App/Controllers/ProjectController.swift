//
//  ProjectController.swift
//  
//
//  Created by Nathan Fallet on 11/04/2022.
//

import Foundation
import Vapor

class ProjectController {
    
    static func get(_ request: Request) throws -> EventLoopFuture<View> {
        guard let project = projects.first(where: { $0.id == request.parameters.get("id") }) else {
            throw Abort(.notFound)
        }
        
        if case ProjectData.link(let link) = project.data {
            throw Abort.redirect(to: link)
        }
        
        let projectContext = ProjectContext(
            title: project.name,
            description: project.description_little,
            snippets: [
                """
                {
                    "@context": "http://schema.org",
                    "@type": "BreadcrumbList",
                    "itemListElement": [{
                        "@type": "ListItem",
                        "position": 1,
                        "name": "Home",
                        "item": "https://www.nathanfallet.me/"
                    },
                    {
                        "@type": "ListItem",
                        "position": 2,
                        "name": "\(project.name)",
                        "item": "https://www.nathanfallet.me/project/\(project.id)"
                    }]
                }
                """
            ],
            project: project
        )
        
        return request.view.render("project", projectContext)
    }
    
}

struct ProjectContext: Codable {
    
    var title: String
    var description: String
    var snippets: [String]
    var project: Project
    
}
