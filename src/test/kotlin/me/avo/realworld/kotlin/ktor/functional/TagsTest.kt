package me.avo.realworld.kotlin.ktor.functional

import com.github.salomonbrys.kotson.nullArray
import org.amshove.kluent.shouldNotBeNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TagsTest: FunctionalTest {

    override val rootUri = "tags"

    @Test fun `All Tags`() = handleRequest {
        it shouldHaveOwnProperty "tags"
        it["tags"].nullArray.shouldNotBeNull()
    }

}