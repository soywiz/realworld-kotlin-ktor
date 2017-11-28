package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import me.avo.realworld.kotlin.ktor.auth.*
import me.avo.realworld.kotlin.ktor.util.*

fun StatusPages.Configuration.setup() {

    exception<Throwable> {
        val status = when (it) {
            is AuthenticationException -> it.status
            is AuthorizationException -> it.status
            else -> HttpStatusCode.InternalServerError
        }
        println(it)
        it.stackTrace.forEach(::println)
        call.response.status(status)
        call.respond(it.toJson())
    }

}