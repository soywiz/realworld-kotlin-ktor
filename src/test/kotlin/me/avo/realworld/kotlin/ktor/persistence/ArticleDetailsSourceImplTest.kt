package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.*
import me.avo.realworld.kotlin.ktor.data.*
import org.amshove.kluent.*
import org.joda.time.*
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ArticleDetailsSourceImplTest : TestEnvironment {

    val ds: ArticleSource = ArticleSourceImpl()
    val userDb: UserSource = UserSourceImpl()

    @Test
    fun getArticles() {
        val query = ArticleQuery(null, null, null, 20, 0)
        ds.getArticles(query).size `should equal` 3 //.map { it.toArticle() } shouldContainAll availableArticles
    }

    @Test
    fun getAll() {
        ds.getTags() shouldContainAll availableTags
    }

    @Test
    fun insertArticle() {
        val user = userDb.findUser("some@other.com")
        val article = Article("new title", "new", "aa", listOf("new tag"), "new slug", DateTime(), DateTime())
        ds.insertArticle(user, article)
        ds.getArticle("new slug").toArticle() `should equal` article
    }

    @Test
    fun updateArticle() {
        val copy = ds.getArticle("test2").copy(slug = "updatedSlug", description = "updated description", title = "updated title")
        val updateArticle = ds.updateArticle(copy)
        println(copy)
        copy `should equal` updateArticle
    }

    @Test
    fun deleteArticle() {
        val copy = ds.getArticle("test1")
        ds.deleteArticle(copy.id)
        val query = ArticleQuery(null, null, null, 20, 0)
        ds.getArticles(query) `should not contain` (copy)

    }


}