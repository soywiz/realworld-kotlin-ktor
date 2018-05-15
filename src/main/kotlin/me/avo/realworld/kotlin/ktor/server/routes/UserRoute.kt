package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.auth.AuthenticationException
import me.avo.realworld.kotlin.ktor.auth.LoginHandler
import me.avo.realworld.kotlin.ktor.model.LoginCredentials
import me.avo.realworld.kotlin.ktor.model.RegistrationDetails
import me.avo.realworld.kotlin.ktor.model.User
import me.avo.realworld.kotlin.ktor.repository.UserRepository
import me.avo.realworld.kotlin.ktor.server.requireLogin

fun Route.user(userRepository: UserRepository) {

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
            val user = userRepository.findUser(email)?.copy(token = token) ?: throw AuthenticationException.USER_NOT_FOUND
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