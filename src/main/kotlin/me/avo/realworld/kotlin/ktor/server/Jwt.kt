package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.*
import io.ktor.pipeline.*
import me.avo.realworld.kotlin.ktor.auth.*
import me.avo.realworld.kotlin.ktor.data.*
import me.avo.realworld.kotlin.ktor.util.*

fun PipelineContext<*, ApplicationCall>.requireLogin(): User = optionalLogin() ?: throw AuthenticationException.USER_NOT_FOUND
fun PipelineContext<*, ApplicationCall>.optionalLogin(): User? = call.user