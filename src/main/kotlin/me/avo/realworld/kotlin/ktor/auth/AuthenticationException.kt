package me.avo.realworld.kotlin.ktor.auth

import org.jetbrains.ktor.http.HttpStatusCode

class AuthenticationException : Exception() {

    val status = HttpStatusCode.Unauthorized

}