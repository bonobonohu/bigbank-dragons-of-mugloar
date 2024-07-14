package hu.bono.bigbank.dragons.common.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient restClient() {
        final RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new LoggingClientHttpRequestInterceptor()));
        return RestClient.builder(restTemplate)
            .defaultHeader("Cache-Control", "no-store", "no-cache", "max-age=0")
            .defaultHeader("Pragma", "no-cache")
            .defaultHeader("Expires", "0")
            .build();
    }
}
