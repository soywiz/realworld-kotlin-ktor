package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.route
import me.avo.realworld.kotlin.ktor.repository.*
import me.avo.realworld.kotlin.ktor.server.routes.article
import me.avo.realworld.kotlin.ktor.server.routes.profile
import me.avo.realworld.kotlin.ktor.server.routes.user

fun Routing.setup(
    userRepository: UserRepository,
    articleRepository: ArticleRepository,
    profileRepository: ProfileRepository
) =
    route("api") {

        article(articleRepository)
        profile(profileRepository)
        user(userRepository)

        route("tags") {
            val tagSource: TagSource = TagSourceImpl()
            authenticate("optional") {
                get {
                    val user = optionalLogin()
                    println(user)
                    val tags = tagSource.getAllTags()
                    call.respond(tags)
                }
            }
        }
    }