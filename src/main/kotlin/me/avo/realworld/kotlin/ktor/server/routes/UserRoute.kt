package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.auth.UserNotFound
import me.avo.realworld.kotlin.ktor.model.LoginCredentials
import me.avo.realworld.kotlin.ktor.model.RegistrationDetails
import me.avo.realworld.kotlin.ktor.model.User
import me.avo.realworld.kotlin.ktor.repository.UserRepository
import me.avo.realworld.kotlin.ktor.server.jwtAuth
import me.avo.realworld.kotlin.ktor.server.requireLogin
import me.avo.realworld.kotlin.ktor.service.AuthService

fun Route.user(userRepository: UserRepository) {
    val authService = AuthService(userRepository)

    route("users") {

        post("login") {
            val credentials = call.receive<LoginCredentials>()
            val user = authService.login(credentials)
            call.respond(user)
        }

        post {
            val details = call.receive<RegistrationDetails>()
            val user = authService.register(details)
            call.respond(user)
        }
    }

    jwtAuth {
        route("user") {
            get {
                val (_, email, _, token) = requireLogin()
                val user = userRepository.findUser(email)?.copy(token = token) ?: throw UserNotFound
                call.respond(user)
            }

            put {
                val current = requireLogin()
                val new = call.receive<User>()
                val updated = authService.updateUser(new, current)
                call.respond(updated)
            }
        }
    }
}