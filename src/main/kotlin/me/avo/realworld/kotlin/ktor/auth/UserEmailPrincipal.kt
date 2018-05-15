package me.avo.realworld.kotlin.ktor.auth

import io.ktor.auth.Principal

data class UserEmailPrincipal(
    val email: String
) : Principal