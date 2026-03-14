package com.interview.debug.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scenario44")
public class Scenario44Controller {

    /**
     * SCENARIO 44: REQUEST PARAMETERS
     * Demonstrates @RequestParam with default values and optional handling.
     * 
     * Test with:
     * - /api/scenario44/search?q=pizza (Explicit)
     * - /api/scenario44/search (Uses defaults)
     */
    @GetMapping("/search")
    public String search(
            @RequestParam(name = "q", defaultValue = "everything") String query,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit) {
        
        return String.format("Searching for '%s' [Page: %d, Limit: %d]", 
                query, page, limit);
    }
}
