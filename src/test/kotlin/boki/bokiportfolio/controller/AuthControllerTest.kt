package boki.bokiportfolio.controller

import boki.bokiportfolio.dto.UserRegisterRequest
import boki.bokiportfolio.dto.UserResponse
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

class OrderControllerTest : ControllerTestSupport() {

    @DisplayName("신규 회원을 가입을 성공한다.")
    @WithMockUser
    @Test
    @Throws(Exception::class)
    fun signup() {
        // given
        val request = UserRegisterRequest(
            email = "test1@test.com",
            phoneNumber = "010-1234-5678",
            userId = "Tester1",
            name = "홍길동",
            password = "Password12345!@",
        )
        val response = UserResponse(
            id = 1L,
            email = "test1@test.com",
            phoneNumber = "010-1234-5678",
            userId = "Tester1",
            name = "홍길동",
        )

        Mockito.`when`(authService.signup(request)).thenReturn(response)

        // when // then
        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/auth/signup")
                .with(csrf())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value("Tester1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test1@test.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("홍길동"))
    }

}
