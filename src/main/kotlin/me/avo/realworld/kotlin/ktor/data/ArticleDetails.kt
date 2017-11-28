package me.avo.realworld.kotlin.ktor.data

import org.joda.time.DateTime

data class ArticleDetails(
        val id: Int,
        val slug: String,
        val title: String,
        val description: String,
        val body: String,
        val tagList: List<String>,
        val createdAt: DateTime,
        val updatedAt: DateTime,
        val favorited: Boolean,
        val favoritesCount: Int,
        val author: Profile
) {
   fun toArticle() =  Article(title,description,body,tagList,slug,createdAt,updatedAt)
}