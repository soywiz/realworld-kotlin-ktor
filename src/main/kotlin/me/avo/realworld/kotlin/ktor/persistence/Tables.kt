package me.avo.realworld.kotlin.ktor.persistence

import org.jetbrains.exposed.sql.Table

val tables = arrayOf(Users)

object Users : Table("users") {

    val email = varchar("email", 254).primaryKey()
    val password = varchar("password", 255)
    val username = varchar("name", 50)
    val bio = varchar("bio", 255)
    val image = varchar("image", 255).nullable()

}