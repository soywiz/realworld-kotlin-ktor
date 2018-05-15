package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.model.Article
import me.avo.realworld.kotlin.ktor.model.ArticleQuery
import me.avo.realworld.kotlin.ktor.repository.ArticleRepository
import me.avo.realworld.kotlin.ktor.server.jwtAuth
import me.avo.realworld.kotlin.ktor.server.optionalLogin
import me.avo.realworld.kotlin.ktor.server.requireLogin

fun Route.article(articleRepository: ArticleRepository) = route("articles") {

    get {
        val user = optionalLogin()
        val query = ArticleQuery(call.parameters)
        val articles = articleRepository.getArticles(query)
        call.respond(articles)
    }

    jwtAuth {
        get("feed") {
            val user = requireLogin()
            TODO("Feed Articles")
        }


        post {
            val user = requireLogin()
            val details = call.receive<Article>()
            val article = articleRepository.insertArticle(user, details)
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