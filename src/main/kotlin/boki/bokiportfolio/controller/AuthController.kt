package boki.bokiportfolio.controller

import boki.bokiportfolio.controller.doc.AuthApiSpec
import boki.bokiportfolio.dto.UserRegisterRequest
import boki.bokiportfolio.dto.UserResponse
import boki.bokiportfolio.dto.LoginRequest
import boki.bokiportfolio.security.CustomUserDetails
import boki.bokiportfolio.security.TokenPair
import boki.bokiportfolio.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RequestMapping("/api/auth")
@RestController
class AuthController(
    private val authService: AuthService,
) : AuthApiSpec {

    @PostMapping("/signup")
    override fun signup(@RequestBody userRegisterRequest: UserRegisterRequest): ResponseEntity<UserResponse> {
        val newUserResponse = authService.signup(userRegisterRequest)
        val location = URI.create("/api/users/${newUserResponse.id}")
        return ResponseEntity.created(location).body(newUserResponse)
    }

    @PostMapping("/login")
    override fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<TokenPair> {
        return ResponseEntity.ok().body(authService.login(loginRequest))
    }

    @GetMapping("/me")
    override fun me(@AuthenticationPrincipal userDetails: CustomUserDetails): ResponseEntity<UserResponse> {
        return ResponseEntity.ok().body(authService.me(userDetails.username.toLong()))
    }
}
