package boki.bokiportfolio.external.httpinterface

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.client.support.RestTemplateAdapter
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory


@Configuration
class HttpInterfaceConfig {

    @Bean
    fun restTemplateHttpProxy(restTemplate: RestTemplate): WorldTimeHttpService {
        val adapter = RestTemplateAdapter.create(restTemplate)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient(WorldTimeHttpService::class.java)
    }

    @Bean
    fun webClientHttpProxy(webclient: WebClient): WorldTimeHttpService {
        val adapter = WebClientAdapter.create(webclient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient(WorldTimeHttpService::class.java)
    }

    @Bean
    fun restClientHttpProxy(restClient: RestClient): WorldTimeHttpService {
        val adapter = RestClientAdapter.create(restClient)
        val factory = HttpServiceProxyFactory.builderFor(adapter).build()
        return factory.createClient(WorldTimeHttpService::class.java)
    }

}
