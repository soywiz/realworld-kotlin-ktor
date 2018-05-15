package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.*
import me.avo.realworld.kotlin.ktor.repository.TagSourceImpl
import org.amshove.kluent.*
import org.junit.jupiter.api.*

internal class TagSourceImplTest {

    val ds = TagSourceImpl()

    @Test
    fun getAll() {
        ds.getAllTags() shouldContainAll availableTags
    }

}