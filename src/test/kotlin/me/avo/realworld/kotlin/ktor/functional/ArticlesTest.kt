package me.avo.realworld.kotlin.ktor.functional

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArticlesTest : FunctionalTest {

    override val rootUri = "articles"

    @Test fun `All Articles`() {

    }

    @Test fun `Articles by Author`() {

    }

    @Test fun `Articles Favorited by Username`() {

    }

    @Test fun `Articles by Tag`() {

    }

    @Test fun `Single Article by slug`() {

    }

}