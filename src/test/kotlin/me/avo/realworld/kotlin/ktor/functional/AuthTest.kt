package me.avo.realworld.kotlin.ktor.functional

import io.ktor.http.HttpMethod
import org.amshove.kluent.shouldNotBeNullOrBlank
import org.junit.jupiter.api.Test

class AuthTest : FunctionalTest {

    override val rootUri = "user"

    @Test fun login() = withServer {
        handleRequest(
            request(
                uri = "${rootUri}s/login",
                method = HttpMethod.Post,
                body = "{\"user\":{\"email\":\"john@jacob.com\", \"password\":\"johnnyjacob\"}}"
            )
        ).checkResponse {
            content.shouldNotBeNullOrBlank()
        }
    }

    @Test fun `Login and Remember Token`() {

    }

    @Test fun register() {

    }

    @Test fun `Current User`() {

    }

    @Test fun `Update User`() {

    }

}