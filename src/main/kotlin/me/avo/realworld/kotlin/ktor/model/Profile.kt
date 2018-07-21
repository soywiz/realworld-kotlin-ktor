package me.avo.realworld.kotlin.ktor.model

data class Profile(
    val username: String,
    val bio: String,
    val image: String?,
    val following: Boolean
)

class ProfileWrapper(val profile: Profile)