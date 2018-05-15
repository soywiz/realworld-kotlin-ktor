package me.avo.realworld.kotlin.ktor.model

import io.ktor.http.Parameters

data class ArticleQuery(
    val tag: String?,
    val author: String?,
    val favoritedBy: String?,
    val limit: Int,
    val offset: Int
) {

    constructor(map: Parameters) : this(
        tag = map["tag"],
        author = map["author"],
        favoritedBy = map["favorited"],
        limit = map["limit"]?.toInt() ?: 20,
        offset = map["offset"]?.toInt() ?: 0
    )

}