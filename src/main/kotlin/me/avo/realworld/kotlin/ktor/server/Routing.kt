package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.response.*
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.auth.*
import me.avo.realworld.kotlin.ktor.persistence.*
import me.avo.realworld.kotlin.ktor.server.routes.*

fun Routing.setup() = route("api") {
    val userSource: UserSource = UserSourceImpl()

    authentication {
        jwtAuthentication(JwtConfig.verifier, JwtConfig.realm) {
            val email = it.payload.getClaim("email").toString()
            userSource.findUser(email)?.let { user ->
                val token = JwtConfig.makeToken(user)
                user.copy(token = token)
            }
        }
    }

    article()
    profile()
    user(userSource)

    route("tags") {
        val tagSource: TagSource = TagSourceImpl()
        get {
            val tags = tagSource.getAllTags()
            call.respond(tags)
        }
    }
}