package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.RegistrationDetails
import me.avo.realworld.kotlin.ktor.data.User
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserSourceImpl : UserSource {

    override fun findUser(email: String): User = transaction {
        Users.select { Users.email eq email }
                .checkNull()
                .let(ResultRow::toUser)
    }

    override fun insertUser(details: RegistrationDetails): Unit = transaction {
        Users.insert {
            it[Users.email] = details.email
            it[Users.password] = details.password
            it[Users.username] = details.username
            it[Users.bio] = ""
            it[Users.image] = null
        }
    }

    override fun updateUser(user: User): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun Query.checkNull(): ResultRow = firstOrNull() ?: throw Exception("User not found")

}