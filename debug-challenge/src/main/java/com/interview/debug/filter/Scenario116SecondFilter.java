package com.interview.debug.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Scenario 116: Second Filter
 */
@Component
@Order(2)
public class Scenario116SecondFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        if (request.getRequestURI().contains("/api/scenario116")) {
            // Append to the header set by the First Filter
            String currentOrder = response.getHeader("X-Scenario116-Order");
            response.setHeader("X-Scenario116-Order", (currentOrder != null ? currentOrder + " -> " : "") + "Second");
        }
        filterChain.doFilter(request, response);
    }
}
