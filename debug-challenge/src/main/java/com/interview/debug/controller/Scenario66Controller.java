package com.interview.debug.controller;

import com.interview.debug.service.FlickeringService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario66")
public class Scenario66Controller {

    private final FlickeringService flickeringService;

    public Scenario66Controller(FlickeringService flickeringService) {
        this.flickeringService = flickeringService;
    }

    /**
     * Trigger a flickering API call that will retry automatically.
     */
    @GetMapping("/retry")
    public String testRetry() {
        return flickeringService.callExternalApi();
    }
}
