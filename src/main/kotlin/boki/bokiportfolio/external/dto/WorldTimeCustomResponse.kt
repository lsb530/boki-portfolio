package boki.bokiportfolio.external.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class WorldTimeCustomResponse(
    @JsonProperty("utc_offset") val utcOffset: String,
    @JsonProperty("timezone") val timezone: String,
    @JsonProperty("datetime") val datetime: String,
    @JsonProperty("utc_datetime") val utcDatetime: String,
    @JsonProperty("unixtime") val unixTime: Long,
    @JsonProperty("abbreviation") val abbreviation: String,
    @JsonProperty("client_ip") val clientIp: String
) {
    constructor() : this("", "", "", "", 0L, "", "")
}
