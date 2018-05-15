package me.avo.realworld.kotlin.ktor.util.serialization

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import me.avo.realworld.kotlin.ktor.model.User

class SerialStrategy : ExclusionStrategy {

    override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return false
    }

    override fun shouldSkipField(f: FieldAttributes): Boolean = when (f.declaringClass) {
        User::class.java -> f.name in listOf(User::password.name, User::id.name)
        else -> false
    }

}