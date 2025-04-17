package com.std.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

// *************************************
// ******* UnderStand JWT and this service ************
// *************************************

@Service
class JwtService {

    private val jwtExpirationMs = 1000*60*60*24 // 24-hr expiration time
    private val secretKey: SecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun generateToken(email: String): String {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() * jwtExpirationMs))
            .signWith(secretKey)
            .compact()
    }

    fun extractEmail(token: String): String? {
        return extractAllClaims(token)?.subject
    }

    fun isTokenValid(token: String): Boolean {
        val claims = extractAllClaims(token)
        return claims?.expiration?.after(Date()) == true
    }

    private fun extractAllClaims(token: String): Claims? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJwt(token)
                .body
        } catch (e: Exception) {
            null
        }
    }

}