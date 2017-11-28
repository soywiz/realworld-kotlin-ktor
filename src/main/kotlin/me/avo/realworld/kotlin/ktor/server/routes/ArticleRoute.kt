package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.data.*
import me.avo.realworld.kotlin.ktor.persistence.*
import me.avo.realworld.kotlin.ktor.server.*

fun Route.article() = route("articles") {
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
        val details = call.receive<Article>()
        val article = articleSource.insertArticle(user, details)
        call.respond(article)
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