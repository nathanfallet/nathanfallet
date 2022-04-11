//
//  ErrorController.swift
//  
//
//  Created by Nathan Fallet on 11/04/2022.
//

import Vapor

class ErrorController {
    
    static func contextGenerator(status: HTTPStatus, error: Error, request: Request) throws -> ErrorContext {
        let reason: String?
        if let abortError = error as? AbortError {
            reason = abortError.reason
        } else {
            reason = nil
        }
        return ErrorContext(
            title: status.reasonPhrase,
            status: status.code.description,
            statusMessage: status.reasonPhrase,
            reason: reason
        )
    }
    
}

struct ErrorContext: Codable {
    
    var title: String
    var status: String?
    var statusMessage: String?
    var reason: String?
    
}
