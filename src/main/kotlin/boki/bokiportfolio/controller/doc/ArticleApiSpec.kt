package boki.bokiportfolio.controller.doc

import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleDetailResponse
import boki.bokiportfolio.dto.ArticleSummaryResponse
import boki.bokiportfolio.dto.ArticleUpdateRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@Tag(name = "B. 게시글 API")
interface ArticleApiSpec {

    @Operation(summary = "게시글 등록", security = [SecurityRequirement(name = "accessToken")])
    fun createArticle(
        @RequestPart(name = "article", required = true) articleCreateRequest: ArticleCreateRequest,
        @RequestPart(name = "imgFile", required = false) imgFile: MultipartFile?,
    ): ResponseEntity<ArticleDetailResponse>

    @Operation(summary = "게시글 수정", security = [SecurityRequirement(name = "accessToken")])
    fun updateArticle(
        @PathVariable articleId: Long,
        @RequestParam authorId: Long,
        @RequestBody articleUpdateRequest: ArticleUpdateRequest,
    ): ResponseEntity<ArticleDetailResponse>

    @Operation(summary = "게시글 목록 조회", security = [SecurityRequirement(name = "accessToken")])
    fun getArticles(
        @RequestParam title: String?,
        @RequestParam createdAtSortDirection: Sort.Direction,
    ): ResponseEntity<List<ArticleSummaryResponse>>

    @Operation(summary = "게시글 좋아요", security = [SecurityRequirement(name = "accessToken")])
    fun likeArticle(@PathVariable articleId: Long): ResponseEntity<ArticleDetailResponse>

    @Operation(summary = "게시글 좋아요 취소", security = [SecurityRequirement(name = "accessToken")])
    fun cancelLikeArticle(@PathVariable articleId: Long): ResponseEntity<ArticleDetailResponse>

    @Operation(summary = "게시글 단건 조회", security = [SecurityRequirement(name = "accessToken")])
    fun getArticle(@PathVariable articleId: Long): ResponseEntity<Any>

    @Operation(summary = "게시글 삭제", security = [SecurityRequirement(name = "accessToken")])
    fun deleteArticle(
        @PathVariable articleId: Long,
        @RequestParam authorId: Long,
        @RequestParam softDel: Boolean,
    ): ResponseEntity<Unit>
}

