package com.interview.debug.filter;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class MdcDebuggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(MdcDebuggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        if (mdcContext != null && !mdcContext.isEmpty()) {
            logger.info("Current MDC Keys: {}", mdcContext.keySet());
        } else {
            logger.info("MDC is currently empty!");
        }
        
        chain.doFilter(request, response);
    }
}
