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
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.Instant;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final com.interview.debug.filter.JwtAuthenticationFilter jwtFilter;
    private final com.interview.debug.filter.CspNonceFilter cspNonceFilter;

    public SecurityConfig(com.interview.debug.filter.JwtAuthenticationFilter jwtFilter, 
                          com.interview.debug.filter.CspNonceFilter cspNonceFilter) {
        this.jwtFilter = jwtFilter;
        this.cspNonceFilter = cspNonceFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf
                .csrfTokenRepository(org.springframework.security.web.csrf.CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/api/scenario51/**") // Keep previous scenarios working for now
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/scenario8/login").permitAll()
                .requestMatchers("/api/filter/**").permitAll()
                .requestMatchers("/api/scenario52/**").permitAll()
                .requestMatchers("/api/scenario53/public").permitAll()
                .requestMatchers("/api/scenario58/admin/**").hasAuthority("SCOPE_ADMIN")
                .requestMatchers("/api/scenario58/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/actuator/**").hasAuthority("SCOPE_ADMIN")
                .requestMatchers("/api/scenario8/protected", "/api/scenario8/logout").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
            .headers(headers -> headers
                .addHeaderWriter((request, response) -> {
                    String nonce = (String) request.getAttribute("cspNonce");
                    if (nonce != null) {
                        response.setHeader("Content-Security-Policy", "script-src 'self' 'nonce-" + nonce + "'; object-src 'none';");
                    }
                })
            )
            .addFilterBefore(cspNonceFilter, org.springframework.security.web.header.HeaderWriterFilter.class)
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

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(PermissionEvaluator permissionEvaluator) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        return expressionHandler;
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
                .claim("scope", token) // For RBAC: pass 'ADMIN' or 'USER'
                .subject(token)       // For Ownership: pass the owner's name (e.g., 'Tushar')
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
        };
    }
}
