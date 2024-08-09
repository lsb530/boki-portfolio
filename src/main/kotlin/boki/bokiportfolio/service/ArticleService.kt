package boki.bokiportfolio.service

import boki.bokiportfolio.common.ErrorCode
import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
import boki.bokiportfolio.dto.ArticleUpdateRequest
import boki.bokiportfolio.exception.CustomException
import boki.bokiportfolio.repository.ArticleRepository
import boki.bokiportfolio.repository.UserRepository
import boki.bokiportfolio.validator.SecurityManager.getAuthenticationName
import boki.bokiportfolio.validator.SecurityManager.verifyAuthentication
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Transactional(readOnly = true)
@Service
class ArticleService(
    private val userRepository: UserRepository,
    private val articleRepository: ArticleRepository,
    private val redisService: RedisService,
) {
    @Transactional
    fun createArticle(articleCreateRequest: ArticleCreateRequest): ArticleResponse {
        verifyAuthentication()

        val userId = SecurityContextHolder.getContext().authentication.name
        val user = userRepository.findByIdOrNull(userId.toLong()) ?: throw CustomException(ErrorCode.NOT_FOUND_USER)
        val newArticle = articleRepository.save(articleCreateRequest.toEntity(user))
        val dueDate = calculateDueDate(LocalDateTime.now().toLocalDate(), newArticle.createdAt.toLocalDate().plusDays(9))
        return ArticleResponse.from(article = newArticle, dueDate = dueDate)
    }

    @Transactional
    fun updateAttachImgFileName(articleId: Long, filename: String) {
        val findArticle = articleRepository.findByIdOrNull(articleId)
            ?: throw CustomException(ErrorCode.NOT_FOUND_ARTICLE)
        findArticle.apply {
            this.imgFileName = filename
        }
        articleRepository.save(findArticle)
    }

    // Criteria ✅
    fun getArticles(title: String? = null, createdAtSortDirection: Sort.Direction): List<ArticleResponse> {
        verifyAuthentication()

        val findArticles = articleRepository.findArticlesContainsTitleAndCreatedAtSortDirection(title, createdAtSortDirection)

        return findArticles.map {
            val dueDate = calculateDueDate(LocalDateTime.now().toLocalDate(), it.createdAt.toLocalDate().plusDays(9))
            return@map ArticleResponse.from(article = it, dueDate = dueDate)
        }
    }

    @Transactional
    fun likeArticle(articleId: Long): ArticleResponse {
        verifyAuthentication()

        val findArticle = articleRepository.findByIdOrNull(articleId)
            ?: throw CustomException(ErrorCode.NOT_FOUND_ARTICLE)

        val currentAuthUserId = getAuthenticationName().toLong()

        if (findArticle.user.id == currentAuthUserId) {
            throw CustomException(ErrorCode.INVALID_LIKE_REQUEST)
        }
        else {
            findArticle.likeArticle(currentAuthUserId)
        }

        return getArticle(articleId, false)
    }

    @Transactional
    fun cancelLikeArticle(articleId: Long): ArticleResponse {
        verifyAuthentication()

        val findArticle = articleRepository.findByIdOrNull(articleId)
            ?: throw CustomException(ErrorCode.NOT_FOUND_ARTICLE)

        findArticle.cancelLikeArticle(getAuthenticationName().toLong())

        return getArticle(articleId, false)
    }

    @Transactional
    fun getArticle(articleId: Long, requireAddViewCnt: Boolean? = true): ArticleResponse {
        verifyAuthentication()

        val findArticle = articleRepository.findByIdOrNull(articleId)
            ?: throw CustomException(ErrorCode.NOT_FOUND_ARTICLE)
        val dueDate = calculateDueDate(LocalDateTime.now().toLocalDate(), findArticle.createdAt.toLocalDate().plusDays(9))

        val viewCntKey = "article:$articleId:viewer:${getAuthenticationName()}"

        if (requireAddViewCnt == true and !redisService.hasKey(viewCntKey)) {
            // 10분동안 동일한 사람이 조회하면 조회수 증가 방지
            redisService.saveWithTemplate(viewCntKey, getAuthenticationName(), 60 * 10)
            findArticle.addViewCnt()
        }
        return ArticleResponse.from(article = findArticle, dueDate = dueDate)
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

        val dueDate = calculateDueDate(LocalDateTime.now().toLocalDate(), updatedArticle.createdAt.toLocalDate().plusDays(9))

        return ArticleResponse.from(
            article = updatedArticle,
            hasToWarnEditAlarm = hasToWarnEditAlarm(
                today = LocalDateTime.now().toLocalDate(),
                editWarningDate = article.createdAt.toLocalDate().plusDays(8),
            ),
            dueDate = dueDate
        )
    }

    @Transactional
    fun deleteArticle(articleId: Long, hasToSoftDel: Boolean = true) {
        verifyAuthentication()

        val article = articleRepository.findByIdOrNull(articleId)
            ?: throw CustomException(ErrorCode.NOT_FOUND_ARTICLE)
        if (hasToSoftDel) {
            article.softDelete()
        }
        else {
            articleRepository.delete(article)
        }
    }

    fun verifyEditableArticle(today: LocalDate, editExpiryDate: LocalDate) {
        if (today.isEqual(editExpiryDate) or today.isAfter(editExpiryDate)) {
            throw CustomException(ErrorCode.INVALID_EDIT_ARTICLE)
        }
    }

    fun hasToWarnEditAlarm(today: LocalDate, editWarningDate: LocalDate): Boolean {
        return today.isEqual(editWarningDate)
    }

    fun calculateDueDate(today: LocalDate, editExpiryDate: LocalDate): Int {
        return ChronoUnit.DAYS.between(today, editExpiryDate).toInt()
    }
}
