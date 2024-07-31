package boki.bokiportfolio.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtProvider: JwtProvider,
    private val userDetailsService: CustomUserDetailsService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            val token = resolveToken(request)
            if (token != null) {
                jwtProvider.validateToken(token)
                val userId = jwtProvider.getUserIdFromToken(token)
                val userDetails = userDetailsService.loadUserByUsername(userId.toString())
                val authentication: Authentication = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities,
                )
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            request.setAttribute("exception", e)
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        }
        else null
    }
}
