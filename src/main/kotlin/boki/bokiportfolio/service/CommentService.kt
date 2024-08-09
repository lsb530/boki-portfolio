package boki.bokiportfolio.service

import boki.bokiportfolio.common.ErrorCode
import boki.bokiportfolio.dto.CommentCreateRequest
import boki.bokiportfolio.dto.CommentResponse
import boki.bokiportfolio.dto.CommentUpdateRequest
import boki.bokiportfolio.exception.CustomException
import boki.bokiportfolio.repository.ArticleRepository
import boki.bokiportfolio.repository.CommentRepository
import boki.bokiportfolio.repository.UserRepository
import boki.bokiportfolio.validator.SecurityManager.getAuthenticationName
import boki.bokiportfolio.validator.SecurityManager.verifyAuthentication
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class CommentService(
    private val articleRepository: ArticleRepository,
    private val commentRepository: CommentRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun createComment(articleId: Long, commentCreateRequest: CommentCreateRequest): CommentResponse {
        verifyAuthentication()

        val currAuthUserId = getAuthenticationName().toLong()
        val article = articleRepository.findByIdOrNull(articleId)
            ?: throw CustomException(ErrorCode.NOT_FOUND_ARTICLE)

        val newComment = commentCreateRequest.toEntity(article = article, userId = currAuthUserId)

        val author = userRepository.findByIdOrNull(currAuthUserId) ?: throw CustomException(ErrorCode.NOT_FOUND_USER)
        return CommentResponse.from(commentRepository.save(newComment), author)
    }

    @Transactional
    fun updateComment(commentId: Long, commentUpdateRequest: CommentUpdateRequest): CommentResponse {
        verifyAuthentication()

        val comment = commentRepository.findByIdOrNull(commentId)
            ?: throw CustomException(ErrorCode.NOT_FOUND_COMMENT)

        val (updateContent) = commentUpdateRequest
        comment.apply {
            this.content = updateContent
        }

        val updatedComment = commentRepository.saveAndFlush(comment)
        val author = userRepository.findByIdOrNull(comment.userId)!!

        return CommentResponse.from(updatedComment, author)
    }

    @Transactional
    fun deleteComment(commentId: Long, hasToSoftDel: Boolean = true) {
        verifyAuthentication()

        val findComment = commentRepository.findByIdOrNull(commentId)
            ?: throw CustomException(ErrorCode.NOT_FOUND_COMMENT)

        if (hasToSoftDel) {
            findComment.softDelete()
        }
        else {
            commentRepository.delete(findComment)
        }
    }

    fun findAuthorId(commentId: Long): Long {
        val comment = (commentRepository.findByIdOrNull(commentId)
            ?: throw CustomException(ErrorCode.NOT_FOUND_COMMENT))
        return comment.userId
    }
}
