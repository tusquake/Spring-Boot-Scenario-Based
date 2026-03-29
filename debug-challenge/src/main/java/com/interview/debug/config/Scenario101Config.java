package com.interview.debug.config;

import com.interview.debug.repository.Scenario101Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import org.springframework.web.client.support.RestClientAdapter;

/**
 * Scenario 101: Declarative HTTP Clients (@HttpExchange).
 * Configuration for the declarative client proxy.
 */
@Configuration
public class Scenario101Config {

    @Bean
    public Scenario101Client scenario101Client() {
        // Step 1: Initialize the RestClient with the base URL
        RestClient restClient = RestClient.builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .build();

        // Step 2: Create a proxy factory using the modern RestClientAdapter
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        // Step 3: Create the proxy for the interface
        return factory.createClient(Scenario101Client.class);
    }
}
