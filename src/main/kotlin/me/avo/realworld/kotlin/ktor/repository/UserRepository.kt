package me.avo.realworld.kotlin.ktor.repository

import me.avo.realworld.kotlin.ktor.model.RegistrationDetails
import me.avo.realworld.kotlin.ktor.model.User

interface UserRepository {

    fun findUser(email: String): User?

    fun insertUser(details: RegistrationDetails): Int

    fun updateUser(new: User, current: User): User?

}