package com.interview.debug.config;

import com.interview.debug.filter.Scenario85SpringFilter;
import com.interview.debug.filter.Scenario85StandardFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Scenario85FilterConfig {

    @Bean
    public FilterRegistrationBean<Scenario85StandardFilter> standardFilterRegistration() {
        FilterRegistrationBean<Scenario85StandardFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new Scenario85StandardFilter());
        registration.addUrlPatterns("/api/scenario85/*");
        registration.setOrder(1); // Run FIRST
        registration.setName("StandardGateFilter");
        return registration;
    }

    @Bean
    public FilterRegistrationBean<Scenario85SpringFilter> springFilterRegistration() {
        FilterRegistrationBean<Scenario85SpringFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new Scenario85SpringFilter());
        registration.addUrlPatterns("/api/scenario85/*");
        registration.setOrder(2); // Run SECOND
        registration.setName("OncePerRequestPassportFilter");
        return registration;
    }
}
