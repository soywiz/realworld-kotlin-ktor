package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.pipeline.PipelineContext
import io.ktor.routing.Route
import me.avo.realworld.kotlin.ktor.auth.AuthenticationException
import me.avo.realworld.kotlin.ktor.model.User
import me.avo.realworld.kotlin.ktor.util.user

fun PipelineContext<*, ApplicationCall>.requireLogin(): User =
    optionalLogin() ?: throw AuthenticationException.USER_NOT_FOUND

fun PipelineContext<*, ApplicationCall>.optionalLogin(): User? = call.user

fun Route.jwtAuth(build: Route.() -> Unit) = authenticate(null, build = build)