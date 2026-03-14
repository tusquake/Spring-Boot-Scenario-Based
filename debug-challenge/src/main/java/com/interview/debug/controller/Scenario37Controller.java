package com.interview.debug.controller;

import com.interview.debug.service.NotificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario37")
public class Scenario37Controller {

    private final NotificationService notificationService;

    public Scenario37Controller(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notify")
    public String notifyUser(@RequestParam String msg) {
        return notificationService.send(msg);
    }
}
