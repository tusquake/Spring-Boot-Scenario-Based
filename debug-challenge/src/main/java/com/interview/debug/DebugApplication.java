package com.interview.debug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = { "com.interview.debug", "com.interview.external.service" })
@EnableJpaRepositories(basePackages = "com.interview.debug.repository")
@EntityScan(basePackages = "com.interview.debug.model")
@SpringBootApplication
public class DebugApplication {
    public static void main(String[] args) {
        SpringApplication.run(DebugApplication.class, args);
    }
}
