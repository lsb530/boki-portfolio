package boki.bokiportfolio.service

import boki.bokiportfolio.common.ErrorCode
import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
import boki.bokiportfolio.exception.CustomException
import boki.bokiportfolio.repository.ArticleRepository
import boki.bokiportfolio.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class ArticleService(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
) {
    @Transactional
    fun createArticle(articleCreateRequest: ArticleCreateRequest): ArticleResponse {
        val userId = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByIdOrNull(userId.toLong()) ?: throw CustomException(ErrorCode.NOT_FOUND_USER)
        val newArticle = articleRepository.save(articleCreateRequest.toEntity(user))
        return ArticleResponse.from(newArticle)
    }

    @Transactional
    fun updateArticle(): ArticleResponse {
        val article = articleRepository.findById(1L).get()
        article.apply {
            this.content = "수정된 내용"
        }
        val updatedArticle = articleRepository.saveAndFlush(article)
        return ArticleResponse.from(updatedArticle)
    }
}
