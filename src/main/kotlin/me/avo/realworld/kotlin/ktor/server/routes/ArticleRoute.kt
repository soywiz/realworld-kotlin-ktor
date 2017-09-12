package me.avo.realworld.kotlin.ktor.server.routes

import me.avo.realworld.kotlin.ktor.data.Article
import me.avo.realworld.kotlin.ktor.data.ArticleDetails
import me.avo.realworld.kotlin.ktor.data.ArticleQuery
import me.avo.realworld.kotlin.ktor.persistence.ArticleSource
import me.avo.realworld.kotlin.ktor.persistence.ArticleSourceImpl
import me.avo.realworld.kotlin.ktor.server.optionalLogin
import me.avo.realworld.kotlin.ktor.server.requireLogin
import org.jetbrains.ktor.request.receive
import org.jetbrains.ktor.response.respond
import org.jetbrains.ktor.routing.*

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