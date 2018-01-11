package me.avo.realworld.kotlin.ktor.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.avo.realworld.kotlin.ktor.data.*
import me.avo.realworld.kotlin.ktor.persistence.*
import me.avo.realworld.kotlin.ktor.util.serialization.*

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

    install(Routing) {
        setup()
    }


}.start(wait = true)

