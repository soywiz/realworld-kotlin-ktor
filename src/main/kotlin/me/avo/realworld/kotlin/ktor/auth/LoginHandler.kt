package me.avo.realworld.kotlin.ktor.auth

import me.avo.realworld.kotlin.ktor.model.LoginCredentials
import me.avo.realworld.kotlin.ktor.model.RegistrationDetails
import me.avo.realworld.kotlin.ktor.model.User
import me.avo.realworld.kotlin.ktor.repository.UserRepository
import me.avo.realworld.kotlin.ktor.repository.UserRepositoryImpl

class LoginHandler {

    private val db: UserRepository =
        UserRepositoryImpl()

    fun login(credentials: LoginCredentials): User = credentials.let { (email, password) ->
        val user = db.findUser(email) ?: throw AuthenticationException.USER_NOT_FOUND
        BcryptHasher.checkPassword(password, user)
        val token = JwtConfig.makeToken(user)
        return user.copy(token = token)
    }

    fun register(details: RegistrationDetails): User = details.let { (username, email, password) ->
        val hashed = BcryptHasher.hashPassword(password)
        val id = db.insertUser(details.copy(password = hashed))
        return User(id, email, "", "", username, "", null).run {
            copy(token = JwtConfig.makeToken(this))
        }
    }

    fun updateUser(new: User, current: User): User {
        val final = if (new.password != null) new.copy(password = BcryptHasher.hashPassword(new.password)) else new
        return db.updateUser(final, current) ?: throw AuthenticationException.USER_NOT_FOUND
    }

}