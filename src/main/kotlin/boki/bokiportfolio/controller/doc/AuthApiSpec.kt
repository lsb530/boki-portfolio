package boki.bokiportfolio.controller.doc

import boki.bokiportfolio.dto.LoginRequest
import boki.bokiportfolio.dto.UserRegisterRequest
import boki.bokiportfolio.dto.UserResponse
import boki.bokiportfolio.security.TokenPair
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity

@Tag(name = "A. 인증 API")
interface AuthApiSpec {

    @Operation(summary = "회원가입")
    fun signup(userRegisterRequest: UserRegisterRequest): ResponseEntity<UserResponse>

    @Operation(summary = "로그인")
    fun login(loginRequest: LoginRequest): ResponseEntity<TokenPair>

}
