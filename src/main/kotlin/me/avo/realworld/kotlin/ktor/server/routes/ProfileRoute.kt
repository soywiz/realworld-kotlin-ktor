package me.avo.realworld.kotlin.ktor.server.routes

import me.avo.realworld.kotlin.ktor.persistence.ProfileSource
import me.avo.realworld.kotlin.ktor.persistence.ProfileSourceImpl
import me.avo.realworld.kotlin.ktor.server.optionalLogin
import me.avo.realworld.kotlin.ktor.server.requireLogin
import org.jetbrains.ktor.application.ApplicationCall
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.*

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