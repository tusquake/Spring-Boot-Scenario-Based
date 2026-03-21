package com.interview.debug.config;

import com.interview.debug.interceptor.Scenario84LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Scenario84WebConfig implements WebMvcConfigurer {

    private final Scenario84LoggingInterceptor loggingInterceptor;

    public Scenario84WebConfig(Scenario84LoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the interceptor and apply it only to Scenario 84 paths
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/scenario84/**");
    }
}
