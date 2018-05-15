package me.avo.realworld.kotlin.ktor.util.serialization

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

class DeserializationStrategy : ExclusionStrategy {

    override fun shouldSkipClass(clazz: Class<*>): Boolean {
        return false
    }

    override fun shouldSkipField(f: FieldAttributes): Boolean {
        return false
    }

}