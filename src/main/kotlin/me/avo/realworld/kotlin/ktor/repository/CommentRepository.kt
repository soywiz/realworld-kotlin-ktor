package me.avo.realworld.kotlin.ktor.repository

import me.avo.realworld.kotlin.ktor.model.Comment
import me.avo.realworld.kotlin.ktor.model.NewComment
import me.avo.realworld.kotlin.ktor.model.User

interface CommentRepository {

    fun addComment(comment: NewComment, slug: String): Comment

    fun getComments(slug: String, user: User?): List<Comment>

    fun deleteComment(slug: String, id: String)

}
