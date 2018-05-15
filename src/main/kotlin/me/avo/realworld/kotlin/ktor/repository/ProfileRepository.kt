package me.avo.realworld.kotlin.ktor.repository

import me.avo.realworld.kotlin.ktor.model.Profile

interface ProfileRepository {

    fun getProfile(username: String, currentId: Int?): Profile

    fun getUserProfile(userId: Int): Profile

    fun follow(currentId: Int, username: String): Profile

    fun unfollow(currentId: Int, username: String): Profile

}