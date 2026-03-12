package com.interview.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scenario16")
public class Scenario16Controller {

    @GetMapping("/long-process")
    public String longProcess() throws InterruptedException {
        System.out.println("Starting a 15-second long process...");
        
        // Simulate a long-running task (e.g., file upload, report generation)
        Thread.sleep(15000); 
        
        System.out.println("Process finished successfully!");
        return "Process completed after 15 seconds!";
    }
}
