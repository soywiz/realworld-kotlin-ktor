package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.Article
import me.avo.realworld.kotlin.ktor.data.ArticleQuery
import me.avo.realworld.kotlin.ktor.data.Comment
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.SqlExpressionBuilder
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class ArticleSourceImpl : ArticleSource {

    override fun getArticles(query: ArticleQuery): List<Article> = transaction {
        (Articles leftJoin Tags leftJoin Favorites)
                .select { parseQuery(query) }
                .orderBy(Articles.createdAt, false)
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun parseQuery(query: ArticleQuery): Op<Boolean> = with(SqlExpressionBuilder) {
        query.tag?.let { Tags.tag eq it }

        TODO()
    }

    override fun getArticle(slug: String): Article {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertArticle(): Article {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateArticle(): Article {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteArticle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addComment(comment: Comment): Comment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getComments(slug: String): List<Comment> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteComment(slug: String, id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun favorite(slug: String): Article {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unfavorite(slug: String): Article {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTags(): List<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFeed(): List<Article> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}