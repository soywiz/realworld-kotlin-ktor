package me.avo.realworld.kotlin.ktor.util

import com.github.salomonbrys.kotson.*
import io.ktor.application.*
import io.ktor.auth.*
import me.avo.realworld.kotlin.ktor.data.*

val ApplicationCall.user: User? get() = authentication.principal()

infix fun <T : Any?> T.or(other: T) = this ?: other

fun Throwable.toJson() = jsonObject("errors" to jsonObject(
        "body" to jsonArray(message)
))