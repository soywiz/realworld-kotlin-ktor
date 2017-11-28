package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.persistence.*
import me.avo.realworld.kotlin.ktor.server.*

fun Route.profile() = route("profiles") {
    val profileSource: ProfileSource = ProfileSourceImpl()

    route("{username}") {
        fun ApplicationCall.getUsername() = parameters["username"]!!

        get {
            val user = optionalLogin()
            val profile = profileSource.getProfile(call.getUsername(), user?.id)
            call.respond(profile)
        }

        post("follow") {
            val user = requireLogin()
            val profile = profileSource.follow(user.id, call.getUsername())
            call.respond(profile)
        }

        delete("follow") {
            val user = requireLogin()
            val profile = profileSource.unfollow(user.id, call.getUsername())
            call.respond(profile)
        }

    }

}