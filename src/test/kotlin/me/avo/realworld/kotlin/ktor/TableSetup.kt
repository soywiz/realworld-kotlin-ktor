package me.avo.realworld.kotlin.ktor

import me.avo.realworld.kotlin.ktor.auth.BcryptHasher
import me.avo.realworld.kotlin.ktor.auth.LoginHandler
import me.avo.realworld.kotlin.ktor.data.RegistrationDetails
import me.avo.realworld.kotlin.ktor.data.User
import me.avo.realworld.kotlin.ktor.persistence.ProfileSourceImpl
import me.avo.realworld.kotlin.ktor.persistence.Setup
import me.avo.realworld.kotlin.ktor.persistence.UserSourceImpl
import me.avo.realworld.kotlin.ktor.persistence.tables
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Disabled
class TableSetup {

    @Test
    fun setup() {
        setupEnvironment()
    }

}

fun setupEnvironment() {
    Setup()
    transaction {
        SchemaUtils.drop(*tables)
        SchemaUtils.create(*tables)
        availableUsers.forEach(RegistrationDetails::insert)
        followSource follow followTarget
    }
}

fun RegistrationDetails.insert() {
    val id = UserSourceImpl().insertUser(this)
    availableMap.put(this, id)
}

infix fun RegistrationDetails.follow(target: RegistrationDetails) {
    val sourceId = availableMap[this]!!
    ProfileSourceImpl().follow(sourceId, target.username)
}

fun RegistrationDetails.getId() = availableMap[this]!!

val testPassword = "test"
val hashedTestPassword = BcryptHasher.hashPassword(testPassword)

val details = RegistrationDetails("test", "some@other.com", hashedTestPassword)
val otherDetails = RegistrationDetails("test2", "another@other.com", hashedTestPassword)
val followSource = RegistrationDetails("follower", "follow@leader.the", hashedTestPassword)
val followTarget = RegistrationDetails("leader", "live@rare.com", hashedTestPassword)

val availableUsers = listOf(details, otherDetails, followSource, followTarget)
val availableMap = mutableMapOf<RegistrationDetails, Int>()
