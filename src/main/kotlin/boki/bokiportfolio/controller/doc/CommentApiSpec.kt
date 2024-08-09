package boki.bokiportfolio.controller.doc

import boki.bokiportfolio.dto.CommentCreateRequest
import boki.bokiportfolio.dto.CommentResponse
import boki.bokiportfolio.dto.CommentUpdateRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "C. 댓글 API")
interface CommentApiSpec {

    @Operation(summary = "댓글 등록", security = [SecurityRequirement(name = "accessToken")])
    fun createComment(
        @RequestParam(name = "article_id", required = true) articleId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest,
    ): ResponseEntity<CommentResponse>

    @Operation(summary = "댓글 수정", security = [SecurityRequirement(name = "accessToken")])
    fun updateComment(
        @PathVariable(name = "comment_id", required = true) commentId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest,
    ): ResponseEntity<CommentResponse>

    @Operation(summary = "댓글 삭제", security = [SecurityRequirement(name = "accessToken")])
    fun deleteComment(
        @PathVariable(name = "comment_id", required = true) commentId: Long,
        @RequestParam(name = "soft_del", required = false, defaultValue = "true") softDel: Boolean,
    ): ResponseEntity<Unit>
}

