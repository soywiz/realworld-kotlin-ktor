package me.avo.realworld.kotlin.ktor.auth

import me.avo.realworld.kotlin.ktor.data.LoginCredentials
import me.avo.realworld.kotlin.ktor.data.RegistrationDetails
import me.avo.realworld.kotlin.ktor.data.User
import me.avo.realworld.kotlin.ktor.persistence.UserSource
import me.avo.realworld.kotlin.ktor.persistence.UserSourceImpl

class LoginHandler {

    private val db: UserSource = UserSourceImpl()

    fun login(credentials: LoginCredentials): User {
        val (email, password) = credentials
        // TODO check password
        val user = db.findUser(email)
        val token = JwtConfig.makeToken(email)
        return user.copy(token = token)
    }

    fun register(details: RegistrationDetails): User {
        val (username, email, password) = details
        val hashed = BcryptHasher.hashPassword(password)
        db.insertUser(details.copy(password = hashed))
        val token = JwtConfig.makeToken(email)
        return User(email, "", token, username, "", null)
    }

}