package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.Article
import me.avo.realworld.kotlin.ktor.data.Profile
import me.avo.realworld.kotlin.ktor.data.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.count

fun ResultRow.toUser() = User(
        id = this[Users.id],
        email = this[Users.email],
        password = this[Users.password],
        token = "",
        username = this[Users.username],
        bio = this[Users.bio],
        image = this[Users.image]
)

fun ResultRow.toProfile(following: Boolean) = Profile(
        username = this[Users.username],
        bio = this[Users.bio],
        image = this[Users.image],
        following = following
)

fun ResultRow.toArticle() = Article(
        id = this[Articles.id],
        slug = this[Articles.slug],
        title = this[Articles.title],
        description = this[Articles.description],
        body = this[Articles.body],
        tagList = listOf(),
        createdAt = this[Articles.createdAt],
        updatedAt = this[Articles.updatedAt],
        favorited = false, // TODO
        favoritesCount = this[Favorites.articleId.count()],
        author = this.toProfile(false)
)