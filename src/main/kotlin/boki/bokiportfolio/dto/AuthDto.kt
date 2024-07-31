package boki.bokiportfolio.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "로그인 요청", description = "로그인 요청 정보")
data class LoginRequest(
    @field:Schema(description = "아이디", example = "Tester1", required = true)
    val userId: String,
    @field:Schema(description = "비밀번호", example = "Password12345!@", required = true)
    val password: String,
) {
    init {
        require(userId.isNotBlank()) { "아이디는 비어있을 수 없습니다" }
        require(password.isNotBlank()) { "비밀번호는 비어있을 수 없습니다" }
    }
}
