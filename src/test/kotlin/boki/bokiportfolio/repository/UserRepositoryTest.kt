package boki.bokiportfolio.repository

import boki.bokiportfolio.entity.User
import boki.bokiportfolio.support.IntegrationTestSupport
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource

@Transactional
@TestPropertySource(properties = ["jwt-secret=test-secret-key"])
class UserRepositoryTest : IntegrationTestSupport() {

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeAll
    fun setup() {
        userRepository.deleteAll()
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
    }

    @DisplayName("이메일로 회원을 중복체크 할 수 있다")
    @Test
    fun exist_user_by_email() {
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
    fun exist_user_by_user_id() {
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
    fun exist_user_by_phone_number() {
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

    @DisplayName("아이디로 회원을 찾을 수 있다")
    @Test
    fun find_user_by_user_id() {
        // given
        val user: User = User.createUser("test@example.com", "testUser", "010-1234-5678", "홍길동", "Password1234!@")
        userRepository.save(user)

        // when
        val findUser = userRepository.findUserByUserId("testUser")

        // then
        Assertions.assertThat(findUser?.name).isEqualTo(user.name)
    }
}

