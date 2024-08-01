package boki.bokiportfolio.controller

import boki.bokiportfolio.controller.doc.ArticleApiSpec
import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
import boki.bokiportfolio.dto.ArticleUpdateRequest
import boki.bokiportfolio.service.ArticleService
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
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

    @GetMapping
//    fun getArticles(@PageableDefault(page = 0, size = 10) pageable: Pageable) {
    fun getArticles(
        @RequestParam(name = "title", required = false) title: String?,
        @RequestParam(name = "created_at_order_by", required = true, defaultValue = "DESC")
        createdAtSortDirection: Sort.Direction,
    ): ResponseEntity<Any> {
        return ResponseEntity.ok().body(articleService.getArticles(title, createdAtSortDirection))
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or #authorId.equals(authentication.principal.id)")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #authorId.toString().equals(authentication.name)")
    @PatchMapping("/{article_id}")
    override fun updateArticle(
        @PathVariable(name = "article_id") articleId: Long,
        @RequestParam(name = "author_id") authorId: Long,
        @RequestBody articleUpdateRequest: ArticleUpdateRequest,
    ): ResponseEntity<ArticleResponse> {
        return ResponseEntity.ok(articleService.updateArticle(articleUpdateRequest.copy(id = articleId)))
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #authorId.toString().equals(authentication.name)")
    @DeleteMapping("/{article_id}")
    override fun deleteArticle(
        @PathVariable(name = "article_id") articleId: Long,
        @RequestParam(name = "author_id") authorId: Long,
        @RequestParam(name = "soft_del", required = false, defaultValue = "false") softDel: Boolean,
    ): ResponseEntity<Unit> {
        articleService.deleteArticle(articleId, softDel)
        return ResponseEntity.noContent().build()
    }
}
