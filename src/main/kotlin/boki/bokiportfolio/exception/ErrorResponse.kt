package boki.bokiportfolio.exception

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class ErrorResponse(
    var type: String,
    var status: Int,
    var message: String?,
    var path: String,
    var method: String,
    var timestamp: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
)
