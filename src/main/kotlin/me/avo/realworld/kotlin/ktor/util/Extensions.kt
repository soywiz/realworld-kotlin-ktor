package me.avo.realworld.kotlin.ktor.util

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import me.avo.realworld.kotlin.ktor.data.User
import org.jetbrains.ktor.application.ApplicationCall

val ApplicationCall.user: User? get() = attributes.getOrNull(User.key)

infix fun <T : Any?> T.or(other: T) = this ?: other

fun Throwable.toJson() = jsonObject("errors" to jsonObject(
        "body" to jsonArray(message)
))