package boki.bokiportfolio.controller.doc

import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody

@Tag(name = "B. 게시글 API")
interface ArticleApiSpec {

    @Operation(summary = "게시글 등록", security = [SecurityRequirement(name = "accessToken")])
    fun createArticle(@RequestBody articleCreateRequest: ArticleCreateRequest): ResponseEntity<ArticleResponse>

}

