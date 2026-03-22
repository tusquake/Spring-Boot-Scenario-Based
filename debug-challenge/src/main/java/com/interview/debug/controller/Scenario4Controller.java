package com.interview.debug.controller;

import com.interview.debug.service.Scenario4MessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario4")
public class Scenario4Controller {

    private final Scenario4MessageService defaultService;
    private final Scenario4MessageService specificService;

    // We use constructor injection for both.
    // 'defaultService' has no qualifier, so it will pick the @Primary bean (SmsService).
    // 'specificService' has a qualifier, so it will pick the EmailService.
    public Scenario4Controller(
            Scenario4MessageService defaultService,
            @Qualifier("emailService") Scenario4MessageService specificService) {
        this.defaultService = defaultService;
        this.specificService = specificService;
    }

    @GetMapping("/test")
    public Map<String, String> testDisambiguation() {
        Map<String, String> result = new HashMap<>();

        result.put("default_bean_response", defaultService.sendMessage());
        result.put("qualified_bean_response", specificService.sendMessage());
        result.put("analysis", "The default service used @Primary (SMS), while the specific service used @Qualifier (Email).");

        return result;
    }
}
