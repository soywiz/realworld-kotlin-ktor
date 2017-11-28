package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.persistence.*
import me.avo.realworld.kotlin.ktor.server.routes.*

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