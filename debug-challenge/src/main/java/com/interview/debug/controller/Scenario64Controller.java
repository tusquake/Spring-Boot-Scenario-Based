package com.interview.debug.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scenario64")
public class Scenario64Controller {

    private static final Pattern HOSTNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9.-]+$");

    /**
     * VULNERABLE: Uses string concatenation with Runtime.exec().
     * An attacker can pass: "127.0.0.1 && echo INJECTED"
     */
    @GetMapping("/vulnerable/ping")
    public String vulnerablePing(@RequestParam String host) {
        try {
            // DANGEROUS: Command is parsed by the shell if we use fixed strings
            // Note: On Windows, cmd.exe /c is often used which makes it even more vulnerable
            String command = "cmd.exe /c ping -n 1 " + host;
            Process process = Runtime.getRuntime().exec(command);
            
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * SECURE: Uses ProcessBuilder with a list of arguments and regex validation.
     */
    @GetMapping("/secure/ping")
    public String securePing(@RequestParam String host) {
        // 1. Validation: Block any special characters
        if (!HOSTNAME_PATTERN.matcher(host).matches()) {
            return "Invalid hostname format. Only alphanumeric, dots, and hyphens allowed.";
        }

        try {
            // 2. ProcessBuilder: Arguments are passed as a list, NOT a single string.
            // This prevents the OS from interpreting '&', ';', or '|' as command separators.
            List<String> command = new ArrayList<>();
            command.add("ping");
            command.add("-n");
            command.add("1");
            command.add(host);

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
