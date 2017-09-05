package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.setupEnvironment
import org.junit.jupiter.api.BeforeAll

interface TestEnvironment {

    @BeforeAll
    fun beforeAll() = setupEnvironment()

}