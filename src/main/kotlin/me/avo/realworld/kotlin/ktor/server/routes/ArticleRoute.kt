package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.data.Article
import me.avo.realworld.kotlin.ktor.data.ArticleQuery
import me.avo.realworld.kotlin.ktor.persistence.ArticleSource
import me.avo.realworld.kotlin.ktor.persistence.ArticleSourceImpl
import me.avo.realworld.kotlin.ktor.server.optionalLogin
import me.avo.realworld.kotlin.ktor.server.requireLogin

fun Route.article() = route("articles") {
    val articleSource: ArticleSource = ArticleSourceImpl()

    get {
        val user = optionalLogin()
        val query = ArticleQuery(call.parameters)
        val articles = articleSource.getArticles(query)
        call.respond(articles)
    }

    authenticate(null) {
        get("feed") {
            val user = requireLogin()
            TODO("Feed Articles")
        }


        post {
            val user = requireLogin()
            val details = call.receive<Article>()
            val article = articleSource.insertArticle(user, details)
            call.respond(article)
        }
    }

    route("{slug}") {

        get {
            TODO("Get ArticleDetails")
        }

        put {
            requireLogin()
            TODO("Update ArticleDetails")
        }

        delete {
            requireLogin()
            TODO("Delete ArticleDetails")
        }

        route("comments") {
            post {
                requireLogin()
                TODO("Add Comments to an ArticleDetails")
            }

            get {
                optionalLogin()
                TODO("Get Comments from an ArticleDetails")
            }

            delete("{id}") {
                requireLogin()
                TODO("Delete Comment")
            }

        }

        route("favorite") {
            post {
                requireLogin()
                TODO("Favorite ArticleDetails")
            }

            delete {
                requireLogin()
                TODO("Unfavorite ArticleDetails")
            }
        }

    }

}