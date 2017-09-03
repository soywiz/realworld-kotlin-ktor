package me.avo.realworld.kotlin.ktor.data

import org.jetbrains.ktor.util.AttributeKey

data class User(
        val id: Int,
        val email: String,
        val password: String,
        val token: String,
        val username: String,
        val bio: String,
        val image: String?
) {

    companion object {

        val key = AttributeKey<User>("user")

    }

}