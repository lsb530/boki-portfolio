package boki.bokiportfolio.dto

import boki.bokiportfolio.entity.Article
import boki.bokiportfolio.entity.User
import boki.bokiportfolio.validator.ArticleValidator.INVALID_ARTICLE_CONTENT_MSG
import boki.bokiportfolio.validator.ArticleValidator.INVALID_ARTICLE_TITLE_MSG
import boki.bokiportfolio.validator.ArticleValidator.isValidContent
import boki.bokiportfolio.validator.ArticleValidator.isValidTitle
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

    private fun isValidTitle(title: String): Boolean {
        return title.length <= 200
    }

    private fun isValidContent(content: String): Boolean {
        return content.length <= 1000
    }
}

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
) {
    companion object {
        fun from(article: Article): ArticleResponse {
            return ArticleResponse(
                id = article.id!!,
                title = article.title,
                content = article.content,
                author = article.user.userId,
                createdAt = article.createdAt,
                updatedAt = article.updatedAt,
                editExpiryDate = article.createdAt.plusDays(10),
            )
        }
    }
}
