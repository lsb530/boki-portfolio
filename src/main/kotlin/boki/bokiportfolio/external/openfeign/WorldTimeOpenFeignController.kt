package boki.bokiportfolio.external.openfeign

import boki.bokiportfolio.external.dto.WorldTimeCustomResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/openfeign/time")
class WorldTimeOpenFeignController(
    private val openFeignProxy: WorldTimeOpenFeignProxy,
    private val objectMapper: ObjectMapper,
) {
    private val log = LoggerFactory.getLogger(WorldTimeOpenFeignController::class.java)

    @GetMapping("/full/{*timezone}")
    fun getFullWorldTime(@PathVariable timezone: String): WorldTimeCustomResponse {
        val apiResponse = openFeignProxy.getWorldTime(timezone)
        log.info("response: $apiResponse")
        return apiResponse
    }

    @GetMapping("/datetime/{*timezone}")
    fun getDateTimeOnly(@PathVariable timezone: String): String {
        val apiResponse = openFeignProxy.getDateTimeOnly(timezone)
        val jsonNode = objectMapper.readTree(apiResponse)
        val result = jsonNode["datetime"].asText()
        log.info("result: $result")
        return result
    }
}
