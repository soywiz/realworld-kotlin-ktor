package me.avo.realworld.kotlin.ktor.auth

import org.mindrot.jbcrypt.BCrypt

object BcryptHasher {

    /**
     * Check if the password matches the User's password
     */
    fun checkPassword(attempt: String, correct: String) = if (BCrypt.checkpw(attempt, correct)) Unit
    else throw Exception("Wrong Password")

    /**
     * Returns the hashed version of the supplied password
     */
    fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())

}