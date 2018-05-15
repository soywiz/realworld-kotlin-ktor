package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ArticleSourceImpl : ArticleSource {

    override fun getArticles(query: ArticleQuery): List<ArticleDetails> = transaction {
        (Articles leftJoin Users leftJoin Tags)
//        (Articles leftJoin Users leftJoin Tags leftJoin (Favorites leftJoin Users))
                .slice(Articles.columns)
                .selectAll()
//                .select { parseQuery(query) }
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
        listOf( //query.tag?.let { Tags.tag eq it },
                query.author?.let { Users.username eq it })
//                query.favoritedBy?.let { Users.username eq it and (Favorites.userId eq Users.id) })
                .filterNotNull()
                .reduce { acc, op -> acc and op }
    }

    fun getTags(articleDetails: ArticleDetails) = Tags
            .slice(Tags.tag)
            .select { Tags.articleId eq articleDetails.id }
            .map { it[Tags.tag] }

    override fun getArticle(slug: String) = transaction {
        Articles.select{Articles.slug eq slug}.first()
                .let(ResultRow::toArticle )

    }

    override fun insertArticle(user: User, details11: Article) = transaction {
//        val (title, description, body, tagList, slug, createdAt, updatedAt) = details11
        val id = Articles.insert {
            it[Articles.slug] = details11.slug
            it[Articles.title] = details11.title
            it[Articles.description] = details11.description
            it[Articles.body] = details11.body
            it[Articles.createdAt] = details11.createdAt
            it[Articles.updatedAt] = details11.updatedAt
            it[Articles.authorId] = user.id
        } get Articles.id ?: TODO()

        insertTags(id, details11.tagList)
//        ArticleDetails(id, slug, title, description, body, tagList, createdAt, updatedAt, false, 0, TODO())
    }

    fun insertTags(articleId: Int, tags: List<String>) = transaction {
        tags.forEach { tag ->
            Tags.insert {
                it[Tags.articleId] = articleId
                it[Tags.tag] = tag
            }
        }

    }

private fun findArticle(where: () -> Op<Boolean>) = transaction {
    Articles.select{where.invoke()}
            .firstOrNull()?.let(ResultRow::toArticle)
}

    override fun updateArticle(articleDetails: ArticleDetails): ArticleDetails? = transaction {
        Articles.update({Articles.id eq articleDetails.id}) {
            it[Articles.title] = articleDetails.title
             it[Articles.body] = articleDetails.body
             it[Articles.description] = articleDetails.description
             it[Articles.slug] = articleDetails.slug
             it[Articles.createdAt] = articleDetails.createdAt
             it[Articles.updatedAt] = articleDetails.updatedAt
        }
        findArticle {Articles.id eq articleDetails.id}
    }

    override fun deleteArticle(articleId: Int) : Unit = transaction {
        Tags.deleteWhere { Tags.articleId eq articleId }
        Articles.deleteWhere { Articles.id eq articleId }
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

    override fun favorite(slug: String): ArticleDetails {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun unfavorite(slug: String): ArticleDetails {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getFeed(): List<ArticleDetails> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}