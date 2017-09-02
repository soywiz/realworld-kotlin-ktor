package me.avo.realworld.kotlin.ktor.util

import java.util.*

object Property {

    val props = Properties().apply {
        Property::class.java.classLoader.getResourceAsStream("application.properties").use {
            load(it)
        }
    }

    operator fun get(key: String) = props[key].toString()

}