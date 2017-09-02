package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.Profile

interface ProfileSource {

    fun getProfiles(username: String): Profile

    fun follow(username: String): Profile

    fun unfollow(username: String): Profile

}