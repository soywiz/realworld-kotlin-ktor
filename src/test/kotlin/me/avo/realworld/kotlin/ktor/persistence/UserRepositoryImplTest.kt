package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.auth.BcryptHasher
import me.avo.realworld.kotlin.ktor.availableUsers
import me.avo.realworld.kotlin.ktor.hashedTestPassword
import me.avo.realworld.kotlin.ktor.model.RegistrationDetails
import me.avo.realworld.kotlin.ktor.model.User
import me.avo.realworld.kotlin.ktor.repository.UserRepository
import me.avo.realworld.kotlin.ktor.repository.UserRepositoryImpl
import me.avo.realworld.kotlin.ktor.shouldNotBeNull
import me.avo.realworld.kotlin.ktor.testPassword
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEqual
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserRepositoryImplTest : TestEnvironment {

    private val ds: UserRepository = UserRepositoryImpl()

    @Test
    fun find() = availableUsers.forEach {
        val user = ds.findUser(it.email).shouldNotBeNull()
        user.username shouldBeEqualTo it.username
        BcryptHasher.checkPassword(testPassword, user)
    }

    @Test
    fun `should be null when user not found`() {
        val email = "doest@not.exist"
        ds.findUser(email) shouldEqual null
    }

    @Test
    fun insertTest() {
        RegistrationDetails("another", "i@me.you", hashedTestPassword).apply {
            ds.insertUser(this)
            val user = ds.findUser(email).shouldNotBeNull()
            user.username shouldBeEqualTo username
            BcryptHasher.checkPassword(testPassword, user)
        }
    }

    @Test
    fun update() {
        val details = RegistrationDetails("update", "up@date.me", hashedTestPassword)
        ds.insertUser(details)
        val current = ds.findUser(details.email).shouldNotBeNull()
        val changedPassword = "changed"
        val hashed = BcryptHasher.hashPassword(changedPassword)
        val new = User(0, "changed@date.me", hashed, "", "changed", "my life has changed", "changed!")

        ds.updateUser(new, current).shouldNotBeNull().compare(new, changedPassword)
        ds.findUser(new.email).shouldNotBeNull().compare(new, changedPassword)
    }

    private fun User.compare(other: User, rawPassword: String) {
        email shouldBeEqualTo other.email
        username shouldBeEqualTo other.username
        BcryptHasher.checkPassword(rawPassword, this)
        bio shouldBeEqualTo other.bio
        image shouldEqual other.image
    }

}