package boki.bokiportfolio.common

import org.springframework.http.HttpStatus

enum class ErrorCode(val status: HttpStatus, var message: String) {
    // Common
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터 요청입니다"),
    INVALID_ACCESS(HttpStatus.BAD_REQUEST, "유효하지 않은 접근입니다"),

    // Auth
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 JWT 토큰입니다"),

    // Role
    NOT_FOUND_ROLE(HttpStatus.NOT_FOUND, "해당 권한은 존재하지 않습니다"),

    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 유저가 없습니다"),
    DUPLICATE_USER(HttpStatus.BAD_REQUEST, "중복된 이메일입니다"),
}
