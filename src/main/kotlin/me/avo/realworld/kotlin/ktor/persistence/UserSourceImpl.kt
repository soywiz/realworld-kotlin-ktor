package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.RegistrationDetails
import me.avo.realworld.kotlin.ktor.data.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UserSourceImpl : UserSource {

    override fun findUser(email: String): User = findUser(byEmail(email))

    fun findUser(where: Op<Boolean>) = transaction {
        Users.select(where)
                .checkNull()
                .let(ResultRow::toUser)
    }

    private fun byId(id: Int): Op<Boolean> = Users.id eq id
    private fun byEmail(email: String): Op<Boolean> = Users.email eq email

    override fun insertUser(details: RegistrationDetails): Int = transaction {
        Users.insert {
            it[Users.email] = details.email
            it[Users.password] = details.password
            it[Users.username] = details.username
            it[Users.bio] = ""
            it[Users.image] = null
        } get Users.id
    }

    override fun updateUser(new: User, current: User): User = transaction {
        Users.update({ Users.id eq current.id }) {
            if (new.email != null) it[Users.email] = new.email
            if (new.username != null) it[Users.username] = new.username
            if (new.password != null) it[Users.password] = new.password
            if (new.bio != null) it[Users.bio] = new.bio
            it[Users.image] = new.image
        }
        findUser(byId(current.id)).copy(token = current.token)
    }


    private fun Query.checkNull(): ResultRow = firstOrNull() ?: throw Exception("User not found")

}