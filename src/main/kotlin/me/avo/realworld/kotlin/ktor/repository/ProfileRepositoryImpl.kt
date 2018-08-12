package me.avo.realworld.kotlin.ktor.repository

import me.avo.realworld.kotlin.ktor.model.Profile
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ProfileRepositoryImpl : ProfileRepository {

    override fun getProfile(username: String, currentId: Int?): Profile = transaction {
        Users.select { Users.username eq username }
            .checkNull()
            .let {
                val following = if (currentId != null) isFollowing(currentId, it[Users.id]) else false
                it.toProfile(following)
            }
    }

    override fun getUserProfile(userId: Int): Profile = transaction {
        Users.select { Users.id eq userId }
            .checkNull()
            .toProfile(false)
    }

    fun isFollowing(source: Int, target: Int): Boolean = transaction {
        Following.select { Following.sourceId eq source and (Following.targetId eq target) }
            .let { !it.empty() }
    }

    override fun follow(currentId: Int, username: String): Profile = transaction {
        val targetId = getTargetId(username)
        Following.insert {
            it[sourceId] = currentId
            it[Following.targetId] = targetId
        }
        getProfile(username, currentId)
    }

    override fun unfollow(currentId: Int, username: String): Profile = transaction {
        val targetId = getTargetId(username)
        Following.deleteWhere { Following.sourceId eq currentId and (Following.targetId eq targetId) }
        getProfile(username, currentId)
    }

    fun getTargetId(username: String) = transaction {
        Users.slice(Users.id)
            .select { Users.username eq username }
            .let { it.firstOrNull()?.get(Users.id) }
                ?: throw Exception("Couldn't follow User with name $username")
    }

    private fun Query.checkNull(): ResultRow = firstOrNull() ?: throw Exception("Profile not found")

}