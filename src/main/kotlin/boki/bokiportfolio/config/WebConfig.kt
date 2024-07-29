package boki.bokiportfolio.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    @Value("\${spring.profiles.active:}")
    private lateinit var activeProfile: String

    private val allowedOrigins = mutableListOf<String>()

    override fun addCorsMappings(registry: CorsRegistry) {
        when (activeProfile) {
            "default", "local", "dev" -> allowedOrigins.add("*")
            "prod" -> {
                (5100..5200).forEach {
                    allowedOrigins.add("http://localhost:$it")
                    allowedOrigins.add("https://localhost:$it")
                }
            }
        }

        registry.addMapping("/api/**")
            .allowedOrigins(*allowedOrigins.toTypedArray())
            .allowedMethods(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name()
            )
    }

}
