package me.avo.realworld.kotlin.ktor.util

import me.avo.realworld.kotlin.ktor.data.User
import org.jetbrains.ktor.application.ApplicationCall

val ApplicationCall.user: User get() = attributes[User.key]