package me.avo.realworld.kotlin.ktor.repository

import me.avo.realworld.kotlin.ktor.util.Property
import org.jetbrains.exposed.sql.Database

class Setup {

    init {
        Database.connect(
            url = "${Property["db.host"]}/${Property["db.database"]}",
            driver = Property["db.driver"],
            user = Property["db.user"],
            password = Property["db.pass"]
        )
    }

}