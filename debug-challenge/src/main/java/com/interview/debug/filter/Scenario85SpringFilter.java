package com.interview.debug.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class Scenario85SpringFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(Scenario85SpringFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        logger.info("[Scenario 85] OncePerRequestFilter: START - Processing at Passport Control.");
        
        // This filter is special because it guarantees only one execution per request
        // even if there are internal forwards or includes.
        
        filterChain.doFilter(request, response);
        
        logger.info("[Scenario 85] OncePerRequestFilter: END - Finished Passport Control.");
    }
}
