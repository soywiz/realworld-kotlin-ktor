package me.avo.realworld.kotlin.ktor.repository

import me.avo.realworld.kotlin.ktor.model.Comment
import me.avo.realworld.kotlin.ktor.model.NewComment
import me.avo.realworld.kotlin.ktor.model.Profile
import me.avo.realworld.kotlin.ktor.model.User
import org.jetbrains.exposed.sql.Join
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class CommentRepositoryImpl : CommentRepository {

    override fun addComment(comment: NewComment, slug: String): Comment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun deleteComment(slug: String, id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
