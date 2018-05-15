package me.avo.realworld.kotlin.ktor.util

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication
import me.avo.realworld.kotlin.ktor.model.User

val ApplicationCall.user: User? get() = authentication.principal()

//infix fun <T : Any?> T.or(other: T) = this ?: other

fun Throwable.toJson() = jsonObject(
    "errors" to jsonObject(
        "body" to jsonArray(message)
    )
)