package com.interview.debug.event;

/**
 * Event published when a new user is registered.
 */
public class UserRegistrationEvent {
    private final String username;

    public UserRegistrationEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
