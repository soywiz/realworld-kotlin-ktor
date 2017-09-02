package me.avo.realworld.kotlin.ktor.util.serialization

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import me.avo.realworld.kotlin.ktor.data.User

class ExclusionStrat : ExclusionStrategy {

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }

    override fun shouldSkipField(f: FieldAttributes): Boolean = when (f.declaringClass) {
        User::class.java -> f.name == User::password.name
        else -> false
    }

}