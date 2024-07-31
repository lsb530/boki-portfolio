package boki.bokiportfolio.controller

import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
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
import kotlin.jvm.Throws

class ArticleControllerTest : ControllerTestSupport() {

    @DisplayName("게시글 등록한다")
    @WithMockUser(username = "test", roles = ["USER"])
    @Test
    @Throws(Exception::class)
    fun createArticle() {
        // given
        val request = ArticleCreateRequest(
            title = "테스트 제목",
            content = "테스트 내용"
        )
        val response = ArticleResponse(
            id = 1L,
            title = request.title,
            content = request.content,
            author = "test",
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            editExpiryDate = LocalDateTime.now().plusDays(10)
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
}
