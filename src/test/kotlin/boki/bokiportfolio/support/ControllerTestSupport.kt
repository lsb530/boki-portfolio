package boki.bokiportfolio.support

import boki.bokiportfolio.config.SecurityConfig
import boki.bokiportfolio.controller.ArticleController
import boki.bokiportfolio.controller.AuthController
import boki.bokiportfolio.security.CustomAuthenticationEntryPoint
import boki.bokiportfolio.security.CustomUserDetailsService
import boki.bokiportfolio.security.JwtAuthFilter
import boki.bokiportfolio.security.JwtProvider
import boki.bokiportfolio.service.ArticleService
import boki.bokiportfolio.service.AuthService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext

@WebMvcTest(
    controllers = [
        AuthController::class,
        ArticleController::class,
    ],
)
@Import(SecurityConfig::class)
@EnableMethodSecurity(proxyTargetClass = true, prePostEnabled = true)
abstract class ControllerTestSupport {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @Autowired
    protected lateinit var jwtAuthFilter: JwtAuthFilter

    @Autowired
    protected lateinit var context: WebApplicationContext

    @MockBean
    protected lateinit var customUserDetailsService: CustomUserDetailsService

    @MockBean
    protected lateinit var customAuthenticationEntryPoint: CustomAuthenticationEntryPoint

    @MockBean
    protected lateinit var jwtProvider: JwtProvider

    @MockBean
//    @SpyBean
    protected lateinit var authService: AuthService

    @MockBean
    protected lateinit var articleService: ArticleService

}
