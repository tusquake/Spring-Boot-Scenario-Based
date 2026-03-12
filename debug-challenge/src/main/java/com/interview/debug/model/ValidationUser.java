package com.interview.debug.model;

import com.interview.debug.validation.OnCreate;
import com.interview.debug.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public class ValidationUser {

    @Null(groups = OnCreate.class, message = "ID must be null when creating a new user")
    @NotBlank(groups = OnUpdate.class, message = "ID is required when updating a user")
    private String id;

    @NotBlank(message = "Username is required")
    private String username;

    @Email(groups = {OnCreate.class, OnUpdate.class}, message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @jakarta.validation.constraints.NotNull(groups = OnCreate.class, message = "Password is required for new users")
    @NotBlank(groups = OnCreate.class, message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
