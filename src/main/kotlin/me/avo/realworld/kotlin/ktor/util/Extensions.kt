package me.avo.realworld.kotlin.ktor.util

import me.avo.realworld.kotlin.ktor.data.User
import org.jetbrains.ktor.application.ApplicationCall

val ApplicationCall.user: User? get() = attributes.getOrNull(User.key)

infix fun <T : Any?> T.or(other: T) = this ?: other
