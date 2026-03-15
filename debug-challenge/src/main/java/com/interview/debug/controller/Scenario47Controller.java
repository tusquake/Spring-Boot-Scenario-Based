package com.interview.debug.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Scenario47Controller {

    // --- 1. URI VERSIONING (Explicit Path) ---

    @GetMapping("/v1/product")
    public Map<String, Object> getProductV1() {
        return Map.of(
            "version", "V1 (URI Path)",
            "product", "Enterprise Server 2023",
            "price", 5000,
            "supportsCloud", false // V1 doesn't have cloud field in DB usually
        );
    }

    @GetMapping("/v2/product")
    public Map<String, Object> getProductV2() {
        return Map.of(
            "version", "V2 (URI Path)",
            "product", "Enterprise Cloud Suite 2024",
            "price", 7500,
            "supportsCloud", true,
            "subscriptionPlan", "Monthly Gold" // V2 adds new fields
        );
    }

    // --- 2. HEADER VERSIONING (Custom Header) ---

    @GetMapping(value = "/product", headers = "X-API-VERSION=1")
    public Map<String, Object> getProductHeaderV1() {
        return Map.of(
            "version", "V1 (Header)",
            "data", "Standard legacy data format"
        );
    }

    @GetMapping(value = "/product", headers = "X-API-VERSION=2")
    public Map<String, Object> getProductHeaderV2() {
        return Map.of(
            "version", "V2 (Header)",
            "data", "Advanced next-gen data structure"
        );
    }
}
