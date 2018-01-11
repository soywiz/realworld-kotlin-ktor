package me.avo.realworld.kotlin.ktor.auth

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import io.jsonwebtoken.*
import io.ktor.auth.jwt.*
import me.avo.realworld.kotlin.ktor.data.*
import java.util.*

object JwtConfig {

    private const val secret = "xE1x1o1x8qflc1iYtcRd"
    private const val issuer = "thinkster.io"
    const val realm = issuer
    private const val validityInMs = 36_000_000 // 10 hours
    private val algorithm = Algorithm.HMAC256(secret)

    val verifier: JWTVerifier = JWT
            .require(algorithm)
            .withIssuer(issuer)
            .build()

    /**
     * Produce a token for this combination of User and Account
     */
    fun makeToken(user: User): String = JWT.create()
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withClaim("id", user.id)
            .withClaim("email", user.email)
            .withExpiresAt(getExpiration())
            .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

}