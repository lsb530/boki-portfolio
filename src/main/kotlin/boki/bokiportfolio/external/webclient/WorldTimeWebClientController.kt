package boki.bokiportfolio.external.webclient

import boki.bokiportfolio.external.dto.WorldTimeCustomResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/webclient/time")
class WorldTimeWebClientController(
    private val webclientProxy: WorldTimeWebClientProxy,
) {
    private val log = LoggerFactory.getLogger(WorldTimeWebClientController::class.java)

    @GetMapping("/full/{*timezone}")
    fun getFullWorldTime(@PathVariable timezone: String): Mono<WorldTimeCustomResponse> {
        val apiResponse = webclientProxy.getWorldTime(timezone)
        log.info("response: $apiResponse")
        return apiResponse
    }

    @GetMapping("/datetime/{*timezone}")
    fun getDateTimeOnly(@PathVariable timezone: String): Mono<String> {
        val apiResponse = webclientProxy.getDateTimeOnly(timezone)
        log.info("datetime: $apiResponse")
        return apiResponse
    }
}
