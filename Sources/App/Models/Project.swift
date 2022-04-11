//
//  Project.swift
//  
//
//  Created by Nathan Fallet on 11/04/2022.
//

import Foundation

struct Project: Codable {
    
    var id: String
    var name: String
    var description_little: String
    var description: String
    var last_update: String
    var data: ProjectData
    var img: String
    var version: String
    var github: String?
    
}
