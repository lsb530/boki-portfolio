package boki.bokiportfolio.dto

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class ArticleDtoTest : FunSpec(
    {
        context("게시글 생성") {

            val articleCreateFixture = ArticleCreateRequest(
                title = "테스트 제목",
                content = "테스트 내용"
            )

            context("[제목] - 길이 200자 제한 검증") {
                test("200글자를 넘은 제목으로 게시글 등록 요청을 하면 예외가 발생한다") {
                    val ex = shouldThrow<IllegalArgumentException> {
                        articleCreateFixture.copy(
                            title = "ㅋ".repeat(201)
                        )
                    }
                    ex.message shouldBe "제목은 200글자를 넘을 수 없습니다"
                }

                test("200글자 이하의 제목으로 게시글 등록 요청을 할 수 있다") {
                    articleCreateFixture.shouldNotBeNull()
                }
            }

            context("[내용] - 길이 1000자 제한 검증") {
                test("1000글자를 넘은 내용으로 게시글 등록 요청을 하면 예외가 발생한다") {
                    val ex = shouldThrow<IllegalArgumentException> {
                        articleCreateFixture.copy(
                            content = "ㅋ".repeat(1001)
                        )
                    }
                    ex.message shouldBe "내용은 1000글자를 넘을 수 없습니다"
                }

                test("1000글자 이하의 내용으로 게시글 등록 요청을 할 수 있다") {
                    articleCreateFixture.shouldNotBeNull()
                }
            }
        }
    },
)
