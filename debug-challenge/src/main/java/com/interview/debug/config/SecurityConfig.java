package com.interview.debug.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final com.interview.debug.filter.JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(com.interview.debug.filter.JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/scenario8/login").permitAll()
                .requestMatchers("/api/filter/**").permitAll()
                .requestMatchers("/api/scenario8/protected", "/api/scenario8/logout").authenticated()
                .anyRequest().permitAll()
            )
            .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
