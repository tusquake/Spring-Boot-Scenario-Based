package com.interview.debug.controller;

import com.interview.debug.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario5")
public class Scenario5Controller {

    private final ReportService reportService;

    public Scenario5Controller(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/report/{userId}")
    public String getReport(@PathVariable String userId) {
        long startTime = System.currentTimeMillis();
        String report = reportService.generateUserReport(userId);
        long duration = System.currentTimeMillis() - startTime;
        return "Report: " + report + "\nTime taken: " + duration + "ms";
    }
}
