package me.avo.realworld.kotlin.ktor

fun <T : Any> T?.shouldNotBeNull(): T = this ?: throw AssertionError("Expected this to not be null")