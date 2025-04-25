package com.std.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import kotlin.math.log

@Service
class JwtService {

    private val secretKey: String = "your-super-secret-key-1234567890-of-all-time"


    val logger = LoggerFactory.getLogger(JwtService::class.java)


    private val expirationTime: Long = 86400000L

    fun generateToken(userEmail: String): String {

        logger.info("Email passed for jwt token: $userEmail")

        val claims = Jwts.claims().setSubject(userEmail)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expirationTime))
            .signWith(
                Keys.hmacShaKeyFor(secretKey.toByteArray()),
                SignatureAlgorithm.HS256
            )
            .compact()
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray())) // set key
                .build()
                .parseClaimsJws(token)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun extractEmail(token: String): String? {
        return extractClaims(token)?.subject
    }

    private fun extractClaims(token: String): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray()))
                .build()
                .parseClaimsJws(token)
                .body
        } catch (e: Exception) {
            logger.info("Failed to extract claim, eMessage: ${e.message}")
            null
        }
    }
}


