package com.interview.debug.config;

import com.interview.debug.filter.CustomLogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CustomLogFilter> loggingFilter() {
        FilterRegistrationBean<CustomLogFilter> registrationBean = new FilterRegistrationBean<>();

        // Set the filter instance
        registrationBean.setFilter(new CustomLogFilter());

        // Define which URL patterns this filter should apply to
        // Example: Only apply to our orders and checkout endpoints
        registrationBean.addUrlPatterns("/checkout/*", "/orders");

        // Set the execution order (Lower numbers run first)
        registrationBean.setOrder(1);

        return registrationBean;
    }
}
