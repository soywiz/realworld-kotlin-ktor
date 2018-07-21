package me.avo.realworld.kotlin.ktor.model

import io.ktor.auth.Principal

data class User(
    val id: Int,
    val email: String,
    val password: String,
    val token: String,
    val username: String,
    val bio: String,
    val image: String?
) : Principal {

    val profile get() = Profile(username, bio, image, false)

}

class UserWrapper(val user: User)