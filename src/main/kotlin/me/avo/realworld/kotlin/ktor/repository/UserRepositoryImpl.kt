package me.avo.realworld.kotlin.ktor.repository

import me.avo.realworld.kotlin.ktor.model.RegistrationDetails
import me.avo.realworld.kotlin.ktor.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl : UserRepository {

    override fun findUser(email: String): User? = findUser(byEmail(email))

    fun findUser(where: Op<Boolean>) = transaction {
        Users.select(where)
            .firstOrNull()
            ?.let(ResultRow::toUser)
    }

    fun byId(id: Int): Op<Boolean> = Users.id eq id
    fun byEmail(email: String): Op<Boolean> = Users.email eq email

    override fun insertUser(details: RegistrationDetails): Int = transaction {
        Users.insert {
            it[email] = details.email
            it[password] = details.password
            it[username] = details.username
            it[bio] = ""
            it[image] = null
        } get Users.id ?: TODO()
    }

    override fun updateUser(new: User, current: User): User? = transaction {
        Users.update({ Users.id eq current.id }) {
            if (new.email != null) it[email] = new.email
            if (new.username != null) it[username] = new.username
            if (new.password != null) it[password] = new.password
            if (new.bio != null) it[bio] = new.bio
            it[image] = new.image
        }
        findUser(byId(current.id))?.copy(token = current.token)
    }

}