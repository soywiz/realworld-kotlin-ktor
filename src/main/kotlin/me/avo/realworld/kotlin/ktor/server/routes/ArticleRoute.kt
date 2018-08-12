package me.avo.realworld.kotlin.ktor.server.routes

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import me.avo.realworld.kotlin.ktor.model.*
import me.avo.realworld.kotlin.ktor.repository.ArticleRepository
import me.avo.realworld.kotlin.ktor.repository.CommentRepositoryImpl
import me.avo.realworld.kotlin.ktor.util.user

fun Route.article(articleRepository: ArticleRepository) = route("articles") {

    authenticate(optional = true) {
        get {
            val query = ArticleQuery(call.parameters)
            val articles = articleRepository.getArticles(query)
            call.respond(MultipleArticles(articles))
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
        fun ApplicationCall.getSlug() = parameters["slug"]!!

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
            val commentRepository = CommentRepositoryImpl()
            authenticate {
                post {
                    val newComment = call.receive<NewComment>()
                    val slug = call.getSlug()
                    val comment = commentRepository.addComment(newComment, slug)
                    call.respond(comment)
                }

                delete("{id}") {
                    val id = call.parameters["id"]!!
                    commentRepository.deleteComment(call.getSlug(), id)
                    call.respondText("")
                }
            }

            authenticate(optional = true) {
                get {
                    val comments = commentRepository.getComments(call.getSlug(), call.user)
                    call.respond(CommentsWrapper(comments))
                }
            }
        }
    }
}
