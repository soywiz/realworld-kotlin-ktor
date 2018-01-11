package me.avo.realworld.kotlin.ktor.data

import io.ktor.auth.*
import io.ktor.util.*

data class User(
        val id: Int,
        val email: String,
        val password: String,
        val token: String,
        val username: String,
        val bio: String,
        val image: String?
): Principal {

    fun getProfile() = Profile(username, bio, image, false)

    companion object {

        val key = AttributeKey<User>("user")
    }

}