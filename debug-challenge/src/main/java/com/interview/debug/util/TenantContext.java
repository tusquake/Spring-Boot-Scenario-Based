package com.interview.debug.util;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class to store and retrieve the current tenant ID from a ThreadLocal.
 */
@Slf4j
public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static void setTenantId(String tenantId) {
        log.debug("Setting current tenant to: {}", tenantId);
        CURRENT_TENANT.set(tenantId);
    }

    public static String getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        log.debug("Clearing current tenant context");
        CURRENT_TENANT.remove();
    }
}
