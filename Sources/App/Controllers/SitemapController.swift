//
//  SitemapController.swift
//  
//
//  Created by Nathan Fallet on 22/04/2022.
//

import Vapor
import VaporSitemap

class SitemapController {
    
    static func isSitemap(_ req: Request) -> Bool {
        return req.url.path == "/sitemap.xml"
    }
    
    static func generateURLs(_ req: Request) -> [SitemapURL] {
        let prefix = "https://www.nathanfallet.me/"
        var paths = [""]
        
        paths.append(contentsOf: projects.map{ "project/\($0.id)" })
        
        return paths.map { path in
            prefix + path
        }
        .map(SitemapURL.init)
    }
    
}
