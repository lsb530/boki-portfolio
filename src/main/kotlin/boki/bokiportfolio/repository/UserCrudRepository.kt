package boki.bokiportfolio.repository

import boki.bokiportfolio.entity.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserCrudRepository: CrudRepository<User, Long>
