package boki.bokiportfolio.service

import boki.bokiportfolio.common.ErrorCode
import boki.bokiportfolio.dto.LoginRequest
import boki.bokiportfolio.dto.UserRegisterRequest
import boki.bokiportfolio.dto.UserResponse
import boki.bokiportfolio.exception.CustomException
import boki.bokiportfolio.repository.UserRepository
import boki.bokiportfolio.security.JwtProvider
import boki.bokiportfolio.security.TokenPair
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class AuthService(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
) {
    @Transactional
    fun signup(userRegisterRequest: UserRegisterRequest): UserResponse {
        if (userRepository.existsUserByEmail(userRegisterRequest.email)) {
            throw CustomException(ErrorCode.DUPLICATE_USER, "이메일 중복 - ${userRegisterRequest.email}")
        }
        else if (userRepository.existsUserByUserId(userRegisterRequest.userId)) {
            throw CustomException(ErrorCode.DUPLICATE_USER, "아이디 중복 - ${userRegisterRequest.userId}")
        }
        else if (userRepository.existsUserByPhoneNumber(userRegisterRequest.phoneNumber)) {
            throw CustomException(ErrorCode.DUPLICATE_USER, "핸드폰 번호 중복 - ${userRegisterRequest.phoneNumber}")
        }
        else {
            val newUserEntity = userRegisterRequest.toEntity(passwordEncoder)
            val newUser = userRepository.save(newUserEntity)
            return UserResponse.from(newUser)
        }
    }

    fun login(loginRequest: LoginRequest): TokenPair {
        val findUser = userRepository.findUserByUserId(loginRequest.userId)
            ?: throw CustomException(ErrorCode.NOT_FOUND_USER)
        if (!passwordEncoder.matches(loginRequest.password, findUser.password)) {
            throw CustomException(ErrorCode.FAILED_LOGIN)
        }
        return jwtProvider.generatePairToken(findUser.id!!)
    }

    fun me(id: Long): UserResponse {
        val findUser = userRepository.findByIdOrNull(id) ?: throw CustomException(ErrorCode.NOT_FOUND_USER)
        return UserResponse.from(findUser)
    }
}
