package boki.bokiportfolio.controller.doc

import boki.bokiportfolio.dto.LoginRequest
import boki.bokiportfolio.dto.UserRegisterRequest
import boki.bokiportfolio.dto.UserResponse
import boki.bokiportfolio.security.CustomUserDetails
import boki.bokiportfolio.security.TokenPair
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal

@Tag(name = "A. 인증 API")
interface AuthApiSpec {

    @Operation(summary = "회원가입")
    fun signup(userRegisterRequest: UserRegisterRequest): ResponseEntity<UserResponse>

    @Operation(summary = "로그인")
    fun login(loginRequest: LoginRequest): ResponseEntity<TokenPair>

    @Operation(summary = "내 정보 조회", security = [SecurityRequirement(name = "accessToken")])
    fun me(@AuthenticationPrincipal userDetails: CustomUserDetails): ResponseEntity<UserResponse>

}
