package com.interview.debug.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scenario92")
@RequiredArgsConstructor
@Slf4j
public class Scenario92Controller {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/write")
    @Transactional
    public Map<String, String> testWrite() {
        log.info("Testing Write (Primary) DataSource");
        String dbName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        
        Map<String, String> response = new HashMap<>();
        response.put("mode", "WRITE");
        response.put("database", dbName);
        return response;
    }

    @GetMapping("/read")
    @Transactional(readOnly = true)
    public Map<String, String> testRead() {
        log.info("Testing Read (Replica) DataSource");
        String dbName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        
        Map<String, String> response = new HashMap<>();
        response.put("mode", "READ_ONLY");
        response.put("database", dbName);
        return response;
    }
}
