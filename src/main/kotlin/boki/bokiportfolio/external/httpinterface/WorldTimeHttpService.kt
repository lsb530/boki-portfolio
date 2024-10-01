package boki.bokiportfolio.external.httpinterface

import boki.bokiportfolio.external.dto.WorldTimeCustomResponse
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import java.net.URI

//@HttpExchange("http://worldtimeapi.org/api/timezone")
@HttpExchange
interface WorldTimeHttpService {
//    @GetExchange("/{timezone}")
//    fun getFullWorldTime(@PathVariable timezone: String): WorldTimeCustomResponse
//
//    @GetExchange("/{timezone}")
//    fun getDateTimeOnly(@PathVariable timezone: String): Map<String, Any>

    // 슬래시 인코딩 방지: URI를 받아 직접 처리
    @GetExchange
    fun getFullWorldTimeFromUri(uri: URI): WorldTimeCustomResponse

    @GetExchange
    fun getDateTimeOnlyFromUri(uri: URI): Map<String, Any>
}
