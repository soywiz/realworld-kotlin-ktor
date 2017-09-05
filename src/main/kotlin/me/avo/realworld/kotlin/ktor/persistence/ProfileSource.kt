package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.Profile

interface ProfileSource {

    fun getProfile(username: String, currentId: Int?): Profile

    fun follow(currentId: Int, username: String): Profile

    fun unfollow(currentId: Int, username: String): Profile

}