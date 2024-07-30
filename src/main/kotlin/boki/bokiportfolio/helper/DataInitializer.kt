package boki.bokiportfolio.helper

import boki.bokiportfolio.entity.User
import boki.bokiportfolio.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.Repository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class DataInitializer(
    private val passwordEncoder: PasswordEncoder,
    private val userRepository: UserRepository,
    private val applicationContext: ApplicationContext,
) : ApplicationRunner {

    // AOP 프록시 기반의 트랜잭션 분리(self-invocation)
    override fun run(args: ApplicationArguments?) {
        val self = applicationContext.getBean(DataInitializer::class.java)
        self.initUser()
        Thread.sleep(5000L)
        self.updateUser()
        Thread.sleep(5000L)
        self.softDeleteUser()
        Thread.sleep(5000L)
        self.deleteUser()
    }

    @Transactional(readOnly = true)
    fun isInitialState(repository: Repository<*, Long>): Boolean {
        return when (repository) {
            is JpaRepository<*, *> -> repository.count() == 0L
            is CrudRepository<*, *> -> repository.count() == 0L
            else -> false
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun initUser() {
        if (isInitialState(userRepository)) {
            val user1 = User.createUser(
                "test1@test.com",
                "Tester1",
                "010-1234-5678",
                "테스터1",
                passwordEncoder.encode("Test11111!@"))
            userRepository.saveAll(listOf(user1))
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun updateUser() {
        val findUser = userRepository.findByIdOrNull(1L)
        findUser?.let {
            it.name = "updateUser1"
            userRepository.save(it)
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun softDeleteUser() {
        val findUser = userRepository.findByIdOrNull(1L)
        findUser?.let {
            it.softDelete()
            userRepository.save(it)
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun deleteUser() {
        val findUser = userRepository.findByIdOrNull(1L)
        findUser?.let {
            userRepository.delete(it)
        }
    }
}
