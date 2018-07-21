package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.auth.UserNotFound
import me.avo.realworld.kotlin.ktor.model.CredentialWrapper
import me.avo.realworld.kotlin.ktor.model.RegistrationWrapper
import me.avo.realworld.kotlin.ktor.model.UserWrapper
import me.avo.realworld.kotlin.ktor.repository.UserRepository
import me.avo.realworld.kotlin.ktor.service.AuthService
import me.avo.realworld.kotlin.ktor.util.user

fun Route.user(userRepository: UserRepository) {
    val authService = AuthService(userRepository)

    route("users") {
        post("login") {
            val credentials = call.receive<CredentialWrapper>().user
            val user = authService.login(credentials)
            call.respond(UserWrapper(user))
        }

        post {
            val details = call.receive<RegistrationWrapper>().user
            val user = authService.register(details)
            call.respond(UserWrapper(user))
        }
    }

    authenticate {
        route("user") {
            get {
                val (_, email, _, token) = call.user!!
                val user = userRepository.findUser(email)?.copy(token = token) ?: throw UserNotFound
                call.respond(UserWrapper(user))
            }

            put {
                val current = call.user!!
                val new = call.receive<UserWrapper>().user
                val updated = authService.updateUser(new, current)
                call.respond(UserWrapper(updated))
            }
        }
    }
}