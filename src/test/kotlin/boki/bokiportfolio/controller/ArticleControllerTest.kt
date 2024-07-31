package boki.bokiportfolio.controller

import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
import boki.bokiportfolio.dto.ArticleUpdateRequest
import boki.bokiportfolio.support.ControllerTestSupport
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

class ArticleControllerTest : ControllerTestSupport() {

    @DisplayName("게시글을 등록한다")
    @WithMockUser
    @Test
    @Throws(Exception::class)
    fun createArticle() {
        // given
        val request = ArticleCreateRequest(
            title = "테스트 제목",
            content = "테스트 내용",
        )
        val response = ArticleResponse(
            id = 1L,
            title = request.title,
            content = request.content,
            author = "test",
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            editExpiryDate = LocalDateTime.now().plusDays(10),
        )

        Mockito.`when`(articleService.createArticle(request)).thenReturn(response)

        // when // then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/articles")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("테스트 제목"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("테스트 내용"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$.editExpiryDate").exists())
    }

    @DisplayName("관리자가 아닌 다른 사람이 올린 게시글을 수정할 수 없다")
    @WithMockUser(username = "3", roles = ["USER"])
    @Test
    @Throws(Exception::class)
    fun updateArticleBySomeoneElseNoAdmin() {
        // given
        val request = ArticleUpdateRequest(
            id = 1L,
            title = "수정할 제목",
            content = "수정할 내용",
        )
        val response = ArticleResponse(
            id = 1L,
            title = request.title ?: "",
            content = request.content ?: "",
            author = "test",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            editExpiryDate = LocalDateTime.now().plusDays(10),
        )

        Mockito.`when`(articleService.updateArticle(request)).thenReturn(response)

        // when // then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/articles/{articleId}", request.id)
                .queryParam("authorId", "1")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @DisplayName("본인이 올린 게시글을 수정한다")
    @WithMockUser(username = "1", roles = ["USER"])
    @Test
    @Throws(Exception::class)
    fun updateArticleByMe() {
        // given
        val request = ArticleUpdateRequest(
            id = 1L,
            title = "수정할 제목",
            content = "수정할 내용",
        )
        val response = ArticleResponse(
            id = 1L,
            title = request.title ?: "",
            content = request.content ?: "",
            author = "test",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            editExpiryDate = LocalDateTime.now().plusDays(10),
        )

        Mockito.`when`(articleService.updateArticle(request)).thenReturn(response)

        // when // then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/articles/{articleId}", request.id)
                .queryParam("authorId", "1")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("수정할 제목"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("수정할 내용"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.editExpiryDate").exists())
    }

    @DisplayName("관리자가 직접 게시글을 수정한다")
    @WithMockUser(username = "admin", roles = ["ADMIN"])
    @Test
    @Throws(Exception::class)
    fun updateArticleByAdmin() {
        // given
        val request = ArticleUpdateRequest(
            id = 1L,
            title = "수정할 제목",
            content = "수정할 내용",
        )
        val response = ArticleResponse(
            id = 1L,
            title = request.title ?: "",
            content = request.content ?: "",
            author = "test",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            editExpiryDate = LocalDateTime.now().plusDays(10),
        )

        Mockito.`when`(articleService.updateArticle(request)).thenReturn(response)

        // when // then
        mockMvc.perform(
            MockMvcRequestBuilders.patch("/api/articles/{articleId}", request.id)
                .queryParam("authorId", "1")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("수정할 제목"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("수정할 내용"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.createdAt").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.updatedAt").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.editExpiryDate").exists())
    }
}
