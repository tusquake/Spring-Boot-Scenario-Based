package com.interview.debug.config;

import com.interview.debug.util.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * RoutingDataSource implementation that uses TenantContext to determine the correct DataSource.
 */
@Slf4j
public class MultiTenantRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        String tenantId = TenantContext.getTenantId();
        log.debug("Routing to tenant DataSource: {}", tenantId);
        return tenantId;
    }
}
