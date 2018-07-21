package me.avo.realworld.kotlin.ktor.functional

import com.github.salomonbrys.kotson.obj
import com.google.gson.JsonObject
import io.ktor.http.HttpMethod
import me.avo.realworld.kotlin.ktor.model.RegistrationDetails
import me.avo.realworld.kotlin.ktor.model.User
import me.avo.realworld.kotlin.ktor.repository.UserRepositoryImpl
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthTest : FunctionalTest {

    override val rootUri = "user"
    private val usersUri = "${rootUri}s"
    private val userDetails = RegistrationDetails("johnjacob", "john@jacob.com", "johnnyjacob")

    @Test fun login() = handleRequest(
        uri = "$usersUri/login",
        method = HttpMethod.Post,
        body = "{\"user\":{\"email\":\"john@jacob.com\", \"password\":\"johnnyjacob\"}}"
    ) { checkUserResponse(it) }


    @Disabled("Same as login, token can be generated instead of remembered")
    @Test fun `Login and Remember Token`() = Unit


    @Test fun register() = handleRequest(
        uri = usersUri,
        method = HttpMethod.Post,
        body = "{\"user\":{\"email\":\"john@jacob.com\", \"password\":\"johnnyjacob\", \"username\":\"johnjacob\"}}"
    ) { checkUserResponse(it) }


    @Test fun `Current User`() = handleRequest(
        uri = rootUri,
        method = HttpMethod.Get,
        tokenUser = ensureUserExists()
    ) { checkUserResponse(it) }


    @Test fun `Update User`() = handleRequest(
        uri = rootUri,
        method = HttpMethod.Post,
        body = "{\"user\":{\"email\":\"john@jacob.com\"}}",
        tokenUser = ensureUserExists()
    ) { checkUserResponse(it) }


    private fun ensureUserExists(): User = userRepository.findUser(userDetails.email) ?: {
        val userId = userRepository.insertUser(userDetails)
        userRepository.findUser(userRepository.byId(userId))!!
    }()

    private fun ensureUserDoesNotExist() {

    }

    private fun checkUserResponse(jsonObject: JsonObject) {
        jsonObject shouldHaveOwnProperty "user"
        val user = jsonObject["user"].obj
        user shouldHaveOwnProperty "email"
        user shouldHaveOwnProperty "username"
        user shouldHaveOwnProperty "bio"
        user shouldHaveOwnProperty "image"
        user shouldHaveOwnProperty "token"
    }

    private val userRepository = UserRepositoryImpl()

}
