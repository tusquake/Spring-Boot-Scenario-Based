package com.interview.debug.controller;

import com.interview.debug.starter.CustomBannerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/scenario18")
public class Scenario18Controller {

    // We use Optional because the bean might NOT exist if the starter is disabled!
    private final Optional<CustomBannerService> bannerService;

    public Scenario18Controller(Optional<CustomBannerService> bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("/check-starter")
    public String checkStarter() {
        if (bannerService.isPresent()) {
            bannerService.get().printBanner();
            return "Custom Starter is ACTIVE! Check your console/logs for the banner.";
        } else {
            return "Custom Starter is DISABLED. Try setting 'custom.starter.enabled=true' in properties.";
        }
    }
}
