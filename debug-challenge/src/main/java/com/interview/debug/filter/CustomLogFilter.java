package com.interview.debug.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Note: We REMOVED @Component to prevent Spring Boot from automatically 
// registering this filter for all paths (/*).
public class CustomLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        System.out.println("LOG [Filtered]: Request received for " + request.getRequestURI());

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
