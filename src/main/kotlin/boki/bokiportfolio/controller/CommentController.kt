package boki.bokiportfolio.controller

import boki.bokiportfolio.common.ErrorCode
import boki.bokiportfolio.controller.doc.CommentApiSpec
import boki.bokiportfolio.dto.CommentCreateRequest
import boki.bokiportfolio.dto.CommentResponse
import boki.bokiportfolio.dto.CommentUpdateRequest
import boki.bokiportfolio.service.CommentService
import boki.bokiportfolio.validator.SecurityManager.verifyAdminOrMine
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/comments")
@RestController
class CommentController(
    private val commentService: CommentService,
) : CommentApiSpec {

    @PostMapping
    override fun createComment(
        @RequestParam(name = "article_id", required = true) articleId: Long,
        @RequestBody commentCreateRequest: CommentCreateRequest,
    ): ResponseEntity<CommentResponse> {
        val newCommentResponse =
            commentService.createComment(articleId = articleId, commentCreateRequest = commentCreateRequest)
        return ResponseEntity.ok().body(newCommentResponse)
    }

    @PatchMapping("/{comment_id}")
    override fun updateComment(
        @PathVariable(name = "comment_id", required = true) commentId: Long,
        @RequestBody commentUpdateRequest: CommentUpdateRequest,
    ): ResponseEntity<CommentResponse> {
        checkCommentAuthority(commentId)

        return ResponseEntity.ok(commentService.updateComment(commentId = commentId, commentUpdateRequest = commentUpdateRequest))
    }

    @DeleteMapping("/{comment_id}")
    override fun deleteComment(
        @PathVariable(name = "comment_id", required = true) commentId: Long,
        @RequestParam(name = "soft_del", required = false, defaultValue = "true") softDel: Boolean,
    ): ResponseEntity<Unit> {
        checkCommentAuthority(commentId)

        commentService.deleteComment(commentId, softDel)
        return ResponseEntity.noContent().build()
    }

    fun checkCommentAuthority(commentId: Long) {
        val authorId = commentService.findAuthorId(commentId)
        verifyAdminOrMine(authorId.toString(), ErrorCode.INVALID_ACCESS_COMMENT)
    }
}
