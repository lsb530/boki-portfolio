package boki.bokiportfolio.dto

import boki.bokiportfolio.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.regex.Pattern

@Schema(name = "회원 가입 요청", description = "회원 가입 요청 정보")
data class UserRegisterRequest(
    @field:Schema(description = "이메일", example = "test1@test.com", required = true)
    val email: String,

    @field:Schema(description = "핸드폰 번호", example = "010-1234-5678", required = true)
    val phoneNumber: String,

    @field:Schema(description = "아이디(대/소문자 영어, 숫자)", example = "tester", required = true)
    val userId: String,

    @field:Schema(description = "이름(한글)", example = "홍길동", required = true)
    val name: String,

    @field:Schema(description = "비밀번호(대문자/소문자/숫자/특수문자[2개] 포함 7글자 이상)", example = "Password2024!@", required = true)
    val password: String,
) {
    init {
        require(isValidEmail(email)) { "잘못된 이메일 형식입니다" }
        require(isValidPhoneNumber(phoneNumber)) { "잘못된 핸드폰 전화번호 형식입니다" }
        require(isValidUserId(userId)) { "잘못된 아이디 형식입니다" }
        require(isValidName(name)) { "잘못된 이름 형식입니다" }
        require(isValidPassword(password)) { "잘못된 비밀번호 형식입니다" }
    }

    fun toEntity(passwordEncoder: PasswordEncoder): User {
        val encodedPassword = passwordEncoder.encode(password)
        return User.createUser(email, userId, phoneNumber, name, encodedPassword)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
        return Pattern.compile(emailRegex).matcher(email).matches()
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        val phoneRegex = "^\\d{3}-\\d{3,4}-\\d{4}$"
        return Pattern.compile(phoneRegex).matcher(phone).matches()
    }

    private fun isValidUserId(userId: String): Boolean {
        if (userId.contains("admin"))
            return false
        val userIdRegex = "^(?=.*[a-zA-Z])[a-zA-Z0-9]+$"
        return Pattern.compile(userIdRegex).matcher(userId).matches()
    }

    private fun isValidName(name: String): Boolean {
        val userIdRegex = "^[가-힣]+$"
        return Pattern.compile(userIdRegex).matcher(name).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!#%^&*].*[@\$!#%^&*])[A-Za-z\\d@\$!#%^&*]{7,}$"
        return Pattern.compile(passwordRegex).matcher(password).matches()
    }
}

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
