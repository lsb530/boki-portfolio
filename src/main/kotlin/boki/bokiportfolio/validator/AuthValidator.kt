package boki.bokiportfolio.validator

object AuthValidator {

    const val INVALID_LOGIN_USER_ID_MSG = "아이디는 비어있을 수 없습니다"
    const val INVALID_LOGIN_PASSWORD_ID_MSG = "비밀번호는 비어있을 수 없습니다"

    /**
     * Checks if the given user ID is valid for authentication.
     *
     * @param userId The user ID to be validated.
     * @return true if the user ID is not blank, false otherwise.
     *
     * ```
     *  isValidAuthUserId("")) // false
     *  isValidAuthUserId(null)) // false
     *  isValidAuthUserId("  ")) // false
     *
     *  isValidAuthUserId("testerId")) // true
     * ```
     */
    fun isValidAuthUserId(userId: String): Boolean {
        return userId.isNotBlank()
    }

    /**
     * Checks if the given password is valid for authentication.
     *
     * @param password The password to be validated.
     * @return true if the password is valid, false otherwise.
     *
     * ```
     *  isValidAuthPassword("")) // false
     *  isValidAuthPassword(null)) // false
     *  isValidAuthPassword("  ")) // false
     *
     *  isValidAuthPassword("Password20!@")) // true
     * ```
     */
    fun isValidAuthPassword(password: String): Boolean {
        return password.isNotBlank()
    }
}
