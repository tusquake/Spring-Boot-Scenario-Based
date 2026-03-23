package com.interview.debug.util;

import com.interview.debug.config.DataSourceType;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility to store the current DataSourceType in a ThreadLocal.
 */
@Slf4j
public class RoutingContextHolder {

    private static final ThreadLocal<DataSourceType> CONTEXT = new ThreadLocal<>();

    public static void set(DataSourceType dataSourceType) {
        log.debug("Setting DataSourceType to: {}", dataSourceType);
        CONTEXT.set(dataSourceType);
    }

    public static DataSourceType get() {
        return CONTEXT.get();
    }

    public static void clear() {
        log.debug("Clearing DataSourceType context");
        CONTEXT.remove();
    }
}
