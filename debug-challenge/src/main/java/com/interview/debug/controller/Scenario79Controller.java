package com.interview.debug.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario79")
@Slf4j
public class Scenario79Controller {

    @GetMapping("/log")
    public Map<String, Object> triggerLogs(@RequestParam(defaultValue = "test") String message) {
        log.trace("TRACE level log: {}", message);
        log.debug("DEBUG level log: {}", message);
        log.info("INFO level log: {}", message);
        log.warn("WARN level log: {}", message);
        log.error("ERROR level log: {}", message);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "Logs triggered");
        response.put("message", message);
        response.put("notice", "Check console and logs/app.log file");
        return response;
    }
}
