package me.avo.realworld.kotlin.ktor.data

data class ArticleDetails(
        val title: String,
        val description: String,
        val body: String,
        val tagList: List<String>
)