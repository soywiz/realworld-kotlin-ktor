package me.avo.realworld.kotlin.ktor.server.routes

import me.avo.realworld.kotlin.ktor.auth.LoginHandler
import me.avo.realworld.kotlin.ktor.data.LoginCredentials
import me.avo.realworld.kotlin.ktor.data.RegistrationDetails
import me.avo.realworld.kotlin.ktor.data.User
import me.avo.realworld.kotlin.ktor.persistence.UserSource
import me.avo.realworld.kotlin.ktor.persistence.UserSourceImpl
import me.avo.realworld.kotlin.ktor.server.requireLogin
import org.jetbrains.ktor.request.receive
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.*

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