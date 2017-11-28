package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.auth.*
import me.avo.realworld.kotlin.ktor.data.*
import me.avo.realworld.kotlin.ktor.persistence.*
import me.avo.realworld.kotlin.ktor.server.*

fun Route.user(userSource: UserSource) {

    route("users") {

        post("login") {
            val credentials = call.receive<LoginCredentials>()
            val user = LoginHandler().login(credentials)
            call.respond(user)
        }

        post {
            val details = call.receive<RegistrationDetails>()
            val user = LoginHandler().register(details)
            call.respond(user)
        }
    }


    route("user") {

        get {
            val (_, email, _, token) = requireLogin()
            val user = userSource.findUser(email).copy(token = token)
            call.respond(user)
        }

        put {
            val current = requireLogin()
            val new = call.receive<User>()
            val updated = LoginHandler().updateUser(new, current)
            call.respond(updated)
        }
    }
}