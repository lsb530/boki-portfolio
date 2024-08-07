package boki.bokiportfolio

import boki.bokiportfolio.prop.MinioProperties
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean

@EnableCaching
@EnableConfigurationProperties(MinioProperties::class)
@SpringBootApplication
class BokiPortfolioApplication {

    private val log = LoggerFactory.getLogger(BokiPortfolioApplication::class.java)

    @Value("\${spring.profiles.active}")
    private val activeProfile: String? = null

    @Value("\${boki.token.secret}")
    private val secret: String? = null

    @Bean
    fun printActiveProfile() = CommandLineRunner {
        log.info("Active Profile: $activeProfile")
        log.info("jwt secret: $secret")
    }

}

fun main(args: Array<String>) {
    runApplication<BokiPortfolioApplication>(*args)
}
