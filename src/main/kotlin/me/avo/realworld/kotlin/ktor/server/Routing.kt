package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import me.avo.realworld.kotlin.ktor.auth.JwtConfig
import me.avo.realworld.kotlin.ktor.persistence.TagSource
import me.avo.realworld.kotlin.ktor.persistence.TagSourceImpl
import me.avo.realworld.kotlin.ktor.persistence.UserSource
import me.avo.realworld.kotlin.ktor.persistence.UserSourceImpl
import me.avo.realworld.kotlin.ktor.server.routes.article
import me.avo.realworld.kotlin.ktor.server.routes.profile
import me.avo.realworld.kotlin.ktor.server.routes.user

fun Routing.setup(userSource: UserSource) = route("api") {

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