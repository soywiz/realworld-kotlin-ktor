package me.avo.realworld.kotlin.ktor

import me.avo.realworld.kotlin.ktor.auth.BcryptHasher
import me.avo.realworld.kotlin.ktor.data.*
import me.avo.realworld.kotlin.ktor.persistence.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
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

        val user = UserSourceImpl().findUser("some@other.com")

        availableArticles.forEach{
            ArticleSourceImpl().insertArticle(user,it)
        }


    }


}

fun RegistrationDetails.insert() {
    val id = UserSourceImpl().insertUser(this)
    availableMap.put(this, id)
}

fun ArticleDetails.insert() {
    //ArticleSourceImpl().insertArticle()
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
// Articles


val artOne = Article("titletest","description test","body test",listOf("tag1"), "test1",DateTime(), DateTime())
val artTwo = Article("titletest1","description test","body test",listOf("tag2"), "test2",DateTime(), DateTime())
val artThree = Article("titletest2","description test","body test",listOf("tag3"), "test3",DateTime(), DateTime())

val availableArticles = listOf(artOne, artTwo, artThree)


// Tags
val availableTags = listOf("tag1","tag2","tag3")