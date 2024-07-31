package boki.bokiportfolio.support

import boki.bokiportfolio.config.SecurityConfig
import boki.bokiportfolio.controller.AuthController
import boki.bokiportfolio.repository.UserRepository
import boki.bokiportfolio.security.CustomAuthenticationEntryPoint
import boki.bokiportfolio.security.CustomUserDetailsService
import boki.bokiportfolio.security.JwtAuthFilter
import boki.bokiportfolio.security.JwtProvider
import boki.bokiportfolio.service.AuthService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(
    controllers = [
        AuthController::class,
    ],
)
@Import(SecurityConfig::class)
abstract class ControllerTestSupport {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var jwtAuthFilter: JwtAuthFilter

    @MockBean
    protected lateinit var customUserDetailsService: CustomUserDetailsService

    @MockBean
    protected lateinit var customAuthenticationEntryPoint: CustomAuthenticationEntryPoint

    @MockBean
    protected lateinit var jwtProvider: JwtProvider

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockBean
    protected lateinit var userRepository: UserRepository

    @MockBean
//    @SpyBean
    protected lateinit var authService: AuthService
}
