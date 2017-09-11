package me.avo.realworld.kotlin.ktor.server

import me.avo.realworld.kotlin.ktor.auth.LoginHandler
import me.avo.realworld.kotlin.ktor.data.*
import me.avo.realworld.kotlin.ktor.persistence.*
import org.jetbrains.ktor.application.ApplicationCall
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

    route("articles") {
        val articleSource: ArticleSource = ArticleSourceImpl()

        get {
            val user = optionalLogin()
            val query = ArticleQuery.fromParameter(call.parameters)
            val articles = articleSource.getArticles(query)
            call.respond(articles)
        }

        get("feed") {
            val user = requireLogin()
            TODO("Feed Articles")
        }


        post {
            val user = requireLogin()
            val details = call.receive<Article>().let {
                ArticleDetails(it.title, it.description, it.body, it.tagList)
            }
            val article = articleSource.insertArticle(user, details)
            call.respond(article)
        }

        route("{slug}") {

            get {
                TODO("Get Article")
            }

            put {
                requireLogin()
                TODO("Update Article")
            }

            delete {
                requireLogin()
                TODO("Delete Article")
            }

            route("comments") {
                post {
                    requireLogin()
                    TODO("Add Comments to an Article")
                }

                get {
                    optionalLogin()
                    TODO("Get Comments from an Article")
                }

                delete("{id}") {
                    requireLogin()
                    TODO("Delete Comment")
                }

            }

            route("favorite") {
                post {
                    requireLogin()
                    TODO("Favorite Article")
                }

                delete {
                    requireLogin()
                    TODO("Unfavorite Article")
                }
            }

        }

    }

    get("tags") {
        TODO("Get Tags")
    }
}