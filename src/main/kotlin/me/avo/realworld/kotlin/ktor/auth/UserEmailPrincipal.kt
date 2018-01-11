package me.avo.realworld.kotlin.ktor.auth

import io.ktor.auth.*

data class UserEmailPrincipal(
        val email: String
) : Principal