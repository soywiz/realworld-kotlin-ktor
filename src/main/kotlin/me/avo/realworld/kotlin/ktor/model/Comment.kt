package me.avo.realworld.kotlin.ktor.model

import org.joda.time.DateTime

data class Comment(
    val id: Int,
    val createdAt: DateTime,
    val updatedAt: DateTime,
    val body: String,
    val author: Profile
)

data class NewComment(val comment: Body) {
    val body get() = comment.body
}

class Body(val body: String)

class CommentsWrapper(val comments: List<Comment>)