package me.avo.realworld.kotlin.ktor.repository

import io.ktor.http.HttpStatusCode
import me.avo.realworld.kotlin.ktor.model.Comment
import me.avo.realworld.kotlin.ktor.model.NewComment
import me.avo.realworld.kotlin.ktor.model.Profile
import me.avo.realworld.kotlin.ktor.model.User
import me.avo.realworld.kotlin.ktor.server.StatusException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

class CommentRepositoryImpl : CommentRepository {

    private class ArticleNotFoundException(slug: String) :
        StatusException("Could not find Article with slug $slug", HttpStatusCode.NotFound)

    override fun addComment(user: User, comment: NewComment, slug: String): Comment = transaction {
        val articleId = Articles
            .slice(Articles.id)
            .select { Articles.slug eq slug }
            .firstOrNull()
            ?.get(Articles.id) ?: throw ArticleNotFoundException(slug)
        val date = DateTime()
        val id = Comments.insert {
            it[body] = comment.body
            it[article_id] = articleId
            it[createdAt] = date
            it[updatedAt] = date
            it[authorId] = user.id
        } get Comments.id ?: throw Exception() // TODO improve exception
        Comment(id, date, date, comment.body, user.profile)
    }

    override fun getComments(slug: String, user: User?): List<Comment> = transaction {
        Join(Comments innerJoin Articles leftJoin Favorites, Users, JoinType.INNER, Users.id, Comments.authorId)
            .select {
                parse(
                    Articles.slug eq slug,
                    user?.let { Favorites.userId eq user.id }
                )!!
            }
            .map { row ->
                Comment(
                    row[Comments.id],
                    row[Comments.createdAt],
                    row[Comments.updatedAt],
                    row[Comments.body],
                    Profile(
                        row[Users.username],
                        row[Users.bio],
                        row[Users.image],
                        row.tryGet(Favorites.articleId) != null
                    )
                )
            }
    }

    override fun deleteComment(slug: String, id: String): Unit = transaction {
        Comments.deleteWhere { Comments.id eq id.toInt() }
    }

}
