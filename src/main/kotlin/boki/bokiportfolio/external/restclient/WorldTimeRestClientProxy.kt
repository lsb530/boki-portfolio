package boki.bokiportfolio.external.restclient

import boki.bokiportfolio.external.dto.WorldTimeCustomResponse
import com.fasterxml.jackson.databind.JsonNode
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Component
class WorldTimeRestClientProxy(
    private val restClient: RestClient,
) {
    private val baseUrl = "http://worldtimeapi.org/api/timezone"

    fun getFullWorldTime(timezone: String): WorldTimeCustomResponse {
        return restClient.get()
            .uri("$baseUrl/$timezone")
            .retrieve()
            .body<WorldTimeCustomResponse>()
            ?: throw RuntimeException("Failed to fetch WorldTime")
    }

    fun getDateTimeOnly(timezone: String): String {
        val response = restClient.get()
            .uri("$baseUrl/$timezone")
            .retrieve()
            .body<JsonNode>()

        return response?.get("datetime")?.asText()
            ?: throw RuntimeException("Failed to extract datetime from response")
    }
}
