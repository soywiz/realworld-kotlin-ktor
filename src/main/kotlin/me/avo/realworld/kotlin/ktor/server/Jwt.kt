package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.*
import io.ktor.pipeline.*
import io.ktor.request.*
import me.avo.realworld.kotlin.ktor.auth.*
import me.avo.realworld.kotlin.ktor.data.*
import me.avo.realworld.kotlin.ktor.persistence.*
import me.avo.realworld.kotlin.ktor.util.*

fun PipelineContext<Unit, ApplicationCall>.jwtAuth(userSource: UserSource) {
    val token = call.request.header("Authorization")?.removePrefix("Token ") ?: return
    val email = JwtConfig.parse(token)
    val user = userSource.findUser(email).copy(token = token)
    call.attributes.put(User.key, user)
}

fun PipelineContext<*, ApplicationCall>.requireLogin(): User = optionalLogin() ?: throw Exception("Not logged in")
fun PipelineContext<*, ApplicationCall>.optionalLogin(): User? = call.user