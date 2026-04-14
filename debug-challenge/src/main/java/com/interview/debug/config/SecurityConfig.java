package com.interview.debug.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import com.interview.debug.security.CustomAuthenticationEntryPoint;
import com.interview.debug.security.CustomAccessDeniedHandler;
import com.interview.debug.service.Scenario114UserDetailsService;
import javax.sql.DataSource;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import java.time.Instant;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final com.interview.debug.filter.JwtAuthenticationFilter jwtFilter;
        private final com.interview.debug.filter.CspNonceFilter cspNonceFilter;
        private final CustomAuthenticationEntryPoint authenticationEntryPoint;
        private final CustomAccessDeniedHandler accessDeniedHandler;
        private final DataSource dataSource;
        private final Scenario114UserDetailsService userDetailsService;

        public SecurityConfig(com.interview.debug.filter.JwtAuthenticationFilter jwtFilter,
                        com.interview.debug.filter.CspNonceFilter cspNonceFilter,
                        CustomAuthenticationEntryPoint authenticationEntryPoint,
                        CustomAccessDeniedHandler accessDeniedHandler,
                        DataSource dataSource,
                        Scenario114UserDetailsService userDetailsService) {
                this.jwtFilter = jwtFilter;
                this.cspNonceFilter = cspNonceFilter;
                this.authenticationEntryPoint = authenticationEntryPoint;
                this.accessDeniedHandler = accessDeniedHandler;
                this.dataSource = dataSource;
                this.userDetailsService = userDetailsService;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        @Bean
        public JwtAuthenticationConverter jwtAuthenticationConverter() {
                JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
                authoritiesConverter.setAuthorityPrefix("ROLE_"); // Convert SCOPE_ADMIN to ROLE_ADMIN
                authoritiesConverter.setAuthoritiesClaimName("scope"); // Or whichever claim holds the authorities

                JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
                jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
                return jwtAuthenticationConverter;
        }

        @Bean
        public RoleHierarchy roleHierarchy() {
                RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
                hierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");
                return hierarchy;
        }

        @Bean
        static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy,
                        PermissionEvaluator permissionEvaluator) {
                DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
                expressionHandler.setRoleHierarchy(roleHierarchy);
                expressionHandler.setPermissionEvaluator(permissionEvaluator);
                return expressionHandler;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(
                                                                org.springframework.security.web.csrf.CookieCsrfTokenRepository
                                                                                .withHttpOnlyFalse())
                                                .ignoringRequestMatchers("/api/scenario51/**", "/api/scenario66/**",
                                                                "/api/scenario67/**",
                                                                "/api/scenario68/**", "/api/scenario69/**",
                                                                "/api/scenario70/**", "/api/scenario71/**",
                                                                "/api/scenario72/**", "/api/scenario73/**",
                                                                "/api/scenario74/**", "/api/scenario75/**",
                                                                "/api/scenario76/**", "/api/scenario77/**",
                                                                "/api/scenario78/**", "/api/scenario79/**",
                                                                "/api/scenario80/**", "/api/scenario81/**",
                                                                "/api/scenario82/**", "/api/scenario83/**",
                                                                "/api/scenario84/**", "/api/scenario85/**",
                                                                "/api/scenario86/**", "/api/scenario87/**",
                                                                "/api/scenario88/**", "/api/scenario89/**",
                                                                "/api/scenario2/**", "/api/scenario3/**",
                                                                "/api/scenario4/**", "/api/scenario38/**",
                                                                "/api/scenario20/**", "/api/scenario34/**",
                                                                "/api/scenario90/**", "/api/scenario91/**",
                                                                "/api/scenario93/**", "/api/scenario94/**",
                                                                "/api/scenario95/**", "/api/scenario96/**",
                                                                "/api/scenario97/**", "/api/scenario98/**",
                                                                "/api/scenario99/**",
                                                                "/api/scenario100/**",
                                                                "/api/scenario101/**",
                                                                "/api/scenario126/**",
                                                                "/api/scenario127/**",
                                                                "/api/scenario128/**",
                                                                "/actuator/**",
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui/**",
                                                                "/api/scenario114/**", // Ignore CSRF for Scenario 114
                                                                                       // login demo
                                                                "/api/scenario117/**", // Ignore CSRF for Scenario 117
                                                                "/api/scenario118/**", // Ignore CSRF for Scenario 118
                                                                "/data-api/**") // Ignore CSRF for Scenario 113
                                )
                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(authenticationEntryPoint)
                                                .accessDeniedHandler(accessDeniedHandler))
                                .sessionManagement(session -> session
                                                // IF_REQUIRED: Spring Security will only create a session if it needs
                                                // one.
                                                // For APIs, STATELESS is usually preferred, but we use IF_REQUIRED here
                                                // to show
                                                // session behavior.
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)

                                                // Session Fixation: Protects against attackers setting a session ID
                                                // before
                                                // login.
                                                // migrateSession(): Creates a new session and copies existing
                                                // attributes.
                                                .sessionFixation(fixation -> fixation.migrateSession())

                                                // Concurrent Session Control: Prevents a user from being logged in
                                                // multiple
                                                // times.
                                                .maximumSessions(1)
                                                // If true, the second login is prevented. If false (default), the first
                                                // session
                                                // is expired.
                                                .maxSessionsPreventsLogin(false))
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/api/scenario8/login").permitAll()
                                                .requestMatchers("/api/filter/**").permitAll()
                                                .requestMatchers("/api/scenario52/**").permitAll()
                                                .requestMatchers("/api/scenario53/public").permitAll()
                                                .requestMatchers("/api/scenario58/admin/**").hasAuthority("ROLE_ADMIN")
                                                .requestMatchers("/api/scenario58/**").permitAll()
                                                .requestMatchers("/api/scenario59/role-test").hasRole("ADMIN")
                                                .requestMatchers("/api/scenario59/authority-test")
                                                .hasAuthority("ROLE_ADMIN")
                                                .requestMatchers("/api/scenario60/user-only").hasRole("USER")
                                                .requestMatchers("/api/scenario61/protected").authenticated()
                                                .requestMatchers("/api/scenario61/admin-only").hasRole("ADMIN")
                                                .requestMatchers("/api/scenario62/permitted").permitAll()
                                                .requestMatchers("/api/scenario64/**").permitAll()
                                                .requestMatchers("/api/scenario65/**").permitAll()
                                                .requestMatchers("/api/scenario66/**").permitAll()
                                                .requestMatchers("/api/scenario67/**").permitAll()
                                                .requestMatchers("/api/scenario68/**").permitAll()
                                                .requestMatchers("/api/scenario69/**").permitAll()
                                                .requestMatchers("/api/scenario70/**").permitAll()
                                                .requestMatchers("/api/scenario71/**").permitAll()
                                                .requestMatchers("/api/scenario72/**").permitAll()
                                                .requestMatchers("/api/scenario73/**").permitAll()
                                                .requestMatchers("/api/scenario74/**").permitAll()
                                                .requestMatchers("/api/scenario75/**").permitAll()
                                                .requestMatchers("/api/scenario76/**").permitAll()
                                                .requestMatchers("/api/scenario77/**").permitAll()
                                                .requestMatchers("/api/scenario78/**").permitAll()
                                                .requestMatchers("/api/scenario79/**").permitAll()
                                                .requestMatchers("/api/scenario80/**").permitAll()
                                                .requestMatchers("/api/scenario81/**").permitAll()
                                                .requestMatchers("/api/scenario82/**").permitAll()
                                                .requestMatchers("/api/scenario83/**", "/api/scenario84/**",
                                                                "/api/scenario85/**",
                                                                "/api/scenario86/**", "/api/scenario87/**",
                                                                "/api/scenario88/**", "/api/scenario89/**",
                                                                "/api/scenario2/**", "/api/scenario3/**",
                                                                "/api/scenario4/**", "/api/scenario38/**",
                                                                "/api/scenario20/**", "/api/scenario34/**",
                                                                "/api/scenario90/**", "/api/scenario91/**",
                                                                "/api/scenario93/**", "/api/scenario94/**",
                                                                "/api/scenario95/**", "/api/scenario96/**",
                                                                "/api/scenario97/**", "/api/scenario98/**",
                                                                "/api/scenario99/**", "/api/scenario100/**",
                                                                "/api/scenario126/**",
                                                                "/api/scenario127/**",
                                                                "/api/scenario128/**")
                                                .permitAll()
                                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                                .requestMatchers("/actuator/health", "/actuator/info",
                                                                "/actuator/metrics",
                                                                "/actuator/system-status")
                                                .permitAll()
                                                .requestMatchers("/actuator/**").hasAuthority("ROLE_ADMIN")
                                                .requestMatchers("/api/scenario8/protected", "/api/scenario8/logout")
                                                .authenticated()
                                                .anyRequest().permitAll())
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt.jwtAuthenticationConverter(
                                                                jwtAuthenticationConverter())))
                                .formLogin(form -> form
                                                .loginPage("/api/scenario114/show-login")
                                                .loginProcessingUrl("/api/scenario114/login")
                                                .defaultSuccessUrl("/api/scenario114/protected", true)
                                                .permitAll())
                                .rememberMe(remember -> remember
                                                .tokenRepository(persistentTokenRepository())
                                                .tokenValiditySeconds(86400) // 1 day
                                                .userDetailsService(userDetailsService)
                                                .rememberMeParameter("remember-me")) // Default is remember-me
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.deny()) // X-Frame-Options: DENY
                                                .contentSecurityPolicy(csp -> csp
                                                                .policyDirectives("frame-ancestors 'none';") // Modern
                                                                                                             // Clickjacking
                                                                                                             // protection
                                                )
                                                .addHeaderWriter((request, response) -> {
                                                        String nonce = (String) request.getAttribute("cspNonce");
                                                        if (nonce != null) {
                                                                // Concatenate existing policy with nonce script-src if
                                                                // needed.
                                                                // Note: .contentSecurityPolicy() above already sets a
                                                                // header,
                                                                // this manual writer might overwrite it if not careful.
                                                                // Best practice: use .contentSecurityPolicy() for
                                                                // static directives
                                                                // and this for dynamic ones.
                                                                String currentCsp = response
                                                                                .getHeader("Content-Security-Policy");
                                                                String scriptCsp = "script-src 'self' 'nonce-" + nonce
                                                                                + "'; object-src 'none';";
                                                                response.setHeader("Content-Security-Policy",
                                                                                (currentCsp != null ? currentCsp + " "
                                                                                                : "") + scriptCsp);
                                                        }
                                                }))
                                .addFilterBefore(cspNonceFilter,
                                                org.springframework.security.web.header.HeaderWriterFilter.class)
                                .addFilterBefore(jwtFilter,
                                                org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
                return http.build();
        }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                // WARNING: This bypasses the entire filter chain.
                // No CSRF, No Security Headers, No Authentication!
                return (web) -> web.ignoring().requestMatchers("/actuator/**");
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
         * In production, this would be replaced by
         * spring.security.oauth2.resourceserver.jwt.issuer-uri
         */
        @Bean
        public JwtDecoder jwtDecoder() {
                return token -> {
                        // This is a dummy decoder for demonstration that treats the token string as the
                        // scope
                        // Usage: Pass 'read' or 'write' as the Authorization Bearer token in curl
                        return Jwt.withTokenValue(token)
                                        .header("alg", "none")
                                        .claim("scope", token) // For RBAC: pass 'ADMIN' or 'USER'
                                        .subject(token) // For Ownership: pass the owner's name (e.g., 'Tushar')
                                        .issuedAt(Instant.now())
                                        .expiresAt(Instant.now().plusSeconds(3600))
                                        .build();
                };
        }

        @Bean
        public PersistentTokenRepository persistentTokenRepository() {
                JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
                tokenRepository.setDataSource(dataSource);
                // Important: If you want Spring to create the table, you use:
                // tokenRepository.setCreateTableOnStartup(true);
                // But since we use Flyway, we create it via SQL script.
                return tokenRepository;
        }

        /**
         * Required for concurrent session management to work correctly.
         * It tells Spring Security when a session has ended so it can update the
         * session registry.
         */
        @Bean
        public HttpSessionEventPublisher httpSessionEventPublisher() {
                return new HttpSessionEventPublisher();
        }

}
