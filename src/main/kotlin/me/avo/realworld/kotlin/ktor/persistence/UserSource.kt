package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.RegistrationDetails
import me.avo.realworld.kotlin.ktor.data.User

interface UserSource {

    fun findUser(email: String) : User

    fun insertUser(details: RegistrationDetails)

}