package boki.bokiportfolio

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeExactly

class JustTest : BehaviorSpec({

    context("1더하기 2는 3이다") {
        Given("1과 2가 있고") {
            val one = 1
            val two = 2
            When("두개를 더하면") {
                val actual = one + two
                Then("3이 된다") {
                    val expected = 3
                    actual shouldBeExactly expected
                }
            }
        }
    }

})
