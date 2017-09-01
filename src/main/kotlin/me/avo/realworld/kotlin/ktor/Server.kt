package me.avo.realworld.kotlin.ktor

import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.CallLogging
import org.jetbrains.ktor.host.embeddedServer
import org.jetbrains.ktor.netty.Netty
import org.jetbrains.ktor.routing.Routing

fun main(args: Array<String>) {


    embeddedServer(Netty, 5000) {
        install(CallLogging)


        install(Routing) {

        }


    }.start(wait = true)

}

