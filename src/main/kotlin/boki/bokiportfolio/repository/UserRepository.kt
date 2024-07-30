package boki.bokiportfolio.repository

import boki.bokiportfolio.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CrudRepository<User, Long> {
    fun existsUserByEmail(email: String): Boolean
    fun existsUserByUserId(userId: String): Boolean
    fun existsUserByPhoneNumber(phoneNumber: String): Boolean
}
