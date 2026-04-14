package com.interview.debug.controller;

import com.interview.debug.service.Scenario124MemoryLeakService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/scenario124")
@RequiredArgsConstructor
public class Scenario124MemoryLeakController {

    private final Scenario124MemoryLeakService memoryLeakService;

    @GetMapping("/leak")
    public String triggerLeak(@RequestParam(defaultValue = "10") int count) {
        memoryLeakService.leakMemory(count);
        return String.format("Successfully leaked %d MB into the static cache. Use /actuator/heapdump to analyze.", count);
    }

    @GetMapping("/status")
    public Map<String, Object> getStatus() {
        Map<String, Object> status = new HashMap<>();
        int count = memoryLeakService.getCacheSize();
        status.put("itemsInLeakCache", count);
        status.put("approximateMemoryUsedMB", count); // Since each item is 1MB
        status.put("message", "Trigger more leakage with /leak?count=50");
        return status;
    }

    @GetMapping("/clear")
    public String clearLeak() {
        memoryLeakService.clearMemory();
        return "Leak cache cleared. Note that memory might not be immediately reclaimed until GC runs.";
    }
}
