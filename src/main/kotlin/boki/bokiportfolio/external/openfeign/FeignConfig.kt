package boki.bokiportfolio.external.openfeign

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients(basePackageClasses = [WorldTimeOpenFeignController::class])
class FeignConfig
