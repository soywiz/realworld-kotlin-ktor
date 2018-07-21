package me.avo.realworld.kotlin.ktor.functional

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.*
import me.avo.realworld.kotlin.ktor.model.User
import me.avo.realworld.kotlin.ktor.server.module
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual

interface FunctionalTest {

    val rootUri: String

    fun setup() {}

    fun getToken(user: User): String = TODO()

    fun withServer(block: TestApplicationEngine.() -> Unit) {
        setup()
        withTestApplication({ module() }) { block() }
    }

    fun TestApplicationEngine.handleRawRequest(block: TestApplicationRequest.() -> Unit = {}) = handleRequest {
        addHeader("Content-Type", "application/json")
        addHeader("X-Requested-With", "XMLHttpRequest")
        uri = rootUri
        block(this)
    }


    fun TestApplicationEngine.handleTokenRequest(user: User, block: TestApplicationRequest.() -> Unit = {}) =
        handleRawRequest {
            addHeader("Authorization", "Token ${getToken(user)}")
            block(this)
        }

    fun TestApplicationCall.checkResponse(
        status: HttpStatusCode = HttpStatusCode.OK,
        handled: Boolean = true,
        block: TestApplicationResponse.() -> Unit = {}
    ) {
        requestHandled shouldBe handled
        response.status() shouldEqual status
        block(response)
    }

    fun TestApplicationCall.checkResponseUnauthorized() = checkResponse(HttpStatusCode.Unauthorized)
    fun TestApplicationCall.checkResponseForbidden() = checkResponse(HttpStatusCode.Forbidden)
    fun TestApplicationCall.checkResponseBadRequest() = checkResponse(HttpStatusCode.BadRequest)

    fun request(
        uri: String = rootUri, method: HttpMethod = HttpMethod.Get, body: String? = null
    ) = fun TestApplicationRequest.() {
        this.uri = uri
        this.method = method
        body?.let { setBody(it) }
    }

}