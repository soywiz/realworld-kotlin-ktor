package me.avo.realworld.kotlin.ktor.auth

import me.avo.realworld.kotlin.ktor.data.User
import org.mindrot.jbcrypt.BCrypt

object BcryptHasher {

    /**
     * Check if the password matches the User's password
     */
    fun checkPassword(attempt: String, user: User) = if (BCrypt.checkpw(attempt, user.password)) Unit
    else throw Exception("Wrong Password")

    /**
     * Returns the hashed version of the supplied password
     */
    fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

}