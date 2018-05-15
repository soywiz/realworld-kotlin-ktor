package me.avo.realworld.kotlin.ktor.model

import java.util.*

data class Comment(
    val id: Int,
    val createdAt: Date,
    val updatedAt: Date,
    val body: String,
    val author: Profile
)