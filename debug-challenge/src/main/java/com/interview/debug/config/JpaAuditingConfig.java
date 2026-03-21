package com.interview.debug.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // In a real app, you would get this from SecurityContextHolder.getContext().getAuthentication().getName()
        // For this scenario, we'll mock the auditor as "Tushar (Mock)"
        return () -> Optional.of("Tushar (Mock)");
    }
}
