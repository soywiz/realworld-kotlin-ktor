package me.avo.realworld.kotlin.ktor.auth

import io.ktor.http.*

class AuthorizationException : Exception() {

    val status = HttpStatusCode.Forbidden

}