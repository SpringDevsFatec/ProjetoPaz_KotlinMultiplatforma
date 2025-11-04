package com.projetopaz.kotlin.security

import com.projetopaz.kotlin.config.SecurityProperties
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class TokenService(
    private val securityProperties: SecurityProperties
) {

    private val signingKey: Key = Keys.hmacShaKeyFor(securityProperties.tokenSecretKey.toByteArray())

    fun generateToken(subject: String, userId: Long): String {
        val now = Date()
        val exp = Date(now.time + securityProperties.tokenExpirationSeconds * 6000)

        return Jwts.builder()
            .setSubject(subject)
            .claim("userId", userId)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(signingKey, SignatureAlgorithm.HS256)
            .compact()
    }


    fun extractUsername(token: String): String? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .body
                .subject
        } catch (ex: Exception) {
            null
        }
    }

    fun extractUserId(token: String): Long? {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .body

            val userId = claims["userId"]
            when (userId) {
                is Int -> userId.toLong()
                is Long -> userId
                is String -> userId.toLongOrNull()
                else -> null
            }
        } catch (ex: Exception) {
            null
        }
    }


    fun isTokenValid(token: String): Boolean {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token)
            claims.body.expiration.after(Date())
        } catch (ex: Exception) {
            false
        }
    }
}
