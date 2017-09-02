package me.avo.realworld.kotlin.ktor.data

import java.util.*

data class Comment(
        val id: Int,
        val createdAt: Date,
        val updatedAt: Date,
        val body: String
// TODO Author
)