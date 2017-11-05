package me.avo.realworld.kotlin.ktor.data

import org.joda.time.DateTime

 data class Article(
  val title: String,
  val description: String,
  val body: String,
  val tagList: List<String>,
  val slug: String,
  val createdAt: DateTime,
  val updatedAt: DateTime
 )