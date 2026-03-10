package com.interview.debug.service;

import com.interview.debug.model.BankAccount;
import com.interview.debug.repository.BankRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Transactional
    public void depositOptimistic(Long id, Double amount) {
        BankAccount account = bankRepository.findById(id).orElseThrow();
        
        // Simulating some processing delay to increase race condition chance
        try { Thread.sleep(100); } catch (InterruptedException e) {}
        
        account.setBalance(account.getBalance() + amount);
        bankRepository.save(account);
    }

    @Transactional
    public void depositPessimistic(Long id, Double amount) {
        BankAccount account = bankRepository.findByIdWithPessimisticLock(id).orElseThrow();
        account.setBalance(account.getBalance() + amount);
        bankRepository.save(account);
    }

    @Transactional
    public Long createAccount(String name, Double initialBalance) {
        BankAccount account = new BankAccount(name, initialBalance);
        return bankRepository.save(account).getId();
    }

    @Transactional(readOnly = true)
    public Double getBalance(Long id) {
        return bankRepository.findById(id).map(BankAccount::getBalance).orElse(0.0);
    }
}
