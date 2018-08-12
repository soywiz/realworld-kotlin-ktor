package me.avo.realworld.kotlin.ktor.functional

import com.github.salomonbrys.kotson.obj
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.*
import me.avo.realworld.kotlin.ktor.auth.JwtConfig
import me.avo.realworld.kotlin.ktor.model.RegistrationDetails
import me.avo.realworld.kotlin.ktor.model.User
import me.avo.realworld.kotlin.ktor.repository.Setup
import me.avo.realworld.kotlin.ktor.repository.UserRepositoryImpl
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
        body: String? = null,
        block: TestApplicationRequest.() -> Unit = {}
    ) = handleRawRequest {
        this.uri = uri
        this.method = method
        body?.let { setBody(it) }
        block()
    }

    fun handleRequest(
        uri: String = rootUri,
        method: HttpMethod = HttpMethod.Get,
        body: String? = null,
        tokenUser: User? = null,
        testBlock: TestApplicationResponse.(JsonObject) -> Unit
    ) = withServer {
        handleRequest(uri, method, body) {
            if (tokenUser != null) {
                addHeader("Authorization", "Token ${getToken(tokenUser)}")
            }
        }
    }.checkJsonResponse { testBlock(it) }

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

    val userDetails get() = RegistrationDetails("johnjacob", "john@jacob.com", "johnnyjacob")
    val userRepository get() = UserRepositoryImpl()

    fun ensureUserExists(details: RegistrationDetails = userDetails): User =
        userRepository.findUser(details.email) ?: {
            val userId = userRepository.insertUser(details)
            userRepository.findUser(userRepository.byId(userId))!!
        }()

}

fun TestApplicationResponse.getJsonBody() = content.shouldNotBeNullOrBlank().let(JsonParser()::parse).obj

infix fun JsonObject.shouldHaveOwnProperty(name: String): JsonElement {
    has(name) shouldBe true
    return this[name]
}

infix fun <T> JsonElement.and(block: (JsonElement) -> T): T {
    return block(this)
}