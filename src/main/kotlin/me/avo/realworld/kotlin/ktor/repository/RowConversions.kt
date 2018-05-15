package me.avo.realworld.kotlin.ktor.repository

import me.avo.realworld.kotlin.ktor.model.ArticleDetails
import me.avo.realworld.kotlin.ktor.model.Profile
import me.avo.realworld.kotlin.ktor.model.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

fun ResultRow.toUser() = User(
    id = this[Users.id],
    email = this[Users.email],
    password = this[Users.password],
    token = "",
    username = this[Users.username],
    bio = this[Users.bio],
    image = this[Users.image]
)

fun ResultRow.toProfile(following: Boolean): Profile {

    return Profile(

        username = this[Users.username],
        bio = this[Users.bio],
        image = this[Users.image],
        following = following
    )
}

fun ResultRow.toArticle() = ArticleDetails(
    id = this[Articles.id],
    slug = this[Articles.slug],
    title = this[Articles.title],
    description = this[Articles.description],
    body = this[Articles.body],
    tagList = Tags.slice(Tags.tag)
        .select { Tags.articleId eq this@toArticle[Articles.id] }
        .map { it[Tags.tag] },

    createdAt = this[Articles.createdAt],
    updatedAt = this[Articles.updatedAt],
    favorited = false, // TODO
    favoritesCount = 0,//this[Favorites.articleId.count()],
    author = ProfileRepositoryImpl().getUserProfile(this[Articles.authorId]) //ProfileRepositoryImpl().getProfile(UserRepositoryImpl().findUser(UserRepositoryImpl().byId(this[Articles.authorId])).username,null) //this.toProfile(false)
)
