package me.avo.realworld.kotlin.ktor.server

import me.avo.realworld.kotlin.ktor.auth.JwtConfig
import me.avo.realworld.kotlin.ktor.data.User
import me.avo.realworld.kotlin.ktor.persistence.UserSource
import me.avo.realworld.kotlin.ktor.util.user
import org.jetbrains.ktor.pipeline.PipelineContext
import org.jetbrains.ktor.request.header

fun PipelineContext<Unit>.jwtAuth(userSource: UserSource) {
    val token = call.request.header("Authorization")?.removePrefix("Token ") ?: return
    val email = JwtConfig.parse(token)
    val user = userSource.findUser(email).copy(token = token)
    call.attributes.put(User.key, user)
}

fun PipelineContext<Unit>.requireLogin(): User = optionalLogin() ?: throw Exception("Not logged in")
fun PipelineContext<Unit>.optionalLogin(): User? = call.user