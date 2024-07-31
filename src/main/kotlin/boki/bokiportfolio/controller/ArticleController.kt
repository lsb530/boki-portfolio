package boki.bokiportfolio.controller

import boki.bokiportfolio.controller.doc.ArticleApiSpec
import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
import boki.bokiportfolio.service.ArticleService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
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
    fun test(): ResponseEntity<ArticleResponse> {
        return ResponseEntity.ok(articleService.updateArticle())
    }
}
