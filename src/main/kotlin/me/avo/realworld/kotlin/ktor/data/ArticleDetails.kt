package me.avo.realworld.kotlin.ktor.data

import org.joda.time.DateTime

data class ArticleDetails(
        val title: String,
        val description: String,
        val body: String,
        val tagList: List<String>,
        val slug: String = title.split(" ").joinToString("-") { it.toLowerCase() },
        val createdAt: DateTime = DateTime.now(),
        val updatedAt: DateTime = createdAt
)