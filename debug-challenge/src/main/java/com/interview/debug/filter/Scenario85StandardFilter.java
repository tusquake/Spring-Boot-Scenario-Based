package com.interview.debug.filter;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class Scenario85StandardFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(Scenario85StandardFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("[Scenario 85] Standard Filter: Initialized and ready at building gate.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        logger.info("[Scenario 85] Standard Filter: START - Pre-processing at building gate.");
        
        chain.doFilter(request, response);
        
        logger.info("[Scenario 85] Standard Filter: END - Post-processing at building gate.");
    }

    @Override
    public void destroy() {
        logger.info("[Scenario 85] Standard Filter: Destroyed.");
    }
}
