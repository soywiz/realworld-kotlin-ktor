package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.availableArticles
import me.avo.realworld.kotlin.ktor.availableTags
import me.avo.realworld.kotlin.ktor.data.Article
import me.avo.realworld.kotlin.ktor.data.ArticleQuery
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not contain`
import org.amshove.kluent.shouldContainAll
import org.joda.time.DateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ArticleDetailsSourceImplTest : TestEnvironment {

    val ds: ArticleSource = ArticleSourceImpl()
    val userDb : UserSource = UserSourceImpl()

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
    fun insertArticle(){
        val user = userDb.findUser("some@other.com")
        val article = Article("new title","new","aa", listOf("new tag"),"new slug",DateTime(),DateTime())
        ds.insertArticle(user,article)
        ds.getArticle("new slug").toArticle() `should equal` article
    }

    @Test
    fun updateArticle(){
        val copy = ds.getArticle("test2").copy(slug = "updatedSlug", description = "updated description", title = "updated title")
        val updateArticle = ds.updateArticle(copy)
        println(copy)
        copy `should equal` updateArticle
    }

    @Test
    fun deleteArticle(){
        val copy = ds.getArticle("test1")
        ds.deleteArticle(copy.id)
        val query = ArticleQuery(null, null, null, 20, 0)
        ds.getArticles(query) `should not contain`(copy)

    }



}