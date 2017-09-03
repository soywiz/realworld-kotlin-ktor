package me.avo.realworld.kotlin.ktor.server

import me.avo.realworld.kotlin.ktor.auth.LoginHandler
import me.avo.realworld.kotlin.ktor.data.LoginCredentials
import me.avo.realworld.kotlin.ktor.data.RegistrationDetails
import me.avo.realworld.kotlin.ktor.data.User
import me.avo.realworld.kotlin.ktor.persistence.ProfileSource
import me.avo.realworld.kotlin.ktor.persistence.ProfileSourceImpl
import me.avo.realworld.kotlin.ktor.persistence.UserSource
import me.avo.realworld.kotlin.ktor.persistence.UserSourceImpl
import me.avo.realworld.kotlin.ktor.util.user
import org.jetbrains.ktor.application.ApplicationCallPipeline
import org.jetbrains.ktor.request.receive
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.*

fun Routing.setup() = route("api") {
    val userSource: UserSource = UserSourceImpl()

    intercept(ApplicationCallPipeline.Infrastructure) {
        jwtAuth(userSource)
    }

    route("users") {

        post("login") {
            val credentials = call.receive<LoginCredentials>()
            val user = LoginHandler().login(credentials)
            call.respond(user)
        }

        post {
            val details = call.receive<RegistrationDetails>()
            val user = LoginHandler().register(details)
            call.respond(user)
        }
    }


    route("user") {

        get {
            val (_, email, _, token) = requireLogin()
            val user = userSource.findUser(email).copy(token = token)
            call.respond(user)
        }

        put {
            val current = requireLogin()
            val new = call.receive<User>()
            val updated = LoginHandler().updateUser(new, current)
            call.respond(updated)
        }
    }

    route("profiles") {
        val profileSource: ProfileSource = ProfileSourceImpl()

        route("{username}") {

            get {
                val user = optionalLogin()
                val username = call.parameters["username"]!!
                val profile = profileSource.getProfile(username, user?.id)
                call.respond(profile)
            }

            post("follow") {
                TODO("Follow User")
            }

            delete("follow") {
                TODO("Unfollow User")
            }

        }

    }

    route("articles") {

        get {
            TODO("Get articles")
        }

        get("feed") {
            TODO("Feed Articles")
        }


        post {
            TODO("Create Article")
        }

        route("{slug}") {

            get {
                TODO("Get Article")
            }

            put {
                TODO("Update Article")
            }

            delete {
                TODO("Delete Article")
            }

            route("comments") {
                post {
                    TODO("Add Comments to an Article")
                }

                get {
                    TODO("Get Comments from an Article")
                }

                delete("{id}") {
                    TODO("Delete Comment")
                }

            }

            route("favorite") {
                post {
                    TODO("Favorite Article")
                }

                delete {
                    TODO("Unfavorite Article")
                }
            }

        }


    }

    get("tags") {
        TODO("Get Tags")
    }
}