package com.interview.debug.model;

import jakarta.persistence.*;

@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountHolder;
    private Double balance;

    @Version // THE FIX: Optimistic Locking version field
    private Integer version;

    public BankAccount() {}

    public BankAccount(String accountHolder, Double balance) {
        this.accountHolder = accountHolder;
        this.balance = balance;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
    public Integer getVersion() { return version; }
}
