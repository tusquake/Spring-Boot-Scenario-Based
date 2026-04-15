package com.interview.debug.controller;

import com.interview.debug.service.Scenario129Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Scenario 129: Transaction Isolation Levels Controller
 */
@RestController
@RequestMapping("/api/scenario129")
@RequiredArgsConstructor
public class Scenario129Controller {

    private final Scenario129Service scenario129Service;

    @GetMapping("/dirty-read")
    public Map<String, Object> dirtyRead(@RequestParam(defaultValue = "false") boolean fixed) {
        return scenario129Service.executeDirtyReadDemo(fixed);
    }

    @GetMapping("/non-repeatable-read")
    public Map<String, Object> nonRepeatableRead(@RequestParam(defaultValue = "false") boolean fixed) {
        return scenario129Service.executeNonRepeatableReadDemo(fixed);
    }

    @GetMapping("/phantom-read")
    public Map<String, Object> phantomRead(@RequestParam(defaultValue = "false") boolean fixed) {
        return scenario129Service.executePhantomReadDemo(fixed);
    }
}
