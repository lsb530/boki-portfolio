package boki.bokiportfolio.dto

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class AutoDtoTest : FunSpec(
    {
        context("로그인") {
            val loginRequest = LoginRequest(
                userId = "test",
                password = "Password12345"
            )
            context("아이디, 패스워드") {
                test("❌- 로그인 요청시 userId 필드가 빈 문자열 일 수 없다") {
                    val ex = shouldThrow<IllegalArgumentException> {
                        loginRequest.copy(userId = "")
                    }
                    ex.message shouldBe "아이디는 비어있을 수 없습니다"
                }

                test("❌- 로그인 요청시 password 필드가 비어있거나 빈 문자열 일 수 없다") {
                    val ex = shouldThrow<IllegalArgumentException> {
                        loginRequest.copy(password = "")
                    }
                    ex.message shouldBe "비밀번호는 비어있을 수 없습니다"
                }

                test("✅- 로그인에 알맞은 데이터로 로그인을 성공할 수 있다") {
                    loginRequest.shouldNotBeNull()
                }
            }
        }
    },
)
