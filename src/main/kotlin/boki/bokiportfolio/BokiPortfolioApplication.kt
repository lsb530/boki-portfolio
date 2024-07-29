package boki.bokiportfolio

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication
class BokiPortfolioApplication {

    private val log = LoggerFactory.getLogger(BokiPortfolioApplication::class.java)

    @Value("\${spring.profiles.active}")
    private val activeProfile: String? = null

    @Bean
    fun printActiveProfile() = CommandLineRunner {
        log.info("Active Profile: $activeProfile")
    }

}

fun main(args: Array<String>) {
    runApplication<BokiPortfolioApplication>(*args)
}
