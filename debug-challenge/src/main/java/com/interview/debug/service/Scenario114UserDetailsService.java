package com.interview.debug.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Scenario 114: UserDetailsService implementation
 * For this demo, we use a simple hardcoded user.
 * In a real app, this would query the database.
 */
@Service
public class Scenario114UserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("user114".equals(username)) {
            // Hardcoded user: user114 / password114
            return new User("user114", "{noop}password114", new ArrayList<>());
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
