package boki.bokiportfolio.external.resttemplate

import boki.bokiportfolio.external.dto.WorldTimeCustomResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/rest-template/time")
class WorldTimeRestTemplateController(
    private val restTemplateProxy: WorldTimeRestTemplateProxy
) {
    private val log = LoggerFactory.getLogger(WorldTimeRestTemplateController::class.java)

    @GetMapping("/full/{*timezone}")
    fun getFullWorldTime(@PathVariable timezone: String): WorldTimeCustomResponse {
        val apiResponse = restTemplateProxy.getFullWorldTime(timezone.drop(1))
        log.info("response: $apiResponse")
        return apiResponse
    }

    @GetMapping("/datetime/{*timezone}")
    fun getDateTimeOnly(@PathVariable timezone: String): String {
        val apiResponse = restTemplateProxy.getDateTimeOnly(timezone.drop(1))
        log.info("datetime: $apiResponse")
        return apiResponse
    }
}
