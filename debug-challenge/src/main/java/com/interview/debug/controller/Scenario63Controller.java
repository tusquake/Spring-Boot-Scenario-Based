package com.interview.debug.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/scenario63")
public class Scenario63Controller {

    /**
     * Shows the current Session ID.
     * We can use this to see if the session changes after "login" 
     * or if multiple calls use the same session.
     */
    @GetMapping("/session-info")
    public Map<String, Object> getSessionInfo(HttpSession session) {
        return Map.of(
            "sessionId", session.getId(),
            "isNew", session.isNew(),
            "creationTime", session.getCreationTime(),
            "message", "This session is tracked by Spring Security."
        );
    }
}
