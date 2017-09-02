package me.avo.realworld.kotlin.ktor.auth

import me.avo.realworld.kotlin.ktor.data.LoginCredentials
import me.avo.realworld.kotlin.ktor.data.RegistrationDetails
import me.avo.realworld.kotlin.ktor.data.User
import me.avo.realworld.kotlin.ktor.persistence.UserSource
import me.avo.realworld.kotlin.ktor.persistence.UserSourceImpl

class LoginHandler {

    private val db: UserSource = UserSourceImpl()

    fun login(credentials: LoginCredentials): User = credentials.let { (email, password) ->
        val user = db.findUser(email)
        BcryptHasher.checkPassword(password, user)
        val token = JwtConfig.makeToken(email)
        return user.copy(token = token)
    }

    fun register(details: RegistrationDetails): User = details.let { (username, email, password) ->
        val hashed = BcryptHasher.hashPassword(password)
        db.insertUser(details.copy(password = hashed))
        val token = JwtConfig.makeToken(email)
        return User(email, "", token, username, "", null)
    }

    fun updateUser(new: User, current: User): User = new
            .copy(password = BcryptHasher.hashPassword(new.password), token = current.token)
            .also { db.updateUser(it, current) }

}