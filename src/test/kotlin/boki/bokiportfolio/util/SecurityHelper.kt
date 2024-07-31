package boki.bokiportfolio.util

import boki.bokiportfolio.common.Role
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken

object SecurityHelper {

    fun injectSecurityContext(username: Any? = null, role: Role) {
        SecurityContextHolder.getContext().authentication =
            PreAuthenticatedAuthenticationToken(
                username,
                null,
                listOf(SimpleGrantedAuthority(role.roleName)),
            )
    }

    fun clearSecurityContext() {
        SecurityContextHolder.clearContext()
    }
}
