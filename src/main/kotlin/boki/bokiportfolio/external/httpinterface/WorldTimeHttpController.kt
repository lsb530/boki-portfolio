package boki.bokiportfolio.external.httpinterface

import boki.bokiportfolio.external.dto.WorldTimeCustomResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/http-interface/time")
class WorldTimeHttpController(
    // restTemplate
    @Qualifier("restTemplateHttpProxy")
    private val httpProxy1: WorldTimeHttpService,
    // webClient
    @Qualifier("webClientHttpProxy")
    private val httpProxy2: WorldTimeHttpService,
    // restClient
    @Qualifier("restClientHttpProxy")
    private val httpProxy3: WorldTimeHttpService,
) {
    private val log = LoggerFactory.getLogger(WorldTimeHttpController::class.java)

    @GetMapping("/full/{*timezone}")
    fun getFullWorldTime(@PathVariable timezone: String): WorldTimeCustomResponse {
//        val apiResponse = httpProxy.getFullWorldTime(timezone) // SLASH(/) 인코딩
        val uri = URI("http://worldtimeapi.org/api/timezone/${timezone.drop(1)}")
        val apiResponse1 = httpProxy1.getFullWorldTimeFromUri(uri)
        log.info("response1: $apiResponse1")
        val apiResponse2 = httpProxy2.getFullWorldTimeFromUri(uri)
        log.info("response2: $apiResponse2")
        val apiResponse3 = httpProxy3.getFullWorldTimeFromUri(uri)
        log.info("response3: $apiResponse3")
        return apiResponse3
    }

    @GetMapping("/datetime/{*timezone}")
    fun getDateTimeOnly(@PathVariable timezone: String): String {
//        val apiResponse = httpProxy.getDateTimeOnly(timezone) // SLASH(/) 인코딩
        val uri = URI("http://worldtimeapi.org/api/timezone/${timezone.drop(1)}")
        val apiResponse = httpProxy1.getDateTimeOnlyFromUri(uri)
//        val apiResponse = httpProxy2.getDateTimeOnlyFromUri(uri)
//        val apiResponse = httpProxy3.getDateTimeOnlyFromUri(uri)
        val result = apiResponse["datetime"] as String
        log.info("datetime: $result")
        return result
    }
}
