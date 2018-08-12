package me.avo.realworld.kotlin.ktor.functional

import com.github.salomonbrys.kotson.array
import com.github.salomonbrys.kotson.obj
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.ktor.http.HttpMethod
import me.avo.realworld.kotlin.ktor.model.Article
import me.avo.realworld.kotlin.ktor.model.Body
import me.avo.realworld.kotlin.ktor.model.NewComment
import me.avo.realworld.kotlin.ktor.model.RegistrationDetails
import me.avo.realworld.kotlin.ktor.repository.ArticleRepositoryImpl
import me.avo.realworld.kotlin.ktor.repository.CommentRepositoryImpl
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.joda.time.DateTime
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.OffsetDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentsTest : FunctionalTest {

    private val slug = "how-to-train-your-dragon"
    override val rootUri = "articles/$slug/comments"
    private val commentRepository = CommentRepositoryImpl()
    private val articleRepository = ArticleRepositoryImpl()
    private val user = ensureUserExists(RegistrationDetails("comment", "co@mmen.ts", "test"))
    private val article = articleRepository.insertArticle(
        user, Article("How to train your dragon", "x", "y", listOf("new"), slug, DateTime(), DateTime())
    )
    private val commentToDelete by lazy { commentRepository.addComment(user, NewComment(Body("test")), article.slug) }

    init {
        commentRepository.addComment(user, NewComment(Body("test")), article.slug)
    }

    @Test fun `All Comments for Article`() = handleRequest(tokenUser = ensureUserExists()) {
        commentCheck(it)
    }

    @Test fun `Create Comment for Article`() {
        val body = "Thank you so much!"
        handleRequest(
            method = HttpMethod.Post,
            tokenUser = ensureUserExists(),
            body = "{\"comment\":{\"body\":\"$body\"}}"
        ) {
            commentBodyCheck(it)
            it["body"].string shouldBeEqualTo body
        }
    }

    @Test fun `Delete Comment for Article`() {
        handleRequest(
            uri = "$rootUri/${commentToDelete.id}",
            method = HttpMethod.Delete,
            tokenUser = ensureUserExists()
        ) {

        }
    }

    private fun commentCheck(json: JsonObject): JsonArray = json shouldHaveOwnProperty "comments" and {
        it.array.apply {
            size() shouldBeGreaterOrEqualTo 1
            first().obj.let(::commentBodyCheck)
        }
    }

    private fun commentBodyCheck(json: JsonObject) {
        json shouldHaveOwnProperty "id"
        json shouldHaveOwnProperty "body"
        json shouldHaveOwnProperty "createdAt" and {
            OffsetDateTime.parse(json.string).toString() shouldBeEqualTo it.string
        }
        json shouldHaveOwnProperty "updatedAt" and {
            OffsetDateTime.parse(it.string).toString() shouldBeEqualTo it.string
        }
        json shouldHaveOwnProperty "author"
    }

}
