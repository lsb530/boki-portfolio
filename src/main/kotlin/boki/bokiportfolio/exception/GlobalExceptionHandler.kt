package boki.bokiportfolio.exception

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    private fun createErrorResponse(
        type: String,
        status: HttpStatus,
        ex: Exception,
        request: HttpServletRequest,
        customMsg: String? = null,
    ): ResponseEntity<ErrorResponse> {
        log.error(
            """==== 🥑Error occurred 🥑====
           Exception: ${ex::class.java},
           Type: ${type},
           Path: ${request.requestURI},
           Method: ${request.method},
           Message: ${customMsg ?: ex.message},
           trace: ${ex.stackTrace[0]}
           """.trimIndent(),
        )
        val errorResponse = ErrorResponse(
            type = type,
            status = status.value(),
            message = customMsg ?: ex.message,
            path = request.requestURI,
            method = request.method,
        )
        return ResponseEntity(errorResponse, status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException, request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage ?: "Not Exception Message"
        }
        val customMsg = errors.entries.joinToString(separator = ", ") {
            "${it.key}=${it.value}"
        }
        return createErrorResponse(
            type = "Invalid_Request",
            status = HttpStatus.BAD_REQUEST,
            ex = ex,
            request = request,
            customMsg = customMsg,
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        ex: HttpMessageNotReadableException, request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        var customMsg = "Message Not Readable"
        ex.message?.let {
            customMsg = it.substring(it.indexOf("problem")).replace("problem: ", "")
        }
        return createErrorResponse(
            type = "Invalid_Request",
            status = HttpStatus.BAD_REQUEST,
            ex = ex,
            request = request,
            customMsg = customMsg,
        )
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsException(
        ex: BadCredentialsException, request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return createErrorResponse(
            type = "Bad_Credentials",
            status = HttpStatus.UNAUTHORIZED,
            ex = ex,
            request = request,
            customMsg = "아이디 혹은 비밀번호를 다시 확인하세요.",
        )
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        ex: AccessDeniedException, request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return createErrorResponse(
            type = "Access_Denied",
            status = HttpStatus.FORBIDDEN,
            ex = ex,
            request = request,
            customMsg = "접근 권한이 없습니다",
        )
    }

    @ExceptionHandler(value = [RuntimeException::class])
    fun handleRuntimeException(
        ex: RuntimeException, request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return createErrorResponse(
            type = "Server_Error",
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            ex = ex,
            request = request,
        )
    }

    @ExceptionHandler(value = [CustomException::class])
    fun handleCustomException(
        ex: CustomException, request: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return createErrorResponse(
            type = ex.errorCode.name,
            status = ex.errorCode.status,
            ex = ex,
            request = request,
        )
    }

}
