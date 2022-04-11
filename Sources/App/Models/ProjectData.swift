//
//  ProjectData.swift
//  
//
//  Created by Nathan Fallet on 11/04/2022.
//

import Foundation

enum ProjectData: Codable {
    
    case link(String) // URL
    case app(String?, String?) // Google, Apple
    case file(String) // File name
    
}
