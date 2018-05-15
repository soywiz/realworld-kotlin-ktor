package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.*
import me.avo.realworld.kotlin.ktor.repository.ProfileRepository
import me.avo.realworld.kotlin.ktor.repository.ProfileRepositoryImpl
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEqualTo
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ProfileRepositoryImplTest : TestEnvironment {

    private val ds: ProfileRepository =
        ProfileRepositoryImpl()

    @Test
    fun getProfile() = availableUsers.forEach {
        ds.getProfile(it.username, null).let { pro ->
            pro.username shouldBeEqualTo it.username
            pro.following shouldEqualTo false
        }
    }

    @Test
    fun follow() {
        val source = details
        val target = otherDetails

        ds.follow(source.getId(), target.username).apply {
            following shouldBe true
            username shouldBeEqualTo target.username
        }
    }

    @Test
    fun `following non existing user should fail`() {
        val source = details
        val target = "nonexisting"

        { ds.follow(source.getId(), target) } shouldThrow Exception::class
    }

    @Test
    fun unfollow() {
        val source = followSource
        val target = followTarget
        ds.getProfile(target.username, source.getId()).let {
            it.following shouldBe true
            it.username shouldBeEqualTo target.username
        }

        ds.unfollow(source.getId(), target.username).let {
            it.following shouldBe false
            it.username shouldBeEqualTo target.username
        }

    }

}