package com.interview.debug.config;

import com.interview.debug.service.Scenario111PostClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.interview.debug.service.Scenario111PostFeignClient;

/**
 * Scenario 111 Configuration: Creating all the Clients
 * Demonstrates the setup for RestTemplate, WebClient, RestClient, and HTTP Interfaces.
 */
@Configuration
@EnableFeignClients(clients = Scenario111PostFeignClient.class)
public class Scenario111Config {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    /**
     * 1. RestTemplate (The OG Client)
     * Simple and synchronous. No longer the recommended approach, but still very common.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 2. WebClient (The Reactive Client)
     * Non-blocking and fluent. The current standard for asynchronous communication.
     */
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.baseUrl(BASE_URL).build();
    }

    /**
     * 3. RestClient (The Modern Synchronous Client)
     * Introduced in Spring 6.1 (Spring Boot 3.2). A fluent alternative to RestTemplate.
     */
    @Bean
    public RestClient restClient() {
        return RestClient.builder().baseUrl(BASE_URL).build();
    }

    /**
     * 4. Declarative Client (HTTP Interface)
     * Uses a proxy to generate the implementation based on an interface.
     */
    @Bean
    public Scenario111PostClient postClient(WebClient webClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();
        return factory.createClient(Scenario111PostClient.class);
    }
}
