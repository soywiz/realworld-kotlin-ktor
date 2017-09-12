package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.availableTags
import me.avo.realworld.kotlin.ktor.data.ArticleQuery
import org.amshove.kluent.shouldContainAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ArticleSourceImplTest : TestEnvironment {

    val ds: ArticleSource = ArticleSourceImpl()

    @Test
    fun getArticles() {
        val query = ArticleQuery(null, null, null, 20, 0)
        ds.getArticles(query)
    }

    @Test
    fun getAll() {
        ds.getTags() shouldContainAll availableTags
    }


}