package boki.bokiportfolio.repository

import boki.bokiportfolio.entity.User
import org.springframework.data.repository.CrudRepository

interface UserCrudRepository: CrudRepository<User, Long>
