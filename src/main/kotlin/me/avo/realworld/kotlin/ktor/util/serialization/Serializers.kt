package me.avo.realworld.kotlin.ktor.util.serialization

import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.registerTypeAdapter
import com.google.gson.*
import java.lang.reflect.Type

class GeneralDeserializer<T : Any> : JsonDeserializer<T> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T = json
        .asJsonObject
        .entrySet()
        .first()
        .value
        .let { gsonWithStrats.fromJson(it, typeOfT) }

}

class GeneralSerializer<T : Any> : JsonSerializer<T> {

    override fun serialize(src: T, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val json = gsonWithStrats.toJsonTree(src, typeOfSrc)
        return jsonObject(src::class.simpleName!!.toLowerCase() to json)
    }

}

val gsonWithStrats: Gson = GsonBuilder().apply {
    serializeNulls()
    addDeserializationExclusionStrategy(DeserializationStrategy())
    addSerializationExclusionStrategy(SerialStrategy())
}.create()

inline fun <reified T : Any> GsonBuilder.register() {
    registerTypeAdapter(GeneralSerializer<T>())
    registerTypeAdapter(GeneralDeserializer<T>())
}