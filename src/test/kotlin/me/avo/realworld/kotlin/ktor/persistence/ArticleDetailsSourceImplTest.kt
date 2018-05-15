package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.model.*
import me.avo.realworld.kotlin.ktor.repository.ArticleRepository
import me.avo.realworld.kotlin.ktor.repository.ArticleRepositoryImpl
import me.avo.realworld.kotlin.ktor.repository.UserRepository
import me.avo.realworld.kotlin.ktor.repository.UserRepositoryImpl
import org.amshove.kluent.*
import me.avo.realworld.kotlin.ktor.shouldNotBeNull
import org.joda.time.*
import org.junit.jupiter.api.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ArticleDetailsSourceImplTest : TestEnvironment {

    private val articleRepository: ArticleRepository =
        ArticleRepositoryImpl()
    private val userRepository: UserRepository =
        UserRepositoryImpl()

    @Test
    fun getArticles() {
        val query = ArticleQuery(null, null, null, 20, 0)
        articleRepository.getArticles(query).size `should equal` 3 //.map { it.toArticle() } shouldContainAll availableArticles
    }

    @Test
    fun insertArticle() {
        val user = userRepository.findUser("some@other.com").shouldNotBeNull()
        val article = Article("new title", "new", "aa", listOf("new tag"), "new slug", DateTime(), DateTime())
        articleRepository.insertArticle(user, article)
        articleRepository.getArticle("new slug").toArticle() `should equal` article
    }

    @Test
    fun updateArticle() {
        val copy = articleRepository.getArticle("test2").copy(slug = "updatedSlug", description = "updated description", title = "updated title")
        val updateArticle = articleRepository.updateArticle(copy)
        println(copy)
        copy `should equal` updateArticle
    }

    @Test
    fun deleteArticle() {
        val copy = articleRepository.getArticle("test1")
        articleRepository.deleteArticle(copy.id)
        val query = ArticleQuery(null, null, null, 20, 0)
        articleRepository.getArticles(query) `should not contain` (copy)

    }


}