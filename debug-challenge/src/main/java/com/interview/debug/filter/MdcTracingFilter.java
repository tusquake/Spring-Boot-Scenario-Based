package com.interview.debug.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.UUID;

@Component
public class MdcTracingFilter implements Filter {

    private static final String TRACE_ID_KEY = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        try {
            // 1. Generate a unique ID (or take it from a header if provided by an API Gateway)
            String traceId = UUID.randomUUID().toString().substring(0, 8);
            
            // 2. Put it into the Mapped Diagnostic Context (MDC)
            // This stays in the ThreadLocal until the request is finished
            MDC.put(TRACE_ID_KEY, traceId);
            
            chain.doFilter(request, response);
        } finally {
            // 3. CRITICAL: Clear MDC after the request to prevent memory leaks 
            // and log contamination in thread-pooled environments
            MDC.remove(TRACE_ID_KEY);
        }
    }
}
