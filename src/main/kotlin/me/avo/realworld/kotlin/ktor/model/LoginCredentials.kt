package me.avo.realworld.kotlin.ktor.model

data class LoginCredentials(
    val email: String,
    val password: String
)

class CredentialWrapper(val user: LoginCredentials)