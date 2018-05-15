package me.avo.realworld.kotlin.ktor.repository

import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TagSourceImpl : TagSource {

    override fun getAllTags() = transaction {
        Tags.slice(Tags.tag)
            .selectAll()
            .map { it[Tags.tag] }
    }

}
