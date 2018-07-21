package me.avo.realworld.kotlin.ktor.functional

import com.github.salomonbrys.kotson.obj
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.*
import me.avo.realworld.kotlin.ktor.auth.JwtConfig
import me.avo.realworld.kotlin.ktor.model.User
import me.avo.realworld.kotlin.ktor.repository.Setup
import me.avo.realworld.kotlin.ktor.repository.tables
import me.avo.realworld.kotlin.ktor.server.module
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBeNullOrBlank
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll

interface FunctionalTest {

    val rootUri: String

    @BeforeAll fun beforeAll() {
        Setup()
        transaction {
            SchemaUtils.drop(*tables)
            SchemaUtils.create(*tables)
        }
    }

    fun getToken(user: User): String = JwtConfig.makeToken(user)

    fun <T> withServer(block: TestApplicationEngine.() -> T): T = withTestApplication({ module() }) { block() }

    fun TestApplicationEngine.handleRawRequest(block: TestApplicationRequest.() -> Unit = {}) = handleRequest {
        addHeader("Content-Type", "application/json")
        addHeader("X-Requested-With", "XMLHttpRequest")
        uri = rootUri
        block(this)
    }

    fun TestApplicationEngine.handleRequest(
        uri: String = rootUri,
        method: HttpMethod = HttpMethod.Get,
        body: String? = null
    ) = handleRawRequest {
        this.uri = uri
        this.method = method
        body?.let { setBody(it) }
    }

    fun handleRequest(
        uri: String = rootUri,
        method: HttpMethod = HttpMethod.Get,
        body: String? = null,
        tokenUser: User? = null,
        testBlock: TestApplicationResponse.(JsonObject) -> Unit
    ) = withServer {
        when {
            tokenUser != null -> handleTokenRequest(tokenUser) { handleRequest(uri, method, body) }
            else -> handleRequest(uri, method, body)
        }
    }.checkJsonResponse { testBlock(it) }

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

    fun TestApplicationCall.checkJsonResponse(
        status: HttpStatusCode = HttpStatusCode.OK,
        handled: Boolean = true,
        block: TestApplicationResponse.(JsonObject) -> Unit = {}
    ) = checkResponse { block(getJsonBody()) }

    fun TestApplicationCall.checkResponseUnauthorized() = checkResponse(HttpStatusCode.Unauthorized)
    fun TestApplicationCall.checkResponseForbidden() = checkResponse(HttpStatusCode.Forbidden)
    fun TestApplicationCall.checkResponseBadRequest() = checkResponse(HttpStatusCode.BadRequest)

}

fun TestApplicationResponse.getJsonBody() = content.shouldNotBeNullOrBlank().let(JsonParser()::parse).obj

infix fun JsonObject.shouldHaveOwnProperty(name: String) = has(name) shouldBe true
