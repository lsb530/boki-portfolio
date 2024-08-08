package boki.bokiportfolio.validator

import boki.bokiportfolio.common.ErrorCode
import boki.bokiportfolio.common.Role
import boki.bokiportfolio.exception.CustomException
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

object SecurityManager {

    fun verifyAuthentication() {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null || authentication is AnonymousAuthenticationToken) {
            throw CustomException(ErrorCode.UNAUTHORIZED_ACCESS)
        }
    }

    fun verifyAdminOrMine(id: String) {
        val authentication = SecurityContextHolder.getContext().authentication
        val authorities = authentication.authorities.map(GrantedAuthority::getAuthority)

        if (!(authorities.contains(Role.ADMIN.roleName) or (authentication.name == id))) {
            throw CustomException(ErrorCode.FORBIDDEN_ACCESS)
        }
    }

    fun verifyAuthorities(requiredRole: Role) {
        val authentication = SecurityContextHolder.getContext().authentication
        val authorities = authentication.authorities.map(GrantedAuthority::getAuthority)

        if (!authorities.contains(requiredRole.roleName)) {
            throw CustomException(ErrorCode.FORBIDDEN_ACCESS)
        }
    }
}
