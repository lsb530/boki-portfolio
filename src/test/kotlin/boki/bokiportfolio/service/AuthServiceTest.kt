package boki.bokiportfolio.service

import boki.bokiportfolio.common.ErrorCode
import boki.bokiportfolio.common.Role
import boki.bokiportfolio.dto.LoginRequest
import boki.bokiportfolio.dto.UserRegisterRequest
import boki.bokiportfolio.dto.UserResponse
import boki.bokiportfolio.entity.User
import boki.bokiportfolio.exception.CustomException
import boki.bokiportfolio.repository.UserRepository
import boki.bokiportfolio.security.JwtProvider
import boki.bokiportfolio.security.Token
import boki.bokiportfolio.security.TokenPair
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.mockk.*
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant

class AuthServiceTest : BehaviorSpec(
    {
        val passwordEncoder = mockk<PasswordEncoder>()
        val userRepository = mockk<UserRepository>()
        val jwtProvider = mockk<JwtProvider>()
        val authService = AuthService(passwordEncoder, userRepository, jwtProvider)

        afterTest {
            clearAllMocks()
        }

        context("회원가입 요청") {

            Given("가입을 원하는 유저 정보를 받아서 처리하려는 상황에서") {
                val request = UserRegisterRequest(
                    email = "test@example.com",
                    phoneNumber = "010-1234-5678",
                    userId = "testUser",
                    name = "홍길동",
                    password = "Password1234!@",
                )
                val encodedPassword = "encodedPassword123!"

                When("❌- 이미 존재하는 email 일 때") {
                    every { userRepository.existsUserByEmail(request.email) } returns true

                    Then("DUPLICATE_USER(이메일 중복) 예외가 발생된다") {
                        val exception = shouldThrow<CustomException> {
                            authService.signup(request)
                        }
                        exception.errorCode shouldBe ErrorCode.DUPLICATE_USER
                        exception.message shouldBe "이메일 중복 - ${request.email}"

                        verify { userRepository.existsUserByEmail(request.email) }
                        verify(exactly = 0) { userRepository.existsUserByUserId(request.userId) }
                        verify(exactly = 0) { userRepository.existsUserByPhoneNumber(request.phoneNumber) }
                        verify(exactly = 0) { userRepository.save(any()) }
                    }
                }

                When("❌- 이미 존재하는 userId 일 때") {
                    every { userRepository.existsUserByEmail(request.email) } returns false
                    every { userRepository.existsUserByUserId(request.userId) } returns true

                    Then("DUPLICATE_USER(아이디 중복) 예외가 발생된다") {
                        val exception = shouldThrow<CustomException> {
                            authService.signup(request)
                        }
                        exception.errorCode shouldBe ErrorCode.DUPLICATE_USER
                        exception.message shouldBe "아이디 중복 - ${request.userId}"

                        verify { userRepository.existsUserByEmail(request.email) }
                        verify { userRepository.existsUserByUserId(request.userId) }
                        verify(exactly = 0) { userRepository.existsUserByPhoneNumber(request.phoneNumber) }
                        verify(exactly = 0) { userRepository.save(any()) }
                    }
                }

                When("❌- 이미 존재하는 phone number 일 때") {
                    every { userRepository.existsUserByEmail(request.email) } returns false
                    every { userRepository.existsUserByUserId(request.userId) } returns false
                    every { userRepository.existsUserByPhoneNumber(request.phoneNumber) } returns true

                    Then("DUPLICATE_USER(핸드폰 번호 중복) 예외가 발생된다") {
                        val exception = shouldThrow<CustomException> {
                            authService.signup(request)
                        }
                        exception.errorCode shouldBe ErrorCode.DUPLICATE_USER
                        exception.message shouldBe "핸드폰 번호 중복 - ${request.phoneNumber}"

                        verify { userRepository.existsUserByEmail(request.email) }
                        verify { userRepository.existsUserByUserId(request.userId) }
                        verify { userRepository.existsUserByPhoneNumber(request.phoneNumber) }
                        verify(exactly = 0) { userRepository.save(any()) }
                    }
                }

                When("✅- 모든 정보가 DB에서 중복되지 않고, 유효할 때") {
                    every { userRepository.existsUserByEmail(request.email) } returns false
                    every { userRepository.existsUserByUserId(request.userId) } returns false
                    every { userRepository.existsUserByPhoneNumber(request.phoneNumber) } returns false
                    every { passwordEncoder.encode(request.password) } returns encodedPassword
                    val savedUser = User(
                        id = 1L,
                        email = request.email,
                        phoneNumber = request.phoneNumber,
                        userId = request.userId,
                        name = request.name,
                        password = encodedPassword,
                        role = Role.USER,
                    )
                    every { userRepository.save(any()) } returns savedUser

                    Then("User 데이터가 추가되고, UserResponse 반환") {
                        val response = authService.signup(request)
                        response shouldBe UserResponse.from(savedUser)

                        verify { userRepository.existsUserByEmail(request.email) }
                        verify { userRepository.existsUserByUserId(request.userId) }
                        verify { userRepository.existsUserByPhoneNumber(request.phoneNumber) }
                        verify { passwordEncoder.encode(request.password) }
                        verify { userRepository.save(any()) }
                    }
                }
            }
        }

        context("로그인 요청") {

            Given("로그인을 원하는 정보를 받아서 처리하려는 상황에서") {
                val request = LoginRequest(
                    userId = "testUser",
                    password = "Password1234!@",
                )

                val findUser = User(
                    id = 1L,
                    email = "test@example.com",
                    phoneNumber = "010-1234-5678",
                    userId = "testUser",
                    name = "홍길동",
                    password = "Password1234!@",
                    role = Role.USER,
                )

                When("❌- 가입된 정보가 없을 때") {
                    every { userRepository.findUserByUserId(request.userId) } throws CustomException(ErrorCode.NOT_FOUND_USER)

                    Then("NOT_FOUND_USER(등록된 회원 정보가 없습니다) 예외가 발생된다") {
                        val exception = shouldThrow<CustomException> {
                            authService.login(request)
                        }
                        exception.errorCode shouldBe ErrorCode.NOT_FOUND_USER
                        exception.message shouldBe "등록된 회원 정보가 없습니다"

                        verify { userRepository.findUserByUserId(request.userId) }
                        verify(exactly = 0) { jwtProvider.generatePairToken(any()) }
                    }
                }

                When("❌- 비밀번호가 틀렸을 때") {
                    every { userRepository.findUserByUserId(request.userId) } returns findUser
                    every { passwordEncoder.matches(any(), any()) } returns false

                    Then("FAILED_LOGIN(로그인에 실패하였습니다) 예외가 발생된다") {
                        val exception = shouldThrow<CustomException> {
                            authService.login(request)
                        }
                        exception.errorCode shouldBe ErrorCode.FAILED_LOGIN
                        exception.message shouldBe "로그인에 실패하였습니다"

                        verify { userRepository.findUserByUserId(request.userId) }
                        verify { passwordEncoder.matches(any(), any()) }
                        verify(exactly = 0) { jwtProvider.generatePairToken(any()) }
                    }
                }

                When("✅- 로그인 요청 정보(id, password)가 유효할 때") {
                    val response = TokenPair(
                        accessToken = Token(value = "accessToken", expiredAt = Instant.now()),
                        refreshToken = Token(value = "refreshToken", expiredAt = Instant.now()),
                    )

                    every { userRepository.findUserByUserId(request.userId) } returns findUser
                    every { passwordEncoder.matches(any(), any()) } returns true
                    every { jwtProvider.generatePairToken(any()) } returns response

                    Then("로그인 성공") {
                        response shouldBe authService.login(request)

                        verify { userRepository.findUserByUserId(request.userId) }
                        verify { passwordEncoder.matches(any(), any()) }
                        verify { jwtProvider.generatePairToken(any()) }
                    }
                }
            }
        }
    },
)
