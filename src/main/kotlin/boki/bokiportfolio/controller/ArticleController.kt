package boki.bokiportfolio.controller

import boki.bokiportfolio.common.ErrorCode
import boki.bokiportfolio.controller.doc.ArticleApiSpec
import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleDetailResponse
import boki.bokiportfolio.dto.ArticleSummaryResponse
import boki.bokiportfolio.dto.ArticleUpdateRequest
import boki.bokiportfolio.exception.CustomException
import boki.bokiportfolio.service.ArticleService
import boki.bokiportfolio.service.MinioService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.core.io.ByteArrayResource
import org.springframework.data.domain.Sort
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths

@RequestMapping("/api/articles")
@RestController
class ArticleController(
    private val articleService: ArticleService,
    private val minioService: MinioService,
    private val objectMapper: ObjectMapper,
) : ArticleApiSpec {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE])
    override fun createArticle(
        @RequestPart(name = "article", required = true) articleCreateRequest: ArticleCreateRequest,
        @RequestPart(name = "imgFile", required = false) imgFile: MultipartFile?,
    ): ResponseEntity<ArticleDetailResponse> {

        val newArticleResponse = articleService.createArticle(articleCreateRequest)
        val location = URI.create("/api/articles/${newArticleResponse.id}")

        imgFile?.let {
            if (it.contentType?.contains("image") == false) {
                throw CustomException(ErrorCode.INVALID_IMG_FILE)
            }
            else {
                minioService.upload(imgFile, mapOf("articleId" to newArticleResponse.id.toString()))
                articleService.updateAttachImgFileName(newArticleResponse.id, it.originalFilename!!)
            }
        }

        return ResponseEntity.created(location).body(newArticleResponse)
    }

    @GetMapping
//    fun getArticles(@PageableDefault(page = 0, size = 10) pageable: Pageable) {
    override fun getArticles(
        @RequestParam(name = "title", required = false) title: String?,
        @RequestParam(name = "created_at_order_by", required = true, defaultValue = "DESC")
        createdAtSortDirection: Sort.Direction,
    ): ResponseEntity<List<ArticleSummaryResponse>> {
        return ResponseEntity.ok().body(articleService.getArticles(title, createdAtSortDirection))
    }

    @PatchMapping("/{article_id}/like")
    override fun likeArticle(@PathVariable(name = "article_id") articleId: Long): ResponseEntity<ArticleDetailResponse> {
        return ResponseEntity.ok().body(articleService.likeArticle(articleId))
    }

    @PatchMapping("/{article_id}/cancel-like")
    override fun cancelLikeArticle(@PathVariable(name = "article_id") articleId: Long): ResponseEntity<ArticleDetailResponse> {
        return ResponseEntity.ok().body(articleService.cancelLikeArticle(articleId))
    }

    @GetMapping("/{article_id}")
    override fun getArticle(@PathVariable(name = "article_id") articleId: Long): ResponseEntity<Any> {
        val articleResponse = articleService.getArticle(articleId)

        articleResponse.imgFileName?.let {
            val userHomeDirectory = System.getProperty("user.home")
            val downloadDirPath = Paths.get(userHomeDirectory, "Downloads", "aaaatest")

            // 디렉토리가 존재하지 않으면 생성
            if (Files.notExists(downloadDirPath)) {
                Files.createDirectories(downloadDirPath)
            }

            val imagePath = downloadDirPath.resolve(it)

            // 파일이 존재하지 않으면 다운로드
            if (Files.notExists(imagePath)) {
                minioService.download(it, imagePath.toString())
            }

            val imageBytes = Files.readAllBytes(imagePath)
            val imageResource = ByteArrayResource(imageBytes)

            val headers = HttpHeaders()
            headers.contentType = MediaType.MULTIPART_FORM_DATA

            // JSON
            val jsonPart = objectMapper.writeValueAsString(articleResponse)

            val multipartBody: MultiValueMap<String, Any> = LinkedMultiValueMap()
            multipartBody.add("article", jsonPart)
            multipartBody.add("image", imageResource)

            return ResponseEntity.ok().headers(headers).body(multipartBody)
        }

        return ResponseEntity.ok().body(articleResponse)
    }

    //    @PreAuthorize("hasRole('ROLE_ADMIN') or #authorId.equals(authentication.principal.id)")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #authorId.toString().equals(authentication.name)")
    @PatchMapping("/{article_id}")
    override fun updateArticle(
        @PathVariable(name = "article_id") articleId: Long,
        @RequestParam(name = "author_id") authorId: Long,
        @RequestBody articleUpdateRequest: ArticleUpdateRequest,
    ): ResponseEntity<ArticleDetailResponse> {
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
