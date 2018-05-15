package me.avo.realworld.kotlin.ktor.model

import io.ktor.auth.Principal
import io.ktor.util.AttributeKey

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val token: String,
    val username: String,
    val bio: String,
    val image: String?
) : Principal {

    fun getProfile() = Profile(username, bio, image, false)

    companion object {

        val key = AttributeKey<User>("user")
    }

}