package boki.bokiportfolio.controller

import boki.bokiportfolio.controller.doc.ArticleApiSpec
import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
import boki.bokiportfolio.dto.ArticleUpdateRequest
import boki.bokiportfolio.service.ArticleService
import boki.bokiportfolio.validator.SecurityManager
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RequestMapping("/api/articles")
@RestController
class ArticleController(
    private val articleService: ArticleService,
) : ArticleApiSpec {
    @PostMapping
    override fun createArticle(@RequestBody articleCreateRequest: ArticleCreateRequest): ResponseEntity<ArticleResponse> {
        val newArticleResponse = articleService.createArticle(articleCreateRequest)
        val location = URI.create("/api/articles/${newArticleResponse.id}")
        return ResponseEntity.created(location).body(newArticleResponse)
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN') or #authorId.equals(authentication.principal.id)")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #authorId.toString().equals(authentication.name)")
    @PatchMapping("/{articleId}")
    override fun updateArticle(
        @PathVariable articleId: Long,
        @RequestParam authorId: Long,
        @RequestBody articleUpdateRequest: ArticleUpdateRequest,
    ): ResponseEntity<ArticleResponse> {
        return ResponseEntity.ok(articleService.updateArticle(articleUpdateRequest.copy(id = articleId)))
    }
}
