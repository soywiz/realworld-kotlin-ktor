package me.avo.realworld.kotlin.ktor.server

import me.avo.realworld.kotlin.ktor.data.Article
import me.avo.realworld.kotlin.ktor.data.LoginCredentials
import me.avo.realworld.kotlin.ktor.data.RegistrationDetails
import me.avo.realworld.kotlin.ktor.data.User
import me.avo.realworld.kotlin.ktor.persistence.Setup
import me.avo.realworld.kotlin.ktor.util.serialization.DeserialStrategy
import me.avo.realworld.kotlin.ktor.util.serialization.SerialStrategy
import me.avo.realworld.kotlin.ktor.util.serialization.register
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.features.StatusPages
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.http.HttpStatusCode
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.response.respondText
import org.jetbrains.ktor.routing.Routing

fun startServer() = embeddedServer(Netty, 5000) {
    Setup()
    install(CallLogging)
    install(DefaultHeaders)
    install(GsonSupport) {
        register<LoginCredentials>()
        register<RegistrationDetails>()
        register<User>()
        register<Article>()
    }

    install(StatusPages) {
        exception<Throwable> {
            println(it)
            it.stackTrace.forEach { println(it) }
            call.response.status(HttpStatusCode.InternalServerError)
            call.respondText(it.toString())
        }
    }

    install(Routing) {
        setup()
    }


}.start(wait = true)

