package me.avo.realworld.kotlin.ktor.data

import io.ktor.util.*

data class ArticleQuery(
        val tag: String?,
        val author: String?,
        val favoritedBy: String?,
        val limit: Int,
        val offset: Int
) {

    companion object {

        fun fromParameter(map: ValuesMap) = ArticleQuery(
                tag = map["tag"],
                author = map["author"],
                favoritedBy = map["favorited"],
                limit = map["limit"]?.toInt() ?: 20,
                offset = map["offset"]?.toInt() ?: 0
        )

    }

}