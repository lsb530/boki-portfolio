package boki.bokiportfolio.external.restclient

import boki.bokiportfolio.external.dto.WorldTimeCustomResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/rest-client/time")
class WorldTimeRestClientController(
    private val restClientProxy: WorldTimeRestClientProxy,
) {
    private val log = LoggerFactory.getLogger(WorldTimeRestClientController::class.java)

    @GetMapping("/full/{*timezone}")
    fun getFullWorldTime(@PathVariable timezone: String): WorldTimeCustomResponse {
        val apiResponse = restClientProxy.getFullWorldTime(timezone)
        log.info("response: $apiResponse")
        return apiResponse
    }

    @GetMapping("/datetime/{*timezone}")
    fun getDateTimeOnly(@PathVariable timezone: String): String {
        val apiResponse = restClientProxy.getDateTimeOnly(timezone)
        log.info("datetime: $apiResponse")
        return apiResponse
    }
}
