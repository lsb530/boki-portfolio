package boki.bokiportfolio

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@TestPropertySource(properties = ["jwt-secret=test-secret-key"])
@SpringBootTest
class BokiPortfolioApplicationTests {

    @Disabled
    @Test
    fun contextLoads() {
    }

}
