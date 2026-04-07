package com.interview.debug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.github.tusquake.envvalidator.annotation.EnableEnvValidation;

import org.springframework.retry.annotation.EnableRetry;
import org.springdoc.core.models.GroupedOpenApi;
@SpringBootApplication
@EnableRetry
@EnableScheduling
@EnableAsync
@EnableEnvValidation
@ComponentScan(basePackages = { "com.interview.debug", "com.interview.external.service" })
@EnableJpaRepositories(basePackages = "com.interview.debug.repository")
@EntityScan(basePackages = "com.interview.debug.model")
public class DebugApplication {
    public static void main(String[] args) {
        SpringApplication.run(DebugApplication.class, args);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }
}
