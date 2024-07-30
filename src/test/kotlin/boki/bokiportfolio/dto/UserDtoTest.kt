package boki.bokiportfolio.dto

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import java.util.logging.Logger

class UserDtoTest : FunSpec(
    {
        val logger = Logger.getLogger(UserDtoTest::class.java.name)

        context("회원가입") {
            val userRegisterFixture = UserRegisterRequest(
                email = "test1@test.com",
                phoneNumber = "010-1234-5678",
                userId = "Tester1",
                name = "홍길동",
                password = "Password12345!@",
            )
            context("[이메일] - 이메일 형식 검증") {
                test("❌- 잘못된 이메일 형식으로 회원가입 요청을 하면 예외가 발생한다") {
                    val invalidEmailArray = listOf(
                        "123-#.cc",
                        "simple",
                        "@id.com",
                        "username@.com",
                        "username@com",
                    )
                    invalidEmailArray.forAll {
                        val ex = shouldThrow<IllegalArgumentException> {
                            userRegisterFixture.copy(email = it)
/*                            UserRegisterRequest(
                                email = it,
                                userId = "validUser123",
                                phoneNumber = "010-1234-5678",
                                name = "홍길동",
                                password = "Password1!",
                            )*/
                        }
                        ex.message shouldBe "잘못된 이메일 형식입니다"
                    }
                }
                test("✅- 올바른 이메일 형식으로 회원가입 요청을 할 수 있다") {
                    val validEmailArray = listOf(
                        "test1@test.com",
                        "sarah.smith85@domain.com",
                        "username@techcompany.com"
                    )
                    validEmailArray.forAll {
                        val requestInstance = userRegisterFixture.copy(email = it)
                        requestInstance.shouldNotBeNull()
                    }
                }
            }

            context("[휴대폰번호] - 숫자와 하이폰으로 구성된 형식 검즘") {
                test("❌- 잘못된 핸드폰 형식으로 회원가입 요청을 하면 예외가 발생한다") {
                    val inValidPhoneNumberArray = listOf(
                        "01-1234-5678",
                        "010-12-5678",
                        "010-123-567",
                    )
                    inValidPhoneNumberArray.forAll {
                        val ex = shouldThrow<IllegalArgumentException> {
                            userRegisterFixture.copy(phoneNumber = it)
                        }
                        ex.message shouldBe "잘못된 핸드폰 전화번호 형식입니다"
                    }
                }
                test("✅- 올바른 이메일 형식으로 회원가입 요청을 할 수 있다") {
                    val validPhoneNumberArray = listOf(
                        "010-123-5678",
                        "010-999-1212",
                        "010-1234-5678",
                        "010-7777-7777"
                    )
                    validPhoneNumberArray.forAll {
                        val requestInstance = userRegisterFixture.copy(phoneNumber = it)
                        requestInstance.shouldNotBeNull()
                    }
                }
            }

            context("[아이디] - 아이디 대소문자/숫자 형식 검증") {
                test("❌- 잘못된 아이디 형식으로 회원가입 요청을 하면 예외가 발생한다") {
                    val inValidUserIdArray = listOf(
                        "",
                        "!@",
                    )
                    inValidUserIdArray.forAll {
                        val ex = shouldThrow<IllegalArgumentException> {
                            userRegisterFixture.copy(userId = it)
                        }
                        ex.message shouldBe "잘못된 아이디 형식입니다"
                    }
                }

                test("❌- `admin` 문자열이 들어간 아이디 형식으로 회원가입 요청을 하면 예외가 발생한다") {
                    val inValidUserIdArray = listOf(
                        "admin",
                        "admin1234",
                    )
                    inValidUserIdArray.forAll {
                        val ex = shouldThrow<IllegalArgumentException> {
                            userRegisterFixture.copy(userId = it)
                        }
                        ex.message shouldBe "잘못된 아이디 형식입니다"
                    }
                }

                test("✅- 올바른 아이디 형식으로 회원가입 요청을 할 수 있다") {
                    val validUserIdArray = listOf(
                        "tester",
                        "TesTer",
                        "user123",
                    )
                    validUserIdArray.forAll {
                        val requestInstance = userRegisterFixture.copy(userId = it)
                        requestInstance.shouldNotBeNull()
                    }
                }
            }

            context("[이름]- 한글 형식 검증") {
                test("❌- 잘못된 이름 형식으로 회원가입 요청을 하면 예외가 발생한다") {
                    val inValidNameArray = listOf(
                        "Lebron James",
                        "Stephen Curry",
                        "헬로우!!!",
                        "!!wow"
                    )
                    inValidNameArray.forAll {
                        val ex = shouldThrow<IllegalArgumentException> {
                            userRegisterFixture.copy(name = it)
                        }
                        ex.message shouldBe "잘못된 이름 형식입니다"
                    }
                }
                test("✅- 올바른 이름 형식으로 회원가입 요청을 할 수 있다") {
                    val validNameArray = listOf(
                        "홍길동",
                        "이승복",
                        "강호동",
                        "유재석",
                    )
                    validNameArray.forAll {
                        val requestInstance = userRegisterFixture.copy(name = it)
                        requestInstance.shouldNotBeNull()
                    }
                }
            }

            context("[비밀번호] - 대소문자+숫자 5글자 이상 + 특수문자>=2개 형식 검즘") {
                test("❌- 잘못된 비밀번호 형식으로 회원가입 요청을 하면 예외가 발생한다") {
                    val inValidPasswordArray = listOf(
                        "lowercase",
                        "UPPERCASE",
                        "test1",
                        "Test12",
                        "Test12!",
                    )
                    inValidPasswordArray.forAll {
                        val ex = shouldThrow<IllegalArgumentException> {
                            userRegisterFixture.copy(password = it)
                        }
                        ex.message shouldBe "잘못된 비밀번호 형식입니다"
                    }
                }
                test("✅- 올바른 비밀번호 형식으로 회원가입 요청을 할 수 있다") {
                    val validPasswordArray = listOf(
                        "Test1!@",
                        "Test1234!@",
                        "!Test1234@",
                        "Test!@12",
                        "bananA0#@",
                    )
                    validPasswordArray.forAll {
                        val requestInstance = userRegisterFixture.copy(password = it)
                        requestInstance.shouldNotBeNull()
                    }
                }
            }
        }
    },
)
