package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.auth.BcryptHasher
import me.avo.realworld.kotlin.ktor.availableUsers
import me.avo.realworld.kotlin.ktor.data.RegistrationDetails
import me.avo.realworld.kotlin.ktor.data.User
import me.avo.realworld.kotlin.ktor.hashedTestPassword
import me.avo.realworld.kotlin.ktor.testPassword
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserSourceImplTest : TestEnvironment {

    val ds: UserSource = UserSourceImpl()

    @Test
    fun find() = availableUsers.forEach {
        val user = ds.findUser(it.email)
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
            val user = ds.findUser(email)
            user.username shouldEqualTo username
            BcryptHasher.checkPassword(testPassword, user)
        }
    }

    @Test
    fun update() {
        val details = RegistrationDetails("update", "up@date.me", hashedTestPassword)
        ds.insertUser(details)
        val current = ds.findUser(details.email)
        val changedPassword = "changed"
        val hashed = BcryptHasher.hashPassword(changedPassword)
        val new = User(0, "changed@date.me", hashed, "", "changed", "my life has changed", "changed!")

        ds.updateUser(new, current).compare(new, changedPassword)
        ds.findUser(new.email).compare(new, changedPassword)
    }

    fun User.compare(other: User, rawPassword: String) {
        email shouldEqualTo other.email
        username shouldEqualTo other.username
        BcryptHasher.checkPassword(rawPassword, this)
        bio shouldEqualTo other.bio
        image shouldEqual other.image
    }


}