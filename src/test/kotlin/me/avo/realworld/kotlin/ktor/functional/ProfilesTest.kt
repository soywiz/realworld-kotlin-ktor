package me.avo.realworld.kotlin.ktor.functional

import com.github.salomonbrys.kotson.obj
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProfilesTest : FunctionalTest {

    override val rootUri = "profiles"

    @Test fun profile() = handleRequest(
        uri = "$rootUri/rick",
        tokenUser = ensureUserExists()
    ) {
        it shouldHaveOwnProperty "profile"
        it["profile"].obj.let {
            it shouldHaveOwnProperty "username"
            it shouldHaveOwnProperty "bio"
            it shouldHaveOwnProperty "image"
            it shouldHaveOwnProperty "following"
        }
    }

    @Test fun `Follow Profile`() {

    }

    @Test fun `Unfollow Profile`() {

    }

}