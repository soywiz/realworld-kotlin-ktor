package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.locations.Locations
import io.ktor.routing.Routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import me.avo.realworld.kotlin.ktor.auth.JwtConfig
import me.avo.realworld.kotlin.ktor.data.*
import me.avo.realworld.kotlin.ktor.persistence.Setup
import me.avo.realworld.kotlin.ktor.persistence.UserSource
import me.avo.realworld.kotlin.ktor.persistence.UserSourceImpl
import me.avo.realworld.kotlin.ktor.util.serialization.register

fun startServer() = embeddedServer(Netty, 5000) {
    Setup()
    install(CallLogging)
    install(DefaultHeaders)
    install(Locations)
    install(ContentNegotiation) {
        gson {
            serializeNulls()
            register<LoginCredentials>()
            register<RegistrationDetails>()
            register<User>()
            register<ArticleDetails>()
            register<Profile>()
        }
    }

    install(StatusPages) {
        setup()
    }

    val userSource: UserSource = UserSourceImpl()
    install(Routing) {
        setup(userSource)
    }

    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = JwtConfig.realm
            validate {
                val email = it.payload.getClaim("email").toString()
                userSource.findUser(email)?.let { user ->
                    val token = JwtConfig.makeToken(user)
                    user.copy(token = token)
                }
            }
        }
    }

}.start(wait = true)

