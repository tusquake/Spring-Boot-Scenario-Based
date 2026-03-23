package com.interview.debug.config;

/**
 * Enum to distinguish between Read-Write (Primary) and Read-Only (Replica) DataSources.
 */
public enum DataSourceType {
    WRITE,
    READ_ONLY
}
