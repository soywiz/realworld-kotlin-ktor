package me.avo.realworld.kotlin.ktor.functional

import com.github.salomonbrys.kotson.bool
import com.github.salomonbrys.kotson.obj
import com.google.gson.JsonObject
import io.ktor.http.HttpMethod
import org.amshove.kluent.shouldEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfilesTest : FunctionalTest {

    override val rootUri = "profiles"

    @Test fun profile() = handleRequest(
        uri = "$rootUri/rick",
        tokenUser = ensureUserExists()
    ) { checkProfile(it) }

    @Test fun `Follow Profile`() = handleRequest(
        uri = "$rootUri/rick/follow",
        method = HttpMethod.Post,
        tokenUser = ensureUserExists(),
        body = "{\"user\":{\"email\":\"{{EMAIL}}\"}}"
    ) {
        val profile = checkProfile(it)
        profile["following"].bool shouldEqualTo true
    }

    @Test fun `Unfollow Profile`() = handleRequest(
        uri = "$rootUri/rick/follow",
        method = HttpMethod.Delete,
        tokenUser = ensureUserExists()
    ) {
        val profile = checkProfile(it)
        profile["following"].bool shouldEqualTo false
    }

    private fun checkProfile(json: JsonObject): JsonObject {
        json shouldHaveOwnProperty "profile"
        return json["profile"].obj.also {
            it shouldHaveOwnProperty "username"
            it shouldHaveOwnProperty "bio"
            it shouldHaveOwnProperty "image"
            it shouldHaveOwnProperty "following"
        }
    }

}