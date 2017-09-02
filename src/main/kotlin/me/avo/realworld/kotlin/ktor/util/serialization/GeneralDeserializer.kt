package me.avo.realworld.kotlin.ktor.util.serialization

import com.google.gson.*
import java.lang.reflect.Type
import kotlin.reflect.KClass

class GeneralDeserializer<T : Any>(clazz: KClass<T>) : JsonDeserializer<T> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): T = json
            .asJsonObject.entrySet().first().value
            .let { Gson().fromJson(it, typeOfT) }

}

inline fun <reified T : Any> GsonBuilder.register() = registerTypeAdapter(T::class.java, GeneralDeserializer(T::class))