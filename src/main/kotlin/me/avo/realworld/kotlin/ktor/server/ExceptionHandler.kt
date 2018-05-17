package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import me.avo.realworld.kotlin.ktor.auth.AuthenticationException
import me.avo.realworld.kotlin.ktor.auth.AuthorizationException
import me.avo.realworld.kotlin.ktor.util.toJson

fun StatusPages.Configuration.setup() {

    exception<Throwable> {
        val status = when (it) {
            is AuthenticationException -> it.status
            is AuthorizationException -> it.status
            else -> HttpStatusCode.InternalServerError
        }
        if (status == HttpStatusCode.InternalServerError) {
            println(it)
            it.stackTrace.forEach(::println)
        }
        call.response.status(status)
        call.respond(it.toJson())
    }

}