package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.Profile

interface ProfileSource {

    fun getProfile(username: String, currendId: Int?): Profile

    fun follow(username: String, currentId: Int): Profile

    fun unfollow(username: String, currentId: Int): Profile

}