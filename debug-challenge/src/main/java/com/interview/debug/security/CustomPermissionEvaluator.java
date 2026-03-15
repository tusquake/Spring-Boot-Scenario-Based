package com.interview.debug.security;

import com.interview.debug.model.BankAccount;
import com.interview.debug.repository.BankAccountRepository;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final BankAccountRepository repository;

    public CustomPermissionEvaluator(BankAccountRepository repository) {
        this.repository = repository;
    }

    /**
     * Used in SpEL: hasPermission(#id, 'bankAccount', 'READ')
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || targetDomainObject == null || !(permission instanceof String)) {
            return false;
        }

        String perm = (String) permission;
        
        // Handle BankAccount object directly
        if (targetDomainObject instanceof BankAccount account) {
            return checkAccountPermission(authentication, account, perm);
        }

        return false;
    }

    /**
     * Used in SpEL: hasPermission(#id, 'com.interview.debug.model.BankAccount', 'READ')
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || targetId == null || targetType == null || !(permission instanceof String)) {
            return false;
        }

        String perm = (String) permission;

        if ("BankAccount".equalsIgnoreCase(targetType) || BankAccount.class.getName().equals(targetType)) {
            return repository.findById((Long) targetId)
                    .map(account -> checkAccountPermission(authentication, account, perm))
                    .orElse(false);
        }

        return false;
    }

    private boolean checkAccountPermission(Authentication authentication, BankAccount account, String permission) {
        // Simple logic: Only owner or ADMIN can READ/WRITE
        boolean isOwner = account.getOwner().equals(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if ("READ".equalsIgnoreCase(permission)) {
            return isOwner || isAdmin;
        }
        if ("WRITE".equalsIgnoreCase(permission)) {
            return isOwner; // Only owner can write, even admins shouldn't withdraw for users? (demo choice)
        }

        return false;
    }
}
