package me.avo.realworld.kotlin.ktor.repository

import me.avo.realworld.kotlin.ktor.model.Article
import me.avo.realworld.kotlin.ktor.model.ArticleDetails
import me.avo.realworld.kotlin.ktor.model.ArticleQuery
import me.avo.realworld.kotlin.ktor.model.User

interface ArticleRepository {

    fun getArticles(query: ArticleQuery): List<ArticleDetails>

    fun getFeed(): List<ArticleDetails>

    fun getArticle(slug: String): ArticleDetails

    fun insertArticle(user: User, article: Article)

    fun updateArticle(articleDetails: ArticleDetails): ArticleDetails?

    fun deleteArticle(articleId: Int)

    fun favorite(slug: String): ArticleDetails

    fun unfavorite(slug: String): ArticleDetails

}
