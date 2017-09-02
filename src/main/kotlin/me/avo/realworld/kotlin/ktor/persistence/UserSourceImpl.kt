package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.RegistrationDetails
import me.avo.realworld.kotlin.ktor.data.User
import org.jetbrains.exposed.sql.*
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

    override fun updateUser(new: User, current: User): Unit = transaction {
        Users.update({ Users.email eq current.email }) {
            it[Users.email] = new.email
            it[Users.username] = new.username
            it[Users.password] = new.password // TODO hash
            it[Users.bio] = new.bio
            it[Users.image] = new.image
        }
    }


    private fun Query.checkNull(): ResultRow = firstOrNull() ?: throw Exception("User not found")

}