package boki.bokiportfolio.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.media.StringSchema
import io.swagger.v3.oas.models.parameters.HeaderParameter
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPIConfiguration(): OpenAPI? {
        val securityRequirement = SecurityRequirement()
        securityRequirement.addList("accessToken")
        securityRequirement.addList("refreshToken")

        val accessTokenHeader = HeaderParameter()
            .name("accessToken")
            .description("Access Token")
            .required(true)
            .schema(StringSchema())
            .example("your-access-token")

        val refreshTokenHeader = HeaderParameter()
            .name("refreshToken")
            .description("Refresh Token")
            .required(true)
            .schema(StringSchema())
            .example("your-refresh-token")

        val securityScheme = SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.HEADER)
            .name("accessToken")

        val securityScheme2 = SecurityScheme()
            .type(SecurityScheme.Type.APIKEY)
            .`in`(SecurityScheme.In.HEADER)
            .name("refreshToken")

        val components = Components()
            .addSecuritySchemes("accessToken", securityScheme)
            .addParameters("accessTokenHeader", accessTokenHeader)
            .addSecuritySchemes("refreshToken", securityScheme2)
            .addParameters("refreshTokenHeader", refreshTokenHeader)

        val openAPI = OpenAPI()
            .info(
                Info().title("Boki Portfolio API")
                    .description("Portfolio API 문서")
                    .version("v0.0.1")
                    .license(License().name("Apache 2.0").url("http://springdoc.org"))
            )
            .externalDocs(
                ExternalDocumentation()
                    .description("Blog")
                    .url("https://code-boki.tistory.com")
            )
            .components(components)

        return openAPI
    }
}
