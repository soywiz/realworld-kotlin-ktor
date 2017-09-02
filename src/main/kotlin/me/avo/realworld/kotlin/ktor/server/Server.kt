package me.avo.realworld.kotlin.ktor.server

import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.gson.GsonSupport
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.routing.Routing

fun startServer() = embeddedServer(Netty, 5000) {
    install(CallLogging)
    install(DefaultHeaders)
    install(GsonSupport)

    install(Routing) {
        setup()
    }


}.start(wait = true)

