package com.interview.debug.config;

import com.interview.debug.util.RoutingContextHolder;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * RoutingDataSource implementation that switches between Primary and Replica.
 */
@Slf4j
public class ReadWriteRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    @Transactional()
    protected Object determineCurrentLookupKey() {
        DataSourceType dataSourceType = RoutingContextHolder.get();
        log.debug("Routing to DataSourceType: {}", dataSourceType != null ? dataSourceType : DataSourceType.WRITE);
        return dataSourceType;
    }
}
