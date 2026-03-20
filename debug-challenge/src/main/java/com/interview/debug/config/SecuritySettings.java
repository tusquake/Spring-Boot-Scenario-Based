package com.interview.debug.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Demonstrates @ConfigurationProperties with:
 * 1. Hierarchical structures
 * 2. Relaxed Binding (e.g. token-timeout in properties maps to tokenTimeout)
 * 3. JSR-303 Validation
 */
@Component
@ConfigurationProperties(prefix = "app.security")
@Validated
public class SecuritySettings {

    @Min(value = 60, message = "Token timeout must be at least 60 seconds")
    private int tokenTimeout;

    @Min(0)
    private int maxRetries;

    private final Encryption encryption = new Encryption();

    public static class Encryption {
        @NotBlank
        private String algorithm;
        
        @NotNull
        private Integer keySize;

        // Getters and Setters
        public String getAlgorithm() { return algorithm; }
        public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }
        public Integer getKeySize() { return keySize; }
        public void setKeySize(Integer keySize) { this.keySize = keySize; }
    }

    // Getters and Setters
    public int getTokenTimeout() { return tokenTimeout; }
    public void setTokenTimeout(int tokenTimeout) { this.tokenTimeout = tokenTimeout; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public Encryption getEncryption() { return encryption; }
}
