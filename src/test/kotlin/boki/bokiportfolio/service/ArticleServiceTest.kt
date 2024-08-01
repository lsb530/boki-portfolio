package boki.bokiportfolio.service

import boki.bokiportfolio.common.ErrorCode
import boki.bokiportfolio.common.Role
import boki.bokiportfolio.dto.ArticleCreateRequest
import boki.bokiportfolio.dto.ArticleResponse
import boki.bokiportfolio.dto.ArticleUpdateRequest
import boki.bokiportfolio.entity.Article
import boki.bokiportfolio.entity.User
import boki.bokiportfolio.exception.CustomException
import boki.bokiportfolio.repository.ArticleRepository
import boki.bokiportfolio.repository.UserRepository
import boki.bokiportfolio.util.SecurityHelper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class ArticleServiceTest : BehaviorSpec(
    {
        val userRepository = mockk<UserRepository>()
        val articleRepository = mockk<ArticleRepository>()

//        val articleService = ArticleService(userRepository, articleRepository)
        // 내부 함수를 호출하기 위해서 spy로 선언해 줄 필요가 있었음..
        val articleService = spyk(
            ArticleService(userRepository, articleRepository),
            recordPrivateCalls = true,
        )

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
                    Then("UNAUTHORIZED_ACCESS(인증 필요) 예외가 발생한다") {
                        val ex = shouldThrow<CustomException> {
                            articleService.createArticle(request)
                        }
                        ex.message shouldBe "인증이 필요한 요청입니다"

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

                    Then("게시글 등록에 성공하고, 성공 결과를 반환한다") {
                        val response = articleService.createArticle(request)
                        response shouldBe ArticleResponse.from(article = savedArticle, dueDate = 9)

                        verify { userRepository.findByIdOrNull(any()) }
                        verify { articleRepository.save(any()) }
                    }
                }
            }
        }

        context("게시글 수정 요청") {

            Given("게시글을 수정하려는 상황에서") {

                val request = ArticleUpdateRequest(
                    id = 1L,
                    title = "테스트 제목",
                    content = "테스트 내용",
                )

                When("❌- 로그인을 하지 않은 유저가 글을 수정하려 할 때") {
                    Then("UNAUTHORIZED_ACCESS(인증 필요) 예외가 발생한다") {
                        val ex = shouldThrow<CustomException> {
                            articleService.updateArticle(request)
                        }
                        ex.message shouldBe "인증이 필요한 요청입니다"

                        verify(exactly = 0) { userRepository.findByIdOrNull(any()) }
                    }
                }

                When("❌- 존재하지 않는 게시글을 수정하려 할 때") {
                    SecurityHelper.injectSecurityContext(1L, Role.USER)
                    every { articleRepository.findByIdOrNull(request.id) } throws CustomException(ErrorCode.NOT_FOUND_ARTICLE)

                    Then("NOT_FOUND_ARTICLE(게시글 없음) 예외가 발생한다") {
                        val ex = shouldThrow<CustomException> {
                            articleService.updateArticle(request)
                        }
                        ex.errorCode shouldBe ErrorCode.NOT_FOUND_ARTICLE
                        ex.message shouldBe "해당 게시글은 존재하지 않습니다"

                        verify { articleRepository.findByIdOrNull(request.id) }
                    }
                }

                When("❌- 작성된 지 10일이 지난 게시글을 수정하려 할 때") {
                    SecurityHelper.injectSecurityContext(1L, Role.USER)

                    val user = User(
                        id = 1L,
                        email = "test@example.com",
                        phoneNumber = "010-1234-5678",
                        userId = "testUser",
                        name = "홍길동",
                        password = "Password1234!@",
                        role = Role.USER,
                    )

                    val findArticle = Article(
                        id = 1L,
                        title = "제목",
                        content = "내용",
                        user = user,
                    )

                    every { articleRepository.findByIdOrNull(request.id) } returns findArticle
                    every {
                        articleService.verifyEditableArticle(any(), any())
                    } throws CustomException(ErrorCode.INVALID_EDIT_ARTICLE)

                    Then("INVALID_EDIT_ARTICLE(수정 불가) 예외가 발생한다") {
                        val ex = shouldThrow<CustomException> {
                            articleService.updateArticle(request)
                        }
                        ex.errorCode shouldBe ErrorCode.INVALID_EDIT_ARTICLE
                        ex.message shouldBe "해당 게시글은 수정 가능한 날짜가 지났습니다"

                        verify { articleRepository.findByIdOrNull(request.id) }
                        verify { articleService.verifyEditableArticle(any(), any()) }
                    }
                }

                When("✅- 작성한 지 3일이 지난 게시글을 수정하려 할 때") {
                    SecurityHelper.injectSecurityContext(1L, Role.USER)

                    val user = User(
                        id = 1L,
                        email = "test@example.com",
                        phoneNumber = "010-1234-5678",
                        userId = "testUser",
                        name = "홍길동",
                        password = "Password1234!@",
                        role = Role.USER,
                    )

                    val findArticle = Article(
                        id = 1L,
                        title = "제목",
                        content = "내용",
                        user = user,
                    )

                    val updatedArticle = Article(
                        id = 1L,
                        title = request.title ?: findArticle.title,
                        content = request.content ?: findArticle.content,
                        user = user,
                    )

                    every { articleRepository.findByIdOrNull(request.id) } returns findArticle
                    every { articleService.verifyEditableArticle(any(), any()) } returns Unit
                    every { articleService.hasToWarnEditAlarm(any(), any()) } returns false
                    every { articleRepository.saveAndFlush(any()) } returns updatedArticle

                    Then("게시글 수정에 성공하고, 성공 결과를 반환한다") {
                        val response = articleService.updateArticle(request)
                        response shouldBe ArticleResponse.from(
                            article = updatedArticle, hasToWarnEditAlarm = false,
                            dueDate = 9,
                        )
                        response.warningMessage.shouldBeNull()

                        verify { articleRepository.findByIdOrNull(request.id) }
                        verify { articleService.verifyEditableArticle(any(), any()) }
                        verify { articleService.hasToWarnEditAlarm(any(), any()) }
                        verify { articleRepository.saveAndFlush(any()) }
                    }
                }

                When("✅- 작성한 지 9일이 지난 게시글을 수정하려 할 때") {
                    SecurityHelper.injectSecurityContext(1L, Role.USER)

                    val user = User(
                        id = 1L,
                        email = "test@example.com",
                        phoneNumber = "010-1234-5678",
                        userId = "testUser",
                        name = "홍길동",
                        password = "Password1234!@",
                        role = Role.USER,
                    )

                    val findArticle = Article(
                        id = 1L,
                        title = "제목",
                        content = "내용",
                        user = user,
                    )

                    val updatedArticle = Article(
                        id = 1L,
                        title = request.title ?: findArticle.title,
                        content = request.content ?: findArticle.content,
                        user = user,
                    )

                    every { articleRepository.findByIdOrNull(request.id) } returns findArticle
                    every { articleService.verifyEditableArticle(any(), any()) } returns Unit
                    every { articleService.hasToWarnEditAlarm(any(), any()) } returns true
                    every { articleRepository.saveAndFlush(any()) } returns updatedArticle

                    Then("'하루 뒤 수정 불가' 경고 알내문과 함께 게시글 수정에 성공하고, 성공 결과를 반환한다") {
                        val response = articleService.updateArticle(request)
                        response shouldBe ArticleResponse.from(
                            article = updatedArticle, hasToWarnEditAlarm = true,
                            dueDate = 9,
                        )
                        response.warningMessage shouldBe "게시글 수정 불가 하루 전 입니다!"

                        verify { articleRepository.findByIdOrNull(request.id) }
                        verify { articleService.verifyEditableArticle(any(), any()) }
                        verify { articleService.hasToWarnEditAlarm(any(), any()) }
                        verify { articleRepository.saveAndFlush(any()) }
                    }
                }
            }
        }

        context("게시글 삭제 요청") {

            Given("게시글을 삭제하려는 상황에서") {

                val articleId = 1L

                When("❌- 로그인을 하지 않은 유저가 글을 수정하려 할 때") {
                    Then("UNAUTHORIZED_ACCESS(인증 필요) 예외가 발생한다") {
                        val ex = shouldThrow<CustomException> {
                            articleService.deleteArticle(articleId)
                        }
                        ex.message shouldBe "인증이 필요한 요청입니다"

                        verify(exactly = 0) { userRepository.findByIdOrNull(any()) }
                    }
                }

                When("❌- 존재하지 않는 게시글을 삭제하려 할 때") {
                    SecurityHelper.injectSecurityContext(1L, Role.USER)
                    every { articleRepository.findByIdOrNull(articleId) } throws CustomException(ErrorCode.NOT_FOUND_ARTICLE)

                    Then("NOT_FOUND_ARTICLE(게시글 없음) 예외가 발생한다") {
                        val ex = shouldThrow<CustomException> {
                            articleService.deleteArticle(articleId)
                        }
                        ex.errorCode shouldBe ErrorCode.NOT_FOUND_ARTICLE
                        ex.message shouldBe "해당 게시글은 존재하지 않습니다"

                        verify { articleRepository.findByIdOrNull(articleId) }
                    }
                }

                When("✅- soft delete 를 하려고 할 때") {
                    SecurityHelper.injectSecurityContext(1L, Role.USER)

                    val user = User(
                        id = 1L,
                        email = "test@example.com",
                        phoneNumber = "010-1234-5678",
                        userId = "testUser",
                        name = "홍길동",
                        password = "Password1234!@",
                        role = Role.USER,
                    )

                    val findArticle = spyk(
                        Article(
                            id = 1L,
                            title = "제목",
                            content = "내용",
                            user = user,
                        ),
                    )

                    every { articleRepository.findByIdOrNull(articleId) } returns findArticle

                    Then("게시글의 deletedAt에 시간이 기록되며, 204(성공) 상태값만 반환한다") {
                        articleService.deleteArticle(articleId, true)

                        verify { articleRepository.findByIdOrNull(articleId) }
                        verify { findArticle.softDelete() }
                        findArticle.deletedAt.shouldNotBeNull()
                    }
                }

                When("✅- hard delete 를 하려고 할 때") {
                    SecurityHelper.injectSecurityContext(1L, Role.USER)

                    val user = User(
                        id = 1L,
                        email = "test@example.com",
                        phoneNumber = "010-1234-5678",
                        userId = "testUser",
                        name = "홍길동",
                        password = "Password1234!@",
                        role = Role.USER,
                    )

                    val findArticle = Article(
                        id = 1L,
                        title = "제목",
                        content = "내용",
                        user = user,
                    )

                    every { articleRepository.findByIdOrNull(articleId) } returns findArticle
                    every { articleRepository.delete(findArticle) } just Runs

                    Then("게시글의 데이터베이스에서 삭제되며, 204(성공) 상태값만 반환한다") {
                        articleService.deleteArticle(articleId, false)

                        verify { articleRepository.findByIdOrNull(articleId) }
                        verify { articleRepository.delete(findArticle) }
                    }
                }
            }
        }
    },
)
