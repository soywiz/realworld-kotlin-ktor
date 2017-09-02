package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.Article
import me.avo.realworld.kotlin.ktor.data.Comment

interface ArticleSource {

    fun getArticles(): List<Article>

    fun getFeed(): List<Article>

    fun getArticle(slug: String): Article

    fun insertArticle(): Article

    fun updateArticle(): Article

    fun deleteArticle()

    fun addComment(comment: Comment): Comment

    fun getComments(slug: String): List<Comment>

    fun deleteComment(slug: String, id: String)

    fun favorite(slug: String): Article

    fun unfavorite(slug: String): Article

    fun getTags(): List<String>

}