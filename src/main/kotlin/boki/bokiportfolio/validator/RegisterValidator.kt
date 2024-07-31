package boki.bokiportfolio.validator

import java.util.regex.Pattern

object RegisterValidator {

    const val INVALID_REGISTER_EMAIL_MSG = "잘못된 이메일 형식입니다"
    const val INVALID_REGISTER_PHONE_NUMBER_MSG = "잘못된 핸드폰 전화번호 형식입니다"
    const val INVALID_REGISTER_USER_ID_MSG = "잘못된 아이디 형식입니다"
    const val INVALID_REGISTER_NAME_MSG = "잘못된 이름 형식입니다"
    const val INVALID_REGISTER_PASSWORD_MSG = "잘못된 비밀번호 형식입니다"

    /**
     * Validates if a given email address is valid.
     *
     * @param email The email address to be validated.
     * @return True if the email address is valid, false otherwise.
     *
     * ```
     *  isValidEmail("123-#.cc")) // false
     *
     *  isValidEmail("test1@test.com")) // true
     * ```
     */
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
        return Pattern.compile(emailRegex).matcher(email).matches()
    }

    /**
     * Validates if a given phone number is valid.
     *
     * @param phone The phone number to be validated.
     * @return True if the phone number is valid, false otherwise.
     *
     * ```
     *  isValidPhoneNumber("01-1234-5678")) // false
     *  isValidPhoneNumber("010-12-5678")) // false
     *
     *  isValidPhoneNumber("010-123-5678")) // true
     *  isValidPhoneNumber("010-1234-5678")) // true
     * ```
     */
    fun isValidPhoneNumber(phone: String): Boolean {
        val phoneRegex = "^\\d{3}-\\d{3,4}-\\d{4}$"
        return Pattern.compile(phoneRegex).matcher(phone).matches()
    }

    /**
     * Validates if a given user ID is valid.
     *
     * @param userId The user ID to be validated.
     * @return True if the user ID is valid, false otherwise.
     *
     * ```
     *  isValidUserId("admin")) // false
     *  isValidUserId("")) // false
     *  isValidUserId("3")) // false
     *  isValidUserId("!0")) // false
     *
     *  isValidUserId("tester")) // true
     *  isValidUserId("tester")) // true
     *  isValidUserId("Tester1234")) // true
     * ```
     */
    fun isValidUserId(userId: String): Boolean {
        if (userId.contains("admin"))
            return false
        val userIdRegex = "^(?=.*[a-zA-Z])[a-zA-Z0-9]+$"
        return Pattern.compile(userIdRegex).matcher(userId).matches()
    }

    /**
     * Validates if a given name is valid.
     *
     * @param name The name to be validated.
     * @return True if the name is valid, false otherwise.
     *
     * ```
     *  isValidName("Lebron James")) // false
     *  isValidName("헬로우!!!")) // false
     *  isValidName("!!wow")) // false
     *
     *  isValidName("홍길동")) // true
     *  isValidName("이승복")) // true
     * ```
     */
    fun isValidName(name: String): Boolean {
        val userIdRegex = "^[가-힣]+$"
        return Pattern.compile(userIdRegex).matcher(name).matches()
    }

    /**
     * Validates if a given password is valid.
     *
     * @param password The password to be validated.
     * @return True if the password is valid, false otherwise.
     *
     * ```
     *  isValidPassword("lowercase")) // false
     *  isValidPassword("UPPERCASE")) // false
     *  isValidPassword("Test12!")) // false
     *
     *  isValidPassword("Test1!@")) // ture
     *  isValidPassword("Test1234!@")) // true
     *  isValidPassword("bananA0#@")) // true
     * ```
     */
    fun isValidPassword(password: String): Boolean {
        val passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@\$!#%^&*].*[@\$!#%^&*])[A-Za-z\\d@\$!#%^&*]{7,}$"
        return Pattern.compile(passwordRegex).matcher(password).matches()
    }
}
