package boki.bokiportfolio.security

import boki.bokiportfolio.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(private val user: User): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        val roleName = user.role.roleName
        return mutableListOf(SimpleGrantedAuthority(roleName))
    }

    override fun getPassword(): String {
        return ""
//        return user.password
    }

    override fun getUsername(): String {
        return user.id.toString()
    }

}

