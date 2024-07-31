package boki.bokiportfolio.service

import boki.bokiportfolio.common.Role
import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
import boki.bokiportfolio.entity.Article
import boki.bokiportfolio.entity.User
import boki.bokiportfolio.repository.ArticleRepository
import boki.bokiportfolio.repository.UserRepository
import boki.bokiportfolio.util.SecurityHelper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class ArticleServiceTest : BehaviorSpec(
    {
        val userRepository = mockk<UserRepository>()
        val articleRepository = mockk<ArticleRepository>()
        val articleService = ArticleService(userRepository, articleRepository)

        afterTest {
            clearAllMocks()
            SecurityHelper.clearSecurityContext()
        }

        context("게시글 등록 요청") {

            Given("게시글을 등록하려는 상황에서") {

                val request = ArticleCreateRequest(
                    title = "테스트 제목",
                    content = "테스트 내용",
                )

                When("❌- 로그인을 하지 않은 유저가 글을 등록하려 할 때") {
                    Then("Authentication Null 예외가 발생한다") {
                        val ex = shouldThrow<NullPointerException> {
                            articleService.createArticle(request)
                        }
                        ex.message?.contains("\"getAuthentication() is null\"")

                        verify(exactly = 0) { userRepository.findByIdOrNull(any()) }
                        verify(exactly = 0) { articleRepository.save(any()) }
                    }
                }

                When("✅- 로그인을 한 유저가 글을 등록하려 할 때") {
                    SecurityHelper.injectSecurityContext(1L, Role.USER)
                    val author = User(
                        id = 1L,
                        email = "test@test.com",
                        phoneNumber = "010-3333-2222",
                        userId = "tester",
                        name = "홍길동",
                        password = "Password1234!@",
                        role = Role.USER,
                    )
                    val savedArticle = Article(
                        id = 1L,
                        title = request.title,
                        content = request.content,
                        user = author,
                    )
                    every { userRepository.findByIdOrNull(any()) } returns author
                    every { articleRepository.save(any()) } returns savedArticle

                    Then("글 등록에 성공하고, 성공 결과를 반환한다") {
                        val response = articleService.createArticle(request)
                        response shouldBe ArticleResponse.from(savedArticle)

                        verify { userRepository.findByIdOrNull(any()) }
                        verify { articleRepository.save(any()) }
                    }
                }
            }
        }

    },
)
