package boki.bokiportfolio.dto

import java.util.regex.Pattern

data class UserRegisterRequest(
    val email: String,
    val phoneNumber: String,
    val userId: String,
    val name: String,
    val password: String,
) {
    init {
        require(isValidEmail(email)) { "잘못된 이메일 형식입니다" }
        require(isValidPhoneNumber(phoneNumber)) { "잘못된 핸드폰 전화번호 형식입니다" }
        require(isValidUserId(userId)) { "잘못된 아이디 형식입니다" }
        require(isValidName(name)) { "잘못된 이름 형식입니다" }
        require(isValidPassword(password)) { "잘못된 비밀번호 형식입니다" }
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
