package boki.bokiportfolio.controller.doc

import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
import boki.bokiportfolio.dto.ArticleUpdateRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "B. 게시글 API")
interface ArticleApiSpec {

    @Operation(summary = "게시글 등록", security = [SecurityRequirement(name = "accessToken")])
    fun createArticle(@RequestBody articleCreateRequest: ArticleCreateRequest): ResponseEntity<ArticleResponse>

    @Operation(summary = "게시글 수정", security = [SecurityRequirement(name = "accessToken")])
    fun updateArticle(@PathVariable articleId: Long, @RequestParam authorId: Long, @RequestBody articleUpdateRequest: ArticleUpdateRequest): ResponseEntity<ArticleResponse>

    @Operation(summary = "게시글 목록 조회", security = [SecurityRequirement(name = "accessToken")])
    fun getArticles(@RequestParam title: String?, @RequestParam createdAtSortDirection: Sort.Direction): ResponseEntity<List<ArticleResponse>>

    @Operation(summary = "게시글 단건 조회", security = [SecurityRequirement(name = "accessToken")])
    fun getArticle(@PathVariable articleId: Long): ResponseEntity<ArticleResponse>

    @Operation(summary = "게시글 삭제", security = [SecurityRequirement(name = "accessToken")])
    fun deleteArticle(@PathVariable articleId: Long, @RequestParam authorId: Long, @RequestParam softDel: Boolean): ResponseEntity<Unit>
}

