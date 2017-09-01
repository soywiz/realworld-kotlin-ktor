package me.avo.realworld.kotlin.ktor.data

data class User(
        val email: String,
        val toke: String,
        val username: String,
        val bio: String,
        val image: String?
)