package boki.bokiportfolio.security

import boki.bokiportfolio.common.ErrorCode
import boki.bokiportfolio.exception.CustomException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtProvider {

    companion object {
        const val ACCESS_TOKEN_EXPIRATION_DURATION: Long = 1000 * 60 * 60 * 24 * 3L // 3일
        const val REFRESH_TOKEN_EXPIRATION_DURATION: Long = 1000 * 60 * 60 * 24 * 30L // 1달
    }

    @Value("\${boki.token.secret}")
    private lateinit var secretKey: String

    private val signKey by lazy { Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)) }

    fun generatePairToken(userId: Long): TokenPair {
        return TokenPair(
            accessToken = generateSingleToken(userId = userId),
            refreshToken = generateSingleToken(userId = userId, isRefreshToken = true),
        )
    }

    private fun generateSingleToken(userId: Long, isRefreshToken: Boolean = false): Token {
        val now = Date()
        val tokenExpiredAt: Date
        val tokenValue: String

        if (isRefreshToken) {
            tokenExpiredAt = Date(now.time + REFRESH_TOKEN_EXPIRATION_DURATION)
            tokenValue = UUID.randomUUID().toString().replace("-", "")
        }
        else {
            tokenExpiredAt = Date(now.time + ACCESS_TOKEN_EXPIRATION_DURATION)
            tokenValue = Jwts.builder()
                .header().type("JWT").and()
                .subject(userId.toString())
                .expiration(tokenExpiredAt)
                .signWith(signKey)
                .compact()
        }

        return Token(
            value = tokenValue,
            expiredAt = tokenExpiredAt.toInstant(),
        )
    }

    fun getUserIdFromToken(token: String): Long {
        val claims = Jwts.parser().verifyWith(signKey).build().parseSignedClaims(token).payload
        return claims.subject.toLong()
    }

    fun validateToken(accessToken: String) {
        try {
            Jwts.parser().verifyWith(signKey).build().parseSignedClaims(accessToken).payload
        } catch (e: JwtException) {
            var errMsg = ErrorCode.INVALID_JWT_TOKEN.message
            when (e) {
                is MalformedJwtException -> errMsg = "잘못된 형식의 JWT 토큰입니다."
                is ExpiredJwtException -> errMsg = "만료된 JWT 토큰입니다."
                is UnsupportedJwtException -> errMsg = "지원되지 않는 JWT 토큰입니다."
            }
            throw CustomException(ErrorCode.INVALID_JWT_TOKEN, errMsg)
        }
    }
}
