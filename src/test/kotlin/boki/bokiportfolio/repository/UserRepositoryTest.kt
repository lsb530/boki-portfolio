package boki.bokiportfolio.repository

import boki.bokiportfolio.entity.User
import boki.bokiportfolio.support.IntegrationTestSupport
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

@Transactional
class UserRepositoryTest : IntegrationTestSupport() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
    }

    @DisplayName("이메일로 회원을 중복체크 할 수 있다")
    @Test
    fun findUserByEmail() {
        // given
        val user: User = User.createUser("test@example.com", "testUser", "010-1234-5678", "홍길동", "Password1234!@")
        userRepository.save(user)

        // when
        val isUserExists = userRepository.existsUserByEmail("test@example.com")
        val allUsers = userRepository.findAll().toList()

        // then
        Assertions.assertThat(isUserExists).isTrue()
        Assertions.assertThat(allUsers).hasSize(1)
            .extracting("email", "name")
            .containsExactlyInAnyOrder(
                Assertions.tuple("test@example.com", "홍길동")
            )
    }

    @DisplayName("아이디로 회원을 중복체크 할 수 있다")
    @Test
    fun findUserByUserId() {
        // given
        val user: User = User.createUser("test@example.com", "testUser", "010-1234-5678", "홍길동", "Password1234!@")
        userRepository.save(user)

        // when
        val isUserExists = userRepository.existsUserByUserId("testUser")
        val allUsers = userRepository.findAll().toList()

        // then
        Assertions.assertThat(isUserExists).isTrue()
        Assertions.assertThat(allUsers).hasSize(1)
            .extracting("email", "name")
            .containsExactlyInAnyOrder(
                Assertions.tuple("test@example.com", "홍길동")
            )
    }

    @DisplayName("핸드폰번호로 회원을 중복체크 할 수 있다")
    @Test
    fun findUserByPhoneNumber() {
        // given
        val user: User = User.createUser("test@example.com", "testUser", "010-1234-5678", "홍길동", "Password1234!@")
        userRepository.save(user)

        // when
        val isUserExists = userRepository.existsUserByPhoneNumber("010-1234-5678")
        val allUsers = userRepository.findAll().toList()

        // then
        Assertions.assertThat(isUserExists).isTrue()
        Assertions.assertThat(allUsers).hasSize(1)
            .extracting("email", "name")
            .containsExactlyInAnyOrder(
                Assertions.tuple("test@example.com", "홍길동")
            )
    }
}

