package com.eyeque


import grails.rest.*
import grails.converters.*

class OauthTokenController {
	static responseFormats = ['json', 'xml']
	
    def index() {
        println(params)
        if (params.getToken != null) {
            def customerId = Integer.parseInt(params.customerId + "")
            String query = "SELECT token FROM OauthToken WHERE customerId = ?"
            def customerToken = OauthToken.executeQuery(query, [customerId])[0]
            respond 'token': customerToken
            return
        }
        String auth = request.getHeader("authorization")
        String token = auth.split(" ")[1]

        String query = "SELECT customerId FROM OauthToken WHERE token = ?"
        def entityId = OauthToken.executeQuery(query, [token])[0]
        if (entityId == null) {
            response.status = 401
            respond 'msg': 'unauthorized'
            return
        }
        response.status = 200
        response.setHeader('X_Forwarded_User', entityId + "")
        respond 'msg': 'ok'
    }
}
