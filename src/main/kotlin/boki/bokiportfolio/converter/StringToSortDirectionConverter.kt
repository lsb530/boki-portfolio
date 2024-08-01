package boki.bokiportfolio.converter

import org.springframework.core.convert.converter.Converter
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class StringToSortDirectionConverter : Converter<String, Sort.Direction> {
    override fun convert(source: String): Sort.Direction {
        return Sort.Direction.valueOf(source.uppercase())
    }
}
