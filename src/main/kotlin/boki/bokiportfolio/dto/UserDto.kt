package boki.bokiportfolio.dto

import boki.bokiportfolio.entity.User
import io.swagger.v3.oas.annotations.media.Schema

@Schema(name = "유저 정보 응답", description = "유저 관련 응답 정보")
data class UserResponse(
    @field:Schema(description = "DB id")
    val id: Long,

    @field:Schema(description = "아이디")
    val userId: String,

    @field:Schema(description = "이메일")
    val email: String,

    @field:Schema(description = "핸드폰 번호")
    val phoneNumber: String,

    @field:Schema(description = "이름")
    val name: String,
) {
    companion object {
        fun from(user: User): UserResponse {
            return UserResponse(
                id = user.id!!,
                userId = user.userId,
                email = user.email,
                phoneNumber = user.phoneNumber,
                name = user.name
            )
        }
    }
}
