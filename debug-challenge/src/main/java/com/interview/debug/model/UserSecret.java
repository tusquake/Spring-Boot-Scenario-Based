package com.interview.debug.model;

import com.interview.debug.util.EncryptionConverter;
import jakarta.persistence.*;

@Entity
@Table(name = "user_secrets")
public class UserSecret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Convert(converter = EncryptionConverter.class)
    private String sensitiveData; // This will be encrypted in DB

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getSensitiveData() { return sensitiveData; }
    public void setSensitiveData(String sensitiveData) { this.sensitiveData = sensitiveData; }
}
