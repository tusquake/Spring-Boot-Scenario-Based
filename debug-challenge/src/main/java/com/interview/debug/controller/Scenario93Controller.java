package com.interview.debug.controller;

import com.interview.debug.model.Scenario93Audit;
import com.interview.debug.service.Scenario93Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/scenario93")
@RequiredArgsConstructor
@Slf4j
public class Scenario93Controller {

    private final Scenario93Service scenario93Service;

    @GetMapping("/audits")
    public List<Scenario93Audit> getAudits() {
        return scenario93Service.getAllAudits();
    }

    @DeleteMapping("/audits")
    public String clearAudits() {
        scenario93Service.clearAudits();
        return "Audits cleared.";
    }

    @PostMapping("/propagation/requires-new")
    public String testRequiresNew(@RequestParam(defaultValue = "true") boolean failOuter) {
        try {
            scenario93Service.testRequiresNew(failOuter);
            return "Outer transaction completed successfully.";
        } catch (Exception e) {
            return "Outer transaction failed: " + e.getMessage();
        }
    }

    @PostMapping("/self-invocation")
    public String testSelfInvocation() {
        try {
            scenario93Service.testSelfInvocationEntrance();
            return "Self-invocation demo finished.";
        } catch (Exception e) {
            return "Self-invocation demo caught exception: " + e.getMessage();
        }
    }

    @PostMapping("/rollback/checked")
    public String testCheckedRollback() throws IOException {
        try {
            scenario93Service.testCheckedExceptionRollback();
            return "Completed.";
        } catch (Exception e) {
            return "Checked Exception caught: " + e.getMessage();
        }
    }

    @PostMapping("/rollback/unchecked")
    public String testUncheckedRollback() {
        try {
            scenario93Service.testUncheckedExceptionRollback();
            return "Completed.";
        } catch (Exception e) {
            return "Unchecked Exception caught: " + e.getMessage();
        }
    }

    @GetMapping("/isolation/repeatable-read")
    public List<Scenario93Audit> testRepeatableRead() {
        return scenario93Service.testRepeatableRead();
    }
}
