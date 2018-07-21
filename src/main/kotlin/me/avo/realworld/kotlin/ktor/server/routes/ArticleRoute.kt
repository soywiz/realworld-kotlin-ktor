package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.model.Article
import me.avo.realworld.kotlin.ktor.model.ArticleQuery
import me.avo.realworld.kotlin.ktor.repository.ArticleRepository
import me.avo.realworld.kotlin.ktor.util.user

fun Route.article(articleRepository: ArticleRepository) = route("articles") {

    authenticate(optional = true) {
        get {
            val query = ArticleQuery(call.parameters)
            val articles = articleRepository.getArticles(query)
            call.respond(articles)
        }
    }

    authenticate {
        get("feed") {
            val user = call.user
            TODO("Feed Articles")
        }


        post {
            val user = call.user
            val details = call.receive<Article>()
            val article = articleRepository.insertArticle(user!!, details)
            call.respond(article)
        }
    }

    route("{slug}") {

        get {
            TODO("Get ArticleDetails")
        }

        authenticate {
            put {
                TODO("Update ArticleDetails")
            }

            delete {
                TODO("Delete ArticleDetails")
            }

            route("favorite") {
                post {
                    TODO("Favorite ArticleDetails")
                }

                delete {
                    TODO("Unfavorite ArticleDetails")
                }
            }
        }

        route("comments") {
            authenticate {
                post {
                    TODO("Add Comments to an ArticleDetails")
                }

                delete("{id}") {
                    TODO("Delete Comment")
                }
            }

            authenticate(optional = true) {
                get {
                    TODO("Get Comments from an ArticleDetails")
                }
            }
        }
    }
}