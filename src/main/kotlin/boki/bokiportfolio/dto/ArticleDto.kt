package boki.bokiportfolio.dto

import boki.bokiportfolio.entity.Article
import boki.bokiportfolio.entity.User
import boki.bokiportfolio.validator.ArticleValidator.INVALID_ARTICLE_CONTENT_MSG
import boki.bokiportfolio.validator.ArticleValidator.INVALID_ARTICLE_TITLE_MSG
import boki.bokiportfolio.validator.ArticleValidator.isValidContent
import boki.bokiportfolio.validator.ArticleValidator.isValidTitle
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(name = "게시글 등록 요청", description = "게시글 등록 정보")
data class ArticleCreateRequest(
    @field:Schema(description = "제목(200글자 이하)", example = "test title", required = true)
    val title: String,
    @field:Schema(description = "내용(1000글자 이하)", example = "test content", required = true)
    val content: String,
) {
    init {
        require(isValidTitle(title)) { INVALID_ARTICLE_TITLE_MSG }
        require(isValidContent(content)) { INVALID_ARTICLE_CONTENT_MSG }
    }

    fun toEntity(user: User): Article {
        return Article.createArticle(title, content, user)
    }
}

data class ArticleUpdateRequest(
    @field:Schema(description = "게시글 아이디", example = "1", required = true)
    val id: Long,
    @field:Schema(description = "제목(200글자 이하)", example = "update title", required = false)
    val title: String?,
    @field:Schema(description = "내용(1000글자 이하)", example = "update content", required = false)
    val content: String?,
) {
    init {
        title?.let { require(isValidTitle(it)) { INVALID_ARTICLE_TITLE_MSG } }
        content?.let { require(isValidContent(it)) { INVALID_ARTICLE_CONTENT_MSG } }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(name = "게시글 정보 응답", description = "게시글 관련 응답 정보")
data class ArticleResponse(
    @field:Schema(description = "DB id")
    val id: Long,

    @field:Schema(description = "제목")
    val title: String,

    @field:Schema(description = "내용")
    val content: String,

    @field:Schema(description = "글 작성자(아이디)")
    val author: String,

    @field:Schema(description = "작성일")
    val createdAt: LocalDateTime,

    @field:Schema(description = "최종 수정일")
    val updatedAt: LocalDateTime?,

    @field:Schema(description = "수정 가능 만료일")
    val editExpiryDate: LocalDateTime,

    @field:Schema(description = "수정 불가 하루 전 경고 알림")
    var warningMessage: String? = null,
) {
    companion object {
        fun from(article: Article, hasToWarnEditAlarm: Boolean = false): ArticleResponse {
            val response = ArticleResponse(
                id = article.id!!,
                title = article.title,
                content = article.content,
                author = article.user.userId,
                createdAt = article.createdAt,
                updatedAt = article.updatedAt,
                editExpiryDate = article.createdAt.plusDays(10),
            )
            if (hasToWarnEditAlarm) {
                response.warningMessage = "게시글 수정 불가 하루 전 입니다!"
            }
            return response
        }
    }
}
