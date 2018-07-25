package me.avo.realworld.kotlin.ktor.functional

import com.github.salomonbrys.kotson.array
import com.github.salomonbrys.kotson.int
import com.github.salomonbrys.kotson.obj
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticlesTest : FunctionalTest {

    override val rootUri = "articles"

    @Test fun `All Articles`() = handleRequest {
        checkArticles(it)
    }

    @Test fun `Articles by Author`() = handleRequest(
        uri = "$rootUri?author=johnjacob"
    ) {
        checkArticles(it)
    }

    @Test fun `Articles Favorited by Username`() = handleRequest(
        uri = "$rootUri?favorited=jane"
    ) { checkArticles(it) }

    @Test fun `Articles by Tag`() = handleRequest(
        uri = "$rootUri?tag=dragons"
    ) { checkArticles(it) }

    @Test fun `Single Article by slug`() = handleRequest(
        uri = "$rootUri/how-to-train-your-dragon"
    ) { checkSingleArticle(it) }

    private fun checkArticles(json: JsonObject) {
        json shouldHaveOwnProperty "articlesCount" and { it.int }
        json shouldHaveOwnProperty "articles" and {
            it.array
                .map(JsonElement::obj)
                .forEach(::checkSingleArticle)
        }
    }

    private fun checkSingleArticle(json: JsonObject) {
        json shouldHaveOwnProperty "title"
        json shouldHaveOwnProperty "slug"
        json shouldHaveOwnProperty "body"
        json shouldHaveOwnProperty "createdAt" // TODO check ISO 8601
        json shouldHaveOwnProperty "updatedAt"
        json shouldHaveOwnProperty "description"
        json shouldHaveOwnProperty "tagList" and { it.array }
        json shouldHaveOwnProperty "author"
        json shouldHaveOwnProperty "favorited"
        json shouldHaveOwnProperty "favoritesCount" and { it.int }
    }

}