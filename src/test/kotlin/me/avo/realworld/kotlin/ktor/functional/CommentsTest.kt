package me.avo.realworld.kotlin.ktor.functional

import com.github.salomonbrys.kotson.array
import com.github.salomonbrys.kotson.obj
import com.github.salomonbrys.kotson.string
import com.google.gson.JsonObject
import io.ktor.http.HttpMethod
import me.avo.realworld.kotlin.ktor.model.NewComment
import me.avo.realworld.kotlin.ktor.repository.CommentRepositoryImpl
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.OffsetDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentsTest : FunctionalTest {

    override val rootUri = "articles/how-to-train-your-dragon/comments"

    @Test fun `All Comments for Article`() = handleRequest(tokenUser = ensureUserExists()) {
        commentCheck(it)
    }

    @Test fun `Create Comment for Article`() = handleRequest(
        method = HttpMethod.Post,
        tokenUser = ensureUserExists(),
        body = "{\"comment\":{\"body\":\"Thank you so much!\"}}"
    ) {
        commentCheck(it)
    }

    @Test fun `Delete Comment for Article`() {
        val comment = CommentRepositoryImpl().addComment(NewComment("test"), "x")
        handleRequest(
            uri = "$rootUri/${comment.id}",
            method = HttpMethod.Delete,
            tokenUser = ensureUserExists()
        ) {

        }
    }

    private fun commentCheck(json: JsonObject) = json shouldHaveOwnProperty "comments" and {
        val comments = it.array
        comments.size() shouldBeGreaterOrEqualTo 1
        comments.first().obj.let {
            it shouldHaveOwnProperty "id"
            it shouldHaveOwnProperty "body"
            it shouldHaveOwnProperty "createdAt" and {
                OffsetDateTime.parse(it.string).toString() shouldBeEqualTo it.string
            }
            it shouldHaveOwnProperty "updatedAt" and {
                OffsetDateTime.parse(it.string).toString() shouldBeEqualTo it.string
            }
            it shouldHaveOwnProperty "author"
        }
    }

}