package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class ArticleSourceImpl : ArticleSource {

    override fun getArticles(query: ArticleQuery): List<Article> = transaction {
        (Articles leftJoin Users leftJoin Tags leftJoin (Favorites leftJoin Users))
                .slice(Articles.columns + Favorites.articleId.count())
                .select { parseQuery(query) }
                .orderBy(Articles.createdAt, isAsc = false)
                .groupBy(*Articles.columns.toTypedArray())
                .limit(query.limit, query.offset)
                .map(ResultRow::toArticle)
                .map {
                    val tags = getTags(it)
                    it.copy(tagList = tags)
                }
    }

    fun parseQuery(query: ArticleQuery): Op<Boolean> = with(SqlExpressionBuilder) {
        listOf(query.tag?.let { Tags.tag eq it },
                query.author?.let { Users.username eq it },
                query.favoritedBy?.let { Users.username eq it and (Favorites.userId eq Users.id) })
                .filterNotNull()
                .reduce { acc, op -> acc and op }
    }

    fun getTags(article: Article) = Tags
            .slice(Tags.tag)
            .select { Tags.articleId eq article.id }
            .map { it[Tags.tag] }

    override fun getArticle(slug: String): Article {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun insertArticle(user: User, details: ArticleDetails): Article = transaction {
        val (title, description, body, tagList, slug, createdAt, updatedAt) = details
        val id = Articles.insert {
            it[Articles.slug] = slug
            it[Articles.title] = title
            it[Articles.description] = description
            it[Articles.body] = body
            it[Articles.createdAt] = createdAt
            it[Articles.updatedAt] = updatedAt
        } get Articles.id

        insertTags(id, tagList)
        Article(id, slug, title, description, body, tagList, createdAt, updatedAt, false, 0, TODO())
    }

    fun insertTags(articleId: Int, tags: List<String>) = transaction {
        tags.forEach { tag ->
            Tags.insert {
                it[Tags.articleId] = articleId
                it[Tags.tag] = tag
            }
        }

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

    override fun getFeed(): List<Article> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTags() = transaction {
        Tags.slice(Tags.tag)
                .selectAll()
                .map { it[Tags.tag] }
    }

}