package boki.bokiportfolio.security

import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant

@Schema(name = "JWT 토큰", description = "JWT 토큰 정보")
data class Token(
    @field:Schema(description = "토큰 값")
    val value: String,

    @field:Schema(description = "만료일자")
    val expiredAt: Instant,
)

@Schema(name = "인증 응답", description = "인증 응답 정보")
data class TokenPair(
    @field:Schema(description = "엑세스 토큰")
    val accessToken: Token,

    @field:Schema(description = "리프레시 토큰")
    val refreshToken: Token,
)
