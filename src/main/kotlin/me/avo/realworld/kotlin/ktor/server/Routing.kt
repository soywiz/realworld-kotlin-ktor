package me.avo.realworld.kotlin.ktor.server

import me.avo.realworld.kotlin.ktor.persistence.TagSource
import me.avo.realworld.kotlin.ktor.persistence.TagSourceImpl
import me.avo.realworld.kotlin.ktor.persistence.UserSource
import me.avo.realworld.kotlin.ktor.persistence.UserSourceImpl
import me.avo.realworld.kotlin.ktor.server.routes.article
import me.avo.realworld.kotlin.ktor.server.routes.profile
import me.avo.realworld.kotlin.ktor.server.routes.user
import org.jetbrains.ktor.application.ApplicationCallPipeline
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.Routing
import org.jetbrains.ktor.routing.get
import org.jetbrains.ktor.routing.route

fun Routing.setup() = route("api") {
    val userSource: UserSource = UserSourceImpl()

    intercept(ApplicationCallPipeline.Infrastructure) {
        jwtAuth(userSource)
    }

    article()
    profile()
    user(userSource)


    val tagSource: TagSource = TagSourceImpl()
    get("tags") {
        val tags = tagSource.getAllTags()
        call.respond(tags)
    }
}