package com.interview.debug.controller;

import com.interview.debug.service.Scenario123DeadlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scenario123")
@RequiredArgsConstructor
public class Scenario123DeadlockController {

    private final Scenario123DeadlockService deadlockService;

    @GetMapping("/trigger")
    public String triggerDeadlock() {
        deadlockService.triggerDeadlock();
        return "Deadlock simulation initiated between two background threads.\n" +
               "Threads Involved:\n" +
               "1. Deadlock-Thread-Alpha (Locks obj1 -> waits -> attempts lock obj2)\n" +
               "2. Deadlock-Thread-Beta (Locks obj2 -> waits -> attempts lock obj1)\n\n" +
               "Instructions:\n" +
               "1. Check server console for locking logs.\n" +
               "2. Use Actuator: GET /actuator/threaddump (search for 'deadlock')\n" +
               "3. Use CLI: jstack <PID>";
    }
}
