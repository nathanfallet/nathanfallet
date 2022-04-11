//
//  HomeController.swift
//  
//
//  Created by Nathan Fallet on 11/04/2022.
//

import Vapor

class HomeController {
    
    static func get(_ request: Request) throws -> EventLoopFuture<View> {
        let homeContext = HomeContext(
            title: "Home",
            description: "Nathan Fallet - Developer - contact@nathanfallet.me",
            snippets: [
                """
                {
                  "@context": "http://schema.org",
                  "@type": "Person",
                  "name": "Nathan Fallet",
                  "description": "Web/Java/Swift developer",
                  "url": "https://www.nathanfallet.me/",
                  "email": "contact@nathanfallet.me",
                  "image": "https://www.nathanfallet.me/img/logo.png",
                  "sameAs": [
                    "https://www.youtube.com/channel/UCHVIGHM-gDwLnzuM1YQrHeA",
                    "https://twitter.com/NathanFallet",
                    "https://www.instagram.com/nathanfallet/"
                  ]
                }
                """,
                """
                {
                  "@context": "http://schema.org",
                  "@type": "BreadcrumbList",
                  "itemListElement": [{
                    "@type": "ListItem",
                    "position": 1,
                    "name": "Home",
                    "item": "https://www.nathanfallet.me/"
                  }]
                }
                """
            ],
            projects: projects,
            opensource_maintainer: opensource_maintainer,
            opensource_contributor: opensource_contributor
        )
        
        return request.view.render("home", homeContext)
    }
    
}

struct HomeContext: Codable {
    
    var title: String
    var description: String
    var snippets: [String]
    var projects: [Project]
    var opensource_maintainer: [OpenSourceRepository]
    var opensource_contributor: [OpenSourceRepository]
    
}
