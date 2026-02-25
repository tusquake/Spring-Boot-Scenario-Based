package com.interview.debug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = { "com.interview.debug", "com.interview.external.service" })
@SpringBootApplication
public class DebugApplication {
    public static void main(String[] args) {
        SpringApplication.run(DebugApplication.class, args);
    }
}
