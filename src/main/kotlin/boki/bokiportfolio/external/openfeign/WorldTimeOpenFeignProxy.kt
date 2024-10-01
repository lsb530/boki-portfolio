package boki.bokiportfolio.external.openfeign

import boki.bokiportfolio.external.dto.WorldTimeCustomResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@FeignClient(name = "worldTimeApi", url = "http://worldtimeapi.org/api")
interface WorldTimeOpenFeignProxy {
    @GetMapping("/timezone/{timezone}")
    fun getWorldTime(@PathVariable("timezone") timezone: String): WorldTimeCustomResponse

    @GetMapping("/timezone/{timezone}")
    fun getDateTimeOnly(@PathVariable("timezone") timezone: String): String
}
