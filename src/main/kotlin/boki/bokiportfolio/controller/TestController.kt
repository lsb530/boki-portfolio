package boki.bokiportfolio.controller

import boki.bokiportfolio.service.ArticleService
import boki.bokiportfolio.service.MinioService
import org.springframework.core.io.ByteArrayResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Files
import java.nio.file.Paths

@RestController
class TestController(
    private val articleService: ArticleService,
    private val minioService: MinioService,
) {

    @GetMapping
    fun test(): ResponseEntity<Any> {
        return ResponseEntity.ok("ready boki")
    }

    @GetMapping("/test/articles/{article_id}")
    fun imgTest(@PathVariable(name = "article_id") articleId: Long): ResponseEntity<Any> {
        val findArticle = articleService.getArticle(articleId)

        if (findArticle.imgFileName == null)
            throw RuntimeException("다운받을 이미지가 없습니다")
        else {
            val userHomeDirectory = System.getProperty("user.home")
            val downloadDirPath = Paths.get(userHomeDirectory, "Downloads", "aaaatest")

            // 디렉토리가 존재하지 않으면 생성
            if (Files.notExists(downloadDirPath)) {
                Files.createDirectories(downloadDirPath)
            }

            val imagePath = downloadDirPath.resolve(findArticle.imgFileName)

            // 파일이 존재하지 않으면 다운로드
            if (Files.notExists(imagePath)) {
                minioService.download(findArticle.imgFileName, imagePath.toString())
            }

            val imageBytes = Files.readAllBytes(imagePath)
            val imageResource = ByteArrayResource(imageBytes)

            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageResource)
        }
    }

}
