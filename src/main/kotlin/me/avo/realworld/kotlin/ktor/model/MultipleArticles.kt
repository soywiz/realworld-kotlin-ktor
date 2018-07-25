package me.avo.realworld.kotlin.ktor.model

class MultipleArticles(
    val articles: List<ArticleDetails>
) {

    val articlesCount: Int = articles.size
}