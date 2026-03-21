package com.interview.debug.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class Scenario84SecurityConfig {

    @Bean
    @Order(2) // Priority after Scenario 83 if they overlap, but matchers are unique
    public SecurityFilterChain scenario84SecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/scenario84/**")
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/scenario84/**"))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll());
        return http.build();
    }
}
