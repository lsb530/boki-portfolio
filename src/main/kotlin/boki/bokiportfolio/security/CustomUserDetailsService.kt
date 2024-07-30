package boki.bokiportfolio.security

import boki.bokiportfolio.entity.User
import boki.bokiportfolio.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): CustomUserDetails {
        val user: User = userRepository.findByIdOrNull(username.toLong())
            ?: throw UsernameNotFoundException("해당 유저는 없습니다 : $username")

        return CustomUserDetails(user)
    }
    
}
