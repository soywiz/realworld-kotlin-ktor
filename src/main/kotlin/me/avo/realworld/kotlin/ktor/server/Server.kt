package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.jackson.jackson
import io.ktor.locations.Locations
import io.ktor.routing.Routing
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import me.avo.realworld.kotlin.ktor.auth.JwtConfig
import me.avo.realworld.kotlin.ktor.repository.*
import org.slf4j.event.Level

fun startServer() = embeddedServer(CIO, 5000) { module() }.start(wait = true)

fun Application.module() {
    Setup()
    install(CallLogging) {
        level = Level.INFO
    }
    install(DefaultHeaders)
    install(Locations)
    install(ContentNegotiation) {
        jackson { }
    }
    install(StatusPages) {
        setup()
    }

    val articleRepository: ArticleRepository = ArticleRepositoryImpl()
    val profileRepository: ProfileRepository = ProfileRepositoryImpl()
    val userRepository: UserRepository = UserRepositoryImpl()

    install(Authentication) {
        jwt {
            authSchemes("Token")
            verifier(JwtConfig.verifier)
            realm = JwtConfig.realm
            validate {
                it.payload.claims.forEach(::println)
                val email = it.payload.getClaim("email")?.asString() ?: return@validate null
                println("Required: $email")
                userRepository.findUser(email)?.let { user ->
                    val token = JwtConfig.makeToken(user)
                    user.copy(token = token)
                }
            }
        }
    }

    install(Routing) {
        setup(userRepository, articleRepository, profileRepository)
    }
}