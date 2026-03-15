package com.interview.debug.security;

import com.interview.debug.model.BankAccount;
import com.interview.debug.repository.BankAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("bankSecurity")
public class BankSecurity {

    private final BankAccountRepository repository;

    public BankSecurity(BankAccountRepository repository) {
        this.repository = repository;
    }

    /**
     * Check if the currently authenticated user is the owner of the account.
     * Used in @PreAuthorize("@bankSecurity.isOwner(authentication, #id)")
     */
    public boolean isOwner(Authentication authentication, Long accountId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return repository.findById(accountId)
                .map(account -> account.getOwner().equals(authentication.getName()))
                .orElse(false);
    }

    /**
     * Check if the currently authenticated user is the owner of the given account object.
     * Used in @PostAuthorize("@bankSecurity.isOwner(authentication, returnObject)")
     */
    public boolean isOwner(Authentication authentication, BankAccount account) {
        if (authentication == null || !authentication.isAuthenticated() || account == null) {
            return false;
        }
        return account.getOwner().equals(authentication.getName());
    }
}
