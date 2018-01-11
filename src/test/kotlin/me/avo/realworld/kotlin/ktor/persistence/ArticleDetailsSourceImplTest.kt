package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.*
import org.amshove.kluent.*
import me.avo.realworld.kotlin.ktor.shouldNotBeNull
import org.joda.time.*
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ArticleDetailsSourceImplTest : TestEnvironment {

    private val articleSource: ArticleSource = ArticleSourceImpl()
    private val userSource: UserSource = UserSourceImpl()

    @Test
    fun getArticles() {
        val query = ArticleQuery(null, null, null, 20, 0)
        articleSource.getArticles(query).size `should equal` 3 //.map { it.toArticle() } shouldContainAll availableArticles
    }

    @Test
    fun insertArticle() {
        val user = userSource.findUser("some@other.com").shouldNotBeNull()
        val article = Article("new title", "new", "aa", listOf("new tag"), "new slug", DateTime(), DateTime())
        articleSource.insertArticle(user, article)
        articleSource.getArticle("new slug").toArticle() `should equal` article
    }

    @Test
    fun updateArticle() {
        val copy = articleSource.getArticle("test2").copy(slug = "updatedSlug", description = "updated description", title = "updated title")
        val updateArticle = articleSource.updateArticle(copy)
        println(copy)
        copy `should equal` updateArticle
    }

    @Test
    fun deleteArticle() {
        val copy = articleSource.getArticle("test1")
        articleSource.deleteArticle(copy.id)
        val query = ArticleQuery(null, null, null, 20, 0)
        articleSource.getArticles(query) `should not contain` (copy)

    }


}