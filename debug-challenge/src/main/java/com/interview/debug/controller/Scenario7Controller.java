package com.interview.debug.controller;

import com.interview.debug.service.AsyncService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/scenario7")
public class Scenario7Controller {

    private final AsyncService asyncService;

    public Scenario7Controller(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @GetMapping("/test")
    public CompletableFuture<String> testAsync() {
        // SIMULATING A LOGGED-IN USER
        org.springframework.security.authentication.UsernamePasswordAuthenticationToken auth = 
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken("Tushar_Seth", "password", 
            org.springframework.security.core.authority.AuthorityUtils.createAuthorityList("ROLE_USER"));
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(auth);

        System.out.println("Controller: User '" + auth.getName() + "' triggered async task on thread: " + Thread.currentThread().getName());
        
        return asyncService.processAsyncTask();
    }
}
