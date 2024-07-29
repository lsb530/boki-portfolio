package boki.bokiportfolio.exception

import boki.bokiportfolio.common.ErrorCode

data class CustomException(
    val errorCode: ErrorCode,
    override val message: String? = errorCode.message,
) : RuntimeException(getErrorMessage(errorCode, message)) {
    companion object {
        private fun getErrorMessage(errorCode: ErrorCode, customMessage: String?): String {
            return customMessage ?: errorCode.message
        }
    }
}
