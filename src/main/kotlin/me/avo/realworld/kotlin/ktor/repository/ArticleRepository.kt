package me.avo.realworld.kotlin.ktor.repository

import me.avo.realworld.kotlin.ktor.model.*

interface ArticleRepository {

    fun getArticles(query: ArticleQuery): List<ArticleDetails>

    fun getFeed(): List<ArticleDetails>

    fun getArticle(slug: String): ArticleDetails

    fun insertArticle(user: User, details11: Article)

    fun updateArticle(articleDetails: ArticleDetails): ArticleDetails?

    fun deleteArticle(articleId: Int)

    fun addComment(comment: Comment): Comment

    fun getComments(slug: String): List<Comment>

    fun deleteComment(slug: String, id: String)

    fun favorite(slug: String): ArticleDetails

    fun unfavorite(slug: String): ArticleDetails

}