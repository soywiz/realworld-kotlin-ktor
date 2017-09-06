package me.avo.realworld.kotlin.ktor.data

import java.util.*

data class Article(
        val slug: String,
        val title: String,
        val description: String,
        val body: String,
        val tagList: List<String>,
        val createdAt: Date,
        val updatedAt: Date,
        val favorited: Boolean,
        val favoritesCount: Int,
        val author: Profile
)