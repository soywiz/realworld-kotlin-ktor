package me.avo.realworld.kotlin.ktor.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

object JwtConfig {

    private const val secret = "rumRaisin1994kyoukoChan"
    private const val issuer = "aPureBase.com"
    private const val validityInMs = 36_000_000 // 10 hours

    fun parse(token: String): String = Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body
            .let { it["email"].toString() }

    /**
     * Produce a token for this combination of User and Account
     */
    fun makeToken(email: String): String = Jwts.builder()
            .setSubject("Authentication")
            .setIssuer(issuer)
            .claim("email", email)
            //.setExpiration(getExpiration())
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)


}