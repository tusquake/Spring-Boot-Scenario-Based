package com.interview.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario22")
public class Scenario22Controller {

    @GetMapping("/test-limit")
    public String testLimit() {
        return "Success! You are within the rate limit (5 requests per minute).";
    }
}
