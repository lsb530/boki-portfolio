package boki.bokiportfolio.dto

import boki.bokiportfolio.entity.User
import boki.bokiportfolio.validator.AuthValidator.INVALID_LOGIN_PASSWORD_ID_MSG
import boki.bokiportfolio.validator.AuthValidator.INVALID_LOGIN_USER_ID_MSG
import boki.bokiportfolio.validator.AuthValidator.isValidAuthPassword
import boki.bokiportfolio.validator.AuthValidator.isValidAuthUserId
import boki.bokiportfolio.validator.RegisterValidator.INVALID_REGISTER_EMAIL_MSG
import boki.bokiportfolio.validator.RegisterValidator.INVALID_REGISTER_NAME_MSG
import boki.bokiportfolio.validator.RegisterValidator.INVALID_REGISTER_PASSWORD_MSG
import boki.bokiportfolio.validator.RegisterValidator.INVALID_REGISTER_PHONE_NUMBER_MSG
import boki.bokiportfolio.validator.RegisterValidator.INVALID_REGISTER_USER_ID_MSG
import boki.bokiportfolio.validator.RegisterValidator.isValidEmail
import boki.bokiportfolio.validator.RegisterValidator.isValidName
import boki.bokiportfolio.validator.RegisterValidator.isValidPassword
import boki.bokiportfolio.validator.RegisterValidator.isValidPhoneNumber
import boki.bokiportfolio.validator.RegisterValidator.isValidUserId
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.security.crypto.password.PasswordEncoder

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
        require(isValidEmail(email)) { INVALID_REGISTER_EMAIL_MSG }
        require(isValidPhoneNumber(phoneNumber)) { INVALID_REGISTER_PHONE_NUMBER_MSG }
        require(isValidUserId(userId)) { INVALID_REGISTER_USER_ID_MSG }
        require(isValidName(name)) { INVALID_REGISTER_NAME_MSG }
        require(isValidPassword(password)) { INVALID_REGISTER_PASSWORD_MSG }
    }

    fun toEntity(passwordEncoder: PasswordEncoder): User {
        val encodedPassword = passwordEncoder.encode(password)
        return User.createUser(email, userId, phoneNumber, name, encodedPassword)
    }
}

@Schema(name = "로그인 요청", description = "로그인 요청 정보")
data class LoginRequest(
    @field:Schema(description = "아이디", example = "Tester1", required = true)
    val userId: String,
    @field:Schema(description = "비밀번호", example = "Password12345!@", required = true)
    val password: String,
) {
    init {
        require(isValidAuthUserId(userId)) { INVALID_LOGIN_USER_ID_MSG}
        require(isValidAuthPassword(password)) { INVALID_LOGIN_PASSWORD_ID_MSG}
    }
}
