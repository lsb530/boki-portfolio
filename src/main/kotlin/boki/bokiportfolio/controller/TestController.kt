package boki.bokiportfolio.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping
    fun test(): ResponseEntity<Any> {
        return ResponseEntity.ok("ready boki")
    }

}
