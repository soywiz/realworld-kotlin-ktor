package me.avo.realworld.kotlin.ktor.auth

import org.jetbrains.ktor.http.HttpStatusCode

class AuthorizationException : Exception() {

    val status = HttpStatusCode.Forbidden

}