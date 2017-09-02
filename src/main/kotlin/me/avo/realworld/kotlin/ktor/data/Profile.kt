package me.avo.realworld.kotlin.ktor.data

data class Profile(
        val username: String,
        val bio: String,
        val image: String,
        val following: Boolean
)