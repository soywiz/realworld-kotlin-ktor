package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.repository.ProfileRepository
import me.avo.realworld.kotlin.ktor.server.optionalLogin
import me.avo.realworld.kotlin.ktor.server.requireLogin

fun Route.profile(profileRepository: ProfileRepository) = route("profiles") {

    route("{username}") {
        fun ApplicationCall.getUsername() = parameters["username"]!!

        get {
            val user = optionalLogin()
            val profile = profileRepository.getProfile(call.getUsername(), user?.id)
            call.respond(profile)
        }

        post("follow") {
            val user = requireLogin()
            val profile = profileRepository.follow(user.id, call.getUsername())
            call.respond(profile)
        }

        delete("follow") {
            val user = requireLogin()
            val profile = profileRepository.unfollow(user.id, call.getUsername())
            call.respond(profile)
        }

    }

}