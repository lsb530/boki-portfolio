package boki.bokiportfolio.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping
    fun test(): ResponseEntity<Any> {
        return ResponseEntity.ok("ready boki")
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/test")
    fun test2(): String {
        return "wowowwowow"
    }
}
