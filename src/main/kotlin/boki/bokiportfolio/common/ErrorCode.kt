package boki.bokiportfolio.common

import org.springframework.http.HttpStatus

enum class ErrorCode(val status: HttpStatus, var message: String) {
    // Common
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터 요청입니다"),
    INVALID_ACCESS(HttpStatus.BAD_REQUEST, "유효하지 않은 접근입니다"),

    // Auth
    FAILED_LOGIN(HttpStatus.BAD_REQUEST, "로그인에 실패하였습니다"),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증이 필요한 요청입니다"),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "권한이 필요한 요청입니다"),
    INVALID_JWT_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 JWT 토큰입니다"),

    // Role
    NOT_FOUND_ROLE(HttpStatus.NOT_FOUND, "해당 권한은 존재하지 않습니다"),

    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "등록된 회원 정보가 없습니다"),
    DUPLICATE_USER(HttpStatus.BAD_REQUEST, "중복된 이메일입니다"),

    // Article
    NOT_FOUND_ARTICLE(HttpStatus.NOT_FOUND, "해당 게시글은 존재하지 않습니다"),
    INVALID_EDIT_ARTICLE(HttpStatus.BAD_REQUEST, "해당 게시글은 수정 가능한 날짜가 지났습니다"),
    INVALID_IMG_FILE(HttpStatus.BAD_REQUEST, "이미지 파일이 아닙니다"),
    INVALID_LIKE_REQUEST(HttpStatus.BAD_REQUEST, "자신의 글은 좋아요 할 수 없습니다"),

    // Comment
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당 댓글은 존재하지 않습니다"),
    INVALID_ACCESS_COMMENT(HttpStatus.BAD_REQUEST, "본인의 댓글만 수정/삭제할 수 있습니다"),
}
