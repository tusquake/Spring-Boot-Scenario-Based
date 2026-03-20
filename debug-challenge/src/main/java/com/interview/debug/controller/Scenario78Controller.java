package com.interview.debug.controller;

import com.interview.debug.service.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario78")
public class Scenario78Controller {

    private final MessageService messageService;
    private final Environment environment;

    @Value("${app.env:NONE}")
    private String appEnv;

    public Scenario78Controller(MessageService messageService, Environment environment) {
        this.messageService = messageService;
        this.environment = environment;
    }

    @GetMapping("/status")
    public Map<String, Object> getProfileStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("active_profiles", environment.getActiveProfiles());
        status.put("default_profiles", environment.getDefaultProfiles());
        status.put("app_env_property", appEnv);
        status.put("message_service_output", messageService.getWelcomeMessage());
        return status;
    }
}
