package com.interview.debug.service;

import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {

    // Simple in-memory storage for revoked JTI (JWT IDs)
    // In production, use Redis with TTL!
    private final Set<String> blacklistedJtis = new HashSet<>();

    public void blacklistToken(String jti) {
        blacklistedJtis.add(jti);
    }

    public boolean isBlacklisted(String jti) {
        return blacklistedJtis.contains(jti);
    }
}
