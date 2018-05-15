package me.avo.realworld.kotlin.ktor.auth

import io.ktor.http.HttpStatusCode

sealed class AuthenticationException : Exception() {

    abstract val status: HttpStatusCode

    object USER_NOT_FOUND : AuthenticationException() {
        override val status = HttpStatusCode.NotFound
    }


}