package boki.bokiportfolio.external.webclient

import boki.bokiportfolio.external.dto.WorldTimeCustomResponse
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class WorldTimeWebClientProxy(
    private val webClient: WebClient
) {
    private val baseUrl = "http://worldtimeapi.org/api/timezone"

    fun getWorldTime(timezone: String): Mono<WorldTimeCustomResponse> {
        return webClient.get()
            .uri("$baseUrl/$timezone")
            .retrieve()
            .bodyToMono(WorldTimeCustomResponse::class.java)
    }

    fun getDateTimeOnly(timezone: String): Mono<String> {
        return webClient.get()
            .uri("$baseUrl/$timezone")
            .retrieve()
            .bodyToMono(JsonNode::class.java)
            .map { it["datetime"].asText() }
    }
}
