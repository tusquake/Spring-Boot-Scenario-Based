package com.interview.debug.controller;

import com.interview.debug.service.BetaFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/scenario40")
public class Scenario40Controller {

    // Using Optional because the bean might not exist due to @ConditionalOnProperty
    private final Optional<BetaFeatureService> betaService;

    public Scenario40Controller(@Autowired(required = false) BetaFeatureService betaService) {
        this.betaService = Optional.ofNullable(betaService);
    }

    @GetMapping("/test")
    public String testFeature() {
        if (betaService.isPresent()) {
            return betaService.get().executeBetaLogic();
        }
        return "Beta feature is CURRENTLY DISABLED in application.properties";
    }
}
