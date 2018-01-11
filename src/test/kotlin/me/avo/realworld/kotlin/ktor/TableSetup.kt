package me.avo.realworld.kotlin.ktor

import me.avo.realworld.kotlin.ktor.auth.*
import me.avo.realworld.kotlin.ktor.data.*
import me.avo.realworld.kotlin.ktor.persistence.*
import org.amshove.kluent.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.*
import org.joda.time.*
import org.junit.jupiter.api.*

@Disabled
class TableSetup {

    @Test
    fun setup() {

        setupEnvironment()
    }

}

fun setupEnvironment() {
    Setup()
    val articleSource: ArticleSource = ArticleSourceImpl()
    val userSource: UserSource = UserSourceImpl()
    transaction {
        SchemaUtils.drop(*tables)
        SchemaUtils.create(*tables)
        availableUsers.forEach(RegistrationDetails::insert)
        followSource follow followTarget

        val user = userSource.findUser(details.email).shouldNotBeNull()

        user.email.shouldNotBeBlank()
        availableArticles.forEach {
            articleSource.insertArticle(user, it)
        }


    }


}

private fun RegistrationDetails.insert() {
    val id = UserSourceImpl().insertUser(this)
    availableMap.put(this, id)
}

private fun ArticleDetails.insert() {
    //ArticleSourceImpl().insertArticle()
}

private infix fun RegistrationDetails.follow(target: RegistrationDetails) {
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


val artOne = Article("titletest", "description test", "body test", listOf("tag1"), "test1", DateTime(), DateTime())
val artTwo = Article("titletest1", "description test", "body test", listOf("tag2"), "test2", DateTime(), DateTime())
val artThree = Article("titletest2", "description test", "body test", listOf("tag3"), "test3", DateTime(), DateTime())

val availableArticles = listOf(artOne, artTwo, artThree)


// Tags
val availableTags = listOf("tag1", "tag2", "tag3")