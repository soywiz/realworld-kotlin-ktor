package me.avo.realworld.kotlin.ktor.repository

import me.avo.realworld.kotlin.ktor.model.Article
import me.avo.realworld.kotlin.ktor.model.ArticleDetails
import me.avo.realworld.kotlin.ktor.model.ArticleQuery
import me.avo.realworld.kotlin.ktor.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class ArticleRepositoryImpl : ArticleRepository {

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
        listOfNotNull(query.author?.let { Users.username eq it })
            .reduce { acc, op -> acc and op }
    }

    fun getTags(articleDetails: ArticleDetails) = Tags
        .slice(Tags.tag)
        .select { Tags.articleId eq articleDetails.id }
        .map { it[Tags.tag] }

    override fun getArticle(slug: String) = transaction {
        Articles.select { Articles.slug eq slug }.first()
            .let(ResultRow::toArticle)

    }

    override fun insertArticle(user: User, article: Article): ArticleDetails = transaction {
        val id = Articles.insert {
            it[slug] = article.slug
            it[title] = article.title
            it[description] = article.description
            it[body] = article.body
            it[createdAt] = article.createdAt
            it[updatedAt] = article.updatedAt
            it[authorId] = user.id
        } get Articles.id ?: throw Exception("") // TODO improve exception

        insertTags(id, article.tagList)
        ArticleDetails(
            id,
            article.slug,
            article.title,
            article.description,
            article.body,
            article.tagList,
            article.createdAt,
            article.updatedAt,
            false, // TODO
            0, // TODO
            user.profile
        )
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
        Articles.select { where.invoke() }
            .firstOrNull()?.let(ResultRow::toArticle)
    }

    override fun updateArticle(articleDetails: ArticleDetails): ArticleDetails? = transaction {
        Articles.update({ Articles.id eq articleDetails.id }) {
            it[title] = articleDetails.title
            it[body] = articleDetails.body
            it[description] = articleDetails.description
            it[slug] = articleDetails.slug
            it[createdAt] = articleDetails.createdAt
            it[updatedAt] = articleDetails.updatedAt
        }
        findArticle { Articles.id eq articleDetails.id }
    }

    override fun deleteArticle(articleId: Int): Unit = transaction {
        Tags.deleteWhere { Tags.articleId eq articleId }
        Articles.deleteWhere { Articles.id eq articleId }
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
