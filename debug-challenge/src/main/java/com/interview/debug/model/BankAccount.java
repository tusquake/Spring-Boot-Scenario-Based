package com.interview.debug.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@Entity
public class BankAccount {
    @Id
    @GeneratedValue
    private Long id;
    
    private String owner;
    private Double balance;

    @Version // CRITICAL: This enables Optimistic Locking
    private Integer version;

    public BankAccount() {}

    public BankAccount(String owner, Double balance) {
        this.owner = owner;
        this.balance = balance;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
    public Integer getVersion() { return version; }
}
