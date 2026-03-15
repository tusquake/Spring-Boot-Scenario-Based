package com.interview.debug.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import java.time.Instant;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final com.interview.debug.filter.JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(com.interview.debug.filter.JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable()) // Disable CSRF for testing
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/scenario8/login").permitAll()
                .requestMatchers("/api/filter/**").permitAll()
                .requestMatchers("/api/scenario52/**").permitAll()
                .requestMatchers("/api/scenario53/public").permitAll()
                .requestMatchers("/api/scenario8/protected", "/api/scenario8/logout").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration configuration = new org.springframework.web.cors.CorsConfiguration();
        // Demonstrate specific allowed origin
        configuration.setAllowedOrigins(java.util.List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true); // Allow cookies/auth headers
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour

        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    /**
     * For Scenario 53 Demonstration:
     * A mock JwtDecoder that allows us to run the app without a real OIDC Provider.
     * In production, this would be replaced by spring.security.oauth2.resourceserver.jwt.issuer-uri
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return token -> {
            // This is a dummy decoder for demonstration that treats the token string as the scope
            // Usage: Pass 'read' or 'write' as the Authorization Bearer token in curl
            return Jwt.withTokenValue(token)
                .header("alg", "none")
                .claim("scope", token) // The token itself is treated as the scope for demo
                .subject("demo-user")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
        };
    }
}
