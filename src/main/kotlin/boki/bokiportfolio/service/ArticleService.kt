package boki.bokiportfolio.service

import boki.bokiportfolio.common.ErrorCode
import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
import boki.bokiportfolio.dto.ArticleUpdateRequest
import boki.bokiportfolio.exception.CustomException
import boki.bokiportfolio.repository.ArticleRepository
import boki.bokiportfolio.repository.UserRepository
import boki.bokiportfolio.validator.SecurityManager.verifyAuthentication
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Transactional(readOnly = true)
@Service
class ArticleService(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
) {
    @Transactional
    fun createArticle(articleCreateRequest: ArticleCreateRequest): ArticleResponse {
        verifyAuthentication()

        val userId = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByIdOrNull(userId.toLong()) ?: throw CustomException(ErrorCode.NOT_FOUND_USER)
        val newArticle = articleRepository.save(articleCreateRequest.toEntity(user))
        return ArticleResponse.from(newArticle)
    }

    @Transactional
    fun updateArticle(articleUpdateRequest: ArticleUpdateRequest): ArticleResponse {
        verifyAuthentication()

        val (articleId, updateTitle, updateContent) = articleUpdateRequest
        val article = articleRepository.findByIdOrNull(articleId)
            ?: throw CustomException(ErrorCode.NOT_FOUND_ARTICLE)

        verifyEditableArticle(
            today = LocalDateTime.now().toLocalDate(),
            editExpiryDate = article.createdAt.toLocalDate().plusDays(9),
        )

        article.apply {
            updateTitle?.let { this.title = it }
            updateContent?.let { this.content = it }
        }

        val updatedArticle = articleRepository.saveAndFlush(article)

        return ArticleResponse.from(
            updatedArticle,
            hasToWarnEditAlarm(
                today = LocalDateTime.now().toLocalDate(),
                editWarningDate = article.createdAt.toLocalDate().plusDays(8),
            ),
        )
    }

    fun verifyEditableArticle(today: LocalDate, editExpiryDate: LocalDate) {
        if (today.isEqual(editExpiryDate) or today.isAfter(editExpiryDate)) {
            throw CustomException(ErrorCode.INVALID_EDIT_ARTICLE)
        }
    }

    fun hasToWarnEditAlarm(today: LocalDate, editWarningDate: LocalDate): Boolean {
        return today.isEqual(editWarningDate)
    }
}
