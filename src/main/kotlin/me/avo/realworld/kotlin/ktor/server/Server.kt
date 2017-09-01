package me.avo.realworld.kotlin.ktor.server

import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.routing.Routing

fun main(args: Array<String>) {

    embeddedServer(Netty, 5000) {
        install(CallLogging)
        install(DefaultHeaders)


        install(Routing) {
            setup()
        }


    }.start(wait = true)

}

