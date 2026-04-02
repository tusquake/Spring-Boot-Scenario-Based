package com.springai.learning.config;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component // ✅ No need for a custom name anymore
public class Scenario107Config {

    public record PackageRequest(String trackingId) {
    }

    public record PackageResponse(String status) {
    }

    @Tool(description = "Get the current tracking status of a delivery package using its trackingId")
    public PackageResponse trackingFunction(PackageRequest request) {
        System.out.println("🚀 Tool Called: trackingFunction for ID: " + request.trackingId());

        if ("XYZ-123".equalsIgnoreCase(request.trackingId())) {
            return new PackageResponse("📦 Status: Shipped - Arriving Friday, Oct 10");
        } else if ("ABC-999".equalsIgnoreCase(request.trackingId())) {
            return new PackageResponse("✅ Status: Delivered - Left at Front Door");
        } else {
            return new PackageResponse("❌ Error: Tracking ID not found in database");
        }
    }
}