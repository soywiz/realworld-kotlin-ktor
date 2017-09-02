package me.avo.realworld.kotlin.ktor.util.serialization

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import me.avo.realworld.kotlin.ktor.data.LoginCredentials

class UserAdapter : TypeAdapter<LoginCredentials>() {

    override fun read(`in`: JsonReader) = with(`in`) {
        println("Reading")
        beginObject()
        nextName()
        beginObject()
        nextName()
        val email = nextString()
        nextName()
        val password = nextString()
        endObject()
        endObject()
        LoginCredentials(email, password)
    }

    override fun write(out: JsonWriter, value: LoginCredentials?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}