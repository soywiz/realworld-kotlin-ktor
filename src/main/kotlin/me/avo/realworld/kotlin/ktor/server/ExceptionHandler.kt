package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import me.avo.realworld.kotlin.ktor.auth.AuthenticationException
import me.avo.realworld.kotlin.ktor.auth.AuthorizationException
import me.avo.realworld.kotlin.ktor.util.toJson

fun StatusPages.Configuration.setup() {
    exception<Throwable> { internal ->
        val status = when (internal) {
            is AuthenticationException -> internal.status
            is AuthorizationException -> internal.status
            else -> HttpStatusCode.InternalServerError
        }

        when {
            status.value.toString().startsWith("5") -> {
                call.respond(status, InternalServerError.toJson())
                serverLogger.error(internal.message, internal)
            }
            else -> {
                call.respond(status, internal.toJson())
                serverLogger.warn(internal.message)
            }
        }
    }
}

val InternalServerError =
    StatusException("Sorry, we encountered an error and are working on it.", HttpStatusCode.InternalServerError)

open class StatusException(
    message: String?,
    open val status: HttpStatusCode = HttpStatusCode.InternalServerError,
    cause: Throwable? = null
) : Exception(message, cause)
