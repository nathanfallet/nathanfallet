//
//  SitemapController.swift
//  
//
//  Created by Nathan Fallet on 22/04/2022.
//

import VaporSitemap

class SitemapController {
    
    static func isSitemap(_ path: String) -> Bool {
        return path == "/sitemap.xml"
    }
    
    static func generateURLs(_ path: String) -> [SitemapURL] {
        let prefix = "https://www.nathanfallet.me/"
        var paths = [""]
        
        paths.append(contentsOf: projects.map{ "project/\($0.id)" })
        
        return paths.map { path in
            prefix + path
        }
        .map(SitemapURL.init)
    }
    
}
