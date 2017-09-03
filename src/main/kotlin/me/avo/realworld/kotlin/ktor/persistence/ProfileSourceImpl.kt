package me.avo.realworld.kotlin.ktor.persistence

import me.avo.realworld.kotlin.ktor.data.Profile
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class ProfileSourceImpl : ProfileSource {

    override fun getProfile(username: String, currentId: Int?): Profile = transaction {
        Users.select { Users.username eq username }
                .checkNull()
                .let {
                    val following = if (currentId != null) isFollowing(currentId, it[Users.id]) else false
                    it.toProfile(following)
                }
    }

    fun isFollowing(source: Int, target: Int): Boolean = transaction {
        Following.select { Following.sourceId eq source and (Following.targetId eq target) }.let { !it.empty() }
    }

    override fun follow(username: String, currentId: Int): Profile = transaction {
        val target = Users.slice(Users.id).select { Users.username eq username }.let { it.firstOrNull()?.get(Users.id) }
                ?: throw Exception("Couldn't follow User with name $username")
        Following.insert {
            it[sourceId] = currentId
            it[targetId] = target
        }
        getProfile(username, currentId)
    }

    override fun unfollow(username: String, currentId: Int): Profile {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun Query.checkNull(): ResultRow = firstOrNull() ?: throw Exception("Profile not found")

}