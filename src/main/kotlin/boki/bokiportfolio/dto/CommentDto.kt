package boki.bokiportfolio.dto

import boki.bokiportfolio.entity.Article
import boki.bokiportfolio.entity.Comment
import boki.bokiportfolio.entity.User
import boki.bokiportfolio.validator.CommentValidator.INVALID_COMMENT_CONTENT_MSG
import boki.bokiportfolio.validator.CommentValidator.isValidContent
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * 댓글 생성 요청
 */
@Schema(name = "댓글 등록 요청", description = "댓글 등록 정보")
data class CommentCreateRequest(
    @field:Schema(description = "내용(500글자 이하)", example = "test content", required = true)
    val content: String,
) {
    init {
        require(isValidContent(content)) { INVALID_COMMENT_CONTENT_MSG }
    }

    fun toEntity(article: Article, userId: Long): Comment {
        return Comment.createComment(
            content = content,
            article = article,
            userId = userId,
        )
    }
}

/**
 * 댓글 수정 요청
 */
@Schema(name = "댓글 수정 요청", description = "댓글 등록 정보")
data class CommentUpdateRequest(
    @field:Schema(description = "내용(500글자 이하)", example = "update content", required = true)
    val content: String,
) {
    init {
        require(isValidContent(content)) { INVALID_COMMENT_CONTENT_MSG }
    }
}

/**
 * 댓글 표준 응답
 */
@Schema(name = "댓글 정보 응답", description = "댓글 관련 응답 정보")
data class CommentResponse(
    @field:Schema(description = "DB id")
    val id: Long,

    @field:Schema(description = "내용")
    val content: String,

    @field:Schema(description = "게시글 id")
    val articleId: Long,

    @field:Schema(description = "작성자 계정 아이디")
    val userId: String,

    @field:Schema(description = "작성일")
    val createdAt: LocalDateTime,

    @field:Schema(description = "최종 수정일")
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun from(comment: Comment, user: User): CommentResponse {
            return CommentResponse(
                id = comment.id!!,
                content = comment.content,
                articleId = comment.article.id!!,
                userId = user.userId,
                createdAt = comment.createdAt,
                updatedAt = comment.updatedAt,
            )
        }
    }
}
