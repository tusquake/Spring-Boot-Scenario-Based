package com.interview.debug.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// THE MAGIC: This bean only gets created if 'custom.starter.enabled' is 'true'
@ConditionalOnProperty(name = "custom.starter.enabled", havingValue = "true", matchIfMissing = true)
public class CustomStarterAutoConfiguration {

    @Bean
    public CustomBannerService customBannerService() {
        return new CustomBannerService("Auto-Configured for Interview Demo!");
    }
}
