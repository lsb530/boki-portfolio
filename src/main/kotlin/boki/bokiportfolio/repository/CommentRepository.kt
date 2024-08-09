package boki.bokiportfolio.repository

import boki.bokiportfolio.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository  : JpaRepository<Comment, Long>
