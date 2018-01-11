package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.*
import me.avo.realworld.kotlin.ktor.auth.*
import me.avo.realworld.kotlin.ktor.data.*
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserSourceImplTest : TestEnvironment {

    private val ds: UserSource = UserSourceImpl()

    @Test
    fun find() = availableUsers.forEach {
        val user = ds.findUser(it.email).shouldNotBeNull()
        user.username shouldEqualTo it.username
        BcryptHasher.checkPassword(testPassword, user)
    }

    @Test
    fun `should fail when user not found`() {
        val email = "doest@not.exist"
        { ds.findUser(email) } shouldThrow Exception::class
    }

    @Test
    fun insertTest() {
        RegistrationDetails("another", "i@me.you", hashedTestPassword).apply {
            ds.insertUser(this)
            val user = ds.findUser(email).shouldNotBeNull()
            user.username shouldEqualTo username
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
        email shouldEqualTo other.email
        username shouldEqualTo other.username
        BcryptHasher.checkPassword(rawPassword, this)
        bio shouldEqualTo other.bio
        image shouldEqual other.image
    }

}