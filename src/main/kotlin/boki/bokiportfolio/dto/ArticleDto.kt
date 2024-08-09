package boki.bokiportfolio.dto

import boki.bokiportfolio.entity.Article
import boki.bokiportfolio.entity.User
import boki.bokiportfolio.validator.ArticleValidator.INVALID_ARTICLE_CONTENT_MSG
import boki.bokiportfolio.validator.ArticleValidator.INVALID_ARTICLE_TITLE_MSG
import boki.bokiportfolio.validator.ArticleValidator.isValidContent
import boki.bokiportfolio.validator.ArticleValidator.isValidTitle
import boki.bokiportfolio.validator.SecurityManager.getAuthenticationName
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 * 게시글 생성 요청
 */
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

/**
 * 게시글 수정 요청
 */
@Schema(name = "게시글 수정 요청", description = "게시글 수정 정보")
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

/**
 * 게시글 목록 조회 응답
 */
@Schema(name = "게시글 요약 정보 응답(목록 요청)", description = "게시글 관련 다건 정보")
data class ArticleSummaryResponse(
    @field:Schema(description = "DB id")
    val id: Long,

    @field:Schema(description = "제목")
    val title: String,

    @field:Schema(description = "조회수")
    val viewCnt: Int = 0,

    @field:Schema(description = "좋아요 수")
    val likeCnt: Int = 0,

    @field:Schema(description = "좋아요 클릭 했는지 여부")
    val hasLiked: Boolean = false,

    @field:Schema(description = "글 작성자(아이디)")
    val author: String,

    @field:Schema(description = "댓글 수")
    val commentCnt: Int = 0,

    @field:Schema(description = "작성일")
    val createdAt: LocalDateTime,

    @field:Schema(description = "최종 수정일")
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun from(article: Article): ArticleSummaryResponse {
            return ArticleSummaryResponse(
                id = article.id!!,
                title = article.title,
                viewCnt = article.viewCnt,
                likeCnt = article.likeUsers.size,
                hasLiked = article.likeUsers.contains(getAuthenticationName().toLong()),
                author = article.user.userId,
                commentCnt = article.comments.size,
                createdAt = article.createdAt,
                updatedAt = article.updatedAt,
            )
        }
    }
}

/**
 * 게시글 단일 조회 응답
 */
@Schema(name = "게시글 상세 정보 응답(단일 요청)", description = "게시글 관련 단건 정보")
data class ArticleDetailResponse(
    @field:Schema(description = "DB id")
    val id: Long,

    @field:Schema(description = "제목")
    val title: String,

    @field:Schema(description = "내용")
    val content: String,

    @field:Schema(description = "이미지 파일명")
    val imgFileName: String? = null,

    @field:Schema(description = "조회수")
    val viewCnt: Int = 0,

    @field:Schema(description = "좋아요 수")
    val likeCnt: Int = 0,

    @field:Schema(description = "좋아요 클릭 했는지 여부")
    val hasLiked: Boolean = false,

    @field:Schema(description = "글 작성자(아이디)")
    val author: String,

    @field:Schema(description = "작성일")
    val createdAt: LocalDateTime,

    @field:Schema(description = "최종 수정일")
    val updatedAt: LocalDateTime?,

    @field:Schema(description = "수정 가능 만료일")
    val editExpiryDate: LocalDateTime,

    @field:Schema(description = "수정 가능 만료일까지 남은 날")
    val dueDate: Int,

    @field:Schema(description = "수정 불가 하루 전 경고 알림")
    var warningMessage: String? = null,

    @field:Schema(description = "댓글 리스트")
    val comments: MutableList<CommentResponse> = mutableListOf()
) {
    companion object {
        fun from(article: Article, hasToWarnEditAlarm: Boolean = false, dueDate: Int): ArticleDetailResponse {
            val response = ArticleDetailResponse(
                id = article.id!!,
                title = article.title,
                content = article.content,
                imgFileName = article.imgFileName,
                viewCnt = article.viewCnt,
                likeCnt = article.likeUsers.size,
                hasLiked = article.likeUsers.contains(getAuthenticationName().toLong()),
                author = article.user.userId,
                createdAt = article.createdAt,
                updatedAt = article.updatedAt,
                editExpiryDate = article.createdAt.plusDays(10),
                dueDate = dueDate,
                comments = article.comments
                    .filter { it.deletedAt == null }
                    .map { CommentResponse.from(it, article.user) }.toMutableList()
            )
            if (hasToWarnEditAlarm) {
                response.warningMessage = "게시글 수정 불가 하루 전 입니다!"
            }
            return response
        }
    }
}
