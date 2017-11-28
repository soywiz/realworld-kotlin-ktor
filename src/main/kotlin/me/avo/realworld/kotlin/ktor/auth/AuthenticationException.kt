package me.avo.realworld.kotlin.ktor.auth

import io.ktor.http.*

class AuthenticationException : Exception() {

    val status = HttpStatusCode.Unauthorized

}