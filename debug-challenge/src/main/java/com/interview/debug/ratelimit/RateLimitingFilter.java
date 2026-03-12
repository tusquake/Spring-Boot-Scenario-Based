package com.interview.debug.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RateLimitingFilter implements Filter {

    private final RateLimitingService rateLimitingService;

    public RateLimitingFilter(RateLimitingService rateLimitingService) {
        this.rateLimitingService = rateLimitingService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Only apply to Scenario 22 endpoints
        if (!httpRequest.getRequestURI().startsWith("/api/scenario22")) {
            chain.doFilter(request, response);
            return;
        }

        // Use IP address or a dummy API key for demo
        String clientIdentifier = httpRequest.getRemoteAddr();
        Bucket bucket = rateLimitingService.resolveBucket(clientIdentifier);

        // Try to consume 1 token
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        
        if (probe.isConsumed()) {
            httpResponse.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            chain.doFilter(request, response);
        } else {
            // Limit exceeded
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            httpResponse.setStatus(429);
            httpResponse.setHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            httpResponse.getWriter().write("Too Many Requests. Please wait " + waitForRefill + " seconds.");
        }
    }
}
