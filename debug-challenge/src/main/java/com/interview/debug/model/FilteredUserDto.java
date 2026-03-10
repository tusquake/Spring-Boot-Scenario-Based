package com.interview.debug.model;

import com.fasterxml.jackson.annotation.JsonFilter;

@JsonFilter("UserFilter")
public class FilteredUserDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String role;

    public FilteredUserDto(Long id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
