package boki.bokiportfolio.external.resttemplate

import boki.bokiportfolio.external.dto.WorldTimeCustomResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Component
class WorldTimeRestTemplateProxy(
    private val restTemplate: RestTemplate,
) {
    private val baseUrl = "http://worldtimeapi.org/api/timezone"

    fun getFullWorldTime(timezone: String): WorldTimeCustomResponse {
        val url = UriComponentsBuilder.fromHttpUrl("$baseUrl/$timezone").toUriString()
        return restTemplate.getForObject(url, WorldTimeCustomResponse::class.java)
            ?: throw RuntimeException("Failed to fetch WorldTime")
    }

    fun getDateTimeOnly(timezone: String): String {
        val url = UriComponentsBuilder.fromHttpUrl("$baseUrl/$timezone").toUriString()
        val response= restTemplate.getForObject(url, Map::class.java)
            ?: throw RuntimeException("Failed to fetch WorldTime")
        return response["datetime"] as String
    }
}
