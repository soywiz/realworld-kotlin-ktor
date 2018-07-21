package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.response.respond
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.model.ProfileWrapper
import me.avo.realworld.kotlin.ktor.repository.ProfileRepository
import me.avo.realworld.kotlin.ktor.util.user

fun Route.profile(profileRepository: ProfileRepository) = route("profiles") {

    route("{username}") {
        fun ApplicationCall.getUsername() = parameters["username"]!!

        authenticate(optional = true) {
            get {
                val user = call.user
                val profile = profileRepository.getProfile(call.getUsername(), user?.id)
                call.respond(ProfileWrapper(profile))
            }
        }

        authenticate {
            post("follow") {
                val user = call.user
                val profile = profileRepository.follow(user!!.id, call.getUsername())
                call.respond(profile)
            }

            delete("follow") {
                val user = call.user
                val profile = profileRepository.unfollow(user!!.id, call.getUsername())
                call.respond(profile)
            }
        }
    }

}