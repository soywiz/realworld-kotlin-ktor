package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.User
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toUser() = User(
        email = this[Users.email],
        password = this[Users.password],
        token = "",
        username = this[Users.username],
        bio = this[Users.bio],
        image = this[Users.image]
)