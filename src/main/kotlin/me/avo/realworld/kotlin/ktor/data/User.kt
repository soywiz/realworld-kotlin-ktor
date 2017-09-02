package me.avo.realworld.kotlin.ktor.data

import org.jetbrains.ktor.util.AttributeKey

data class User(
        val email: String,
        val token: String,
        val username: String,
        val bio: String,
        val image: String?
) {

    companion object {

        val key = AttributeKey<String>("user")

    }

}