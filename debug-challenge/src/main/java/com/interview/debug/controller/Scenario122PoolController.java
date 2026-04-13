package com.interview.debug.controller;

import com.interview.debug.service.Scenario122PoolService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/scenario122")
@RequiredArgsConstructor
public class Scenario122PoolController {

    private final Scenario122PoolService poolService;
    private final DataSource dataSource;

    @GetMapping("/stats")
    public Map<String, Object> getPoolStats() {
        Map<String, Object> stats = new LinkedHashMap<>();
        List<HikariDataSource> pools = findHikariPools(dataSource);

        if (pools.isEmpty()) {
            stats.put("error", "No HikariDataSource found. Actual type: " + dataSource.getClass().getName());
            return stats;
        }

        for (HikariDataSource pool : pools) {
            Map<String, Object> poolStats = new HashMap<>();
            poolStats.put("active", pool.getHikariPoolMXBean().getActiveConnections());
            poolStats.put("idle", pool.getHikariPoolMXBean().getIdleConnections());
            poolStats.put("total", pool.getHikariPoolMXBean().getTotalConnections());
            poolStats.put("waiting", pool.getHikariPoolMXBean().getThreadsAwaitingConnection());
            poolStats.put("maxSize", pool.getMaximumPoolSize());
            stats.put(pool.getPoolName(), poolStats);
        }

        return stats;
    }

    private List<HikariDataSource> findHikariPools(DataSource ds) {
        List<HikariDataSource> result = new ArrayList<>();
        DataSource unwrapped = unwrap(ds);

        if (unwrapped instanceof HikariDataSource hikari) {
            result.add(hikari);
        } else if (unwrapped instanceof AbstractRoutingDataSource routing) {
            result.addAll(extractPoolsFromRouting(routing));
        }

        return result;
    }

    private DataSource unwrap(DataSource ds) {
        DataSource current = ds;
        // Unwrap DelegatingDataSource (like LazyConnectionDataSourceProxy)
        while (current instanceof DelegatingDataSource delegating) {
            current = delegating.getTargetDataSource();
        }
        // Unwrap Spring CGLIB/JDK Proxies
        while (AopUtils.isAopProxy(current) && current instanceof Advised advised) {
            try {
                current = (DataSource) advised.getTargetSource().getTarget();
            } catch (Exception e) {
                break; 
            }
        }
        // Double check delegating again after proxy unwrapping
        while (current instanceof DelegatingDataSource delegating) {
            current = delegating.getTargetDataSource();
        }
        return current;
    }

    @SuppressWarnings("unchecked")
    private List<HikariDataSource> extractPoolsFromRouting(AbstractRoutingDataSource routing) {
        List<HikariDataSource> pools = new ArrayList<>();
        try {
            Field field = AbstractRoutingDataSource.class.getDeclaredField("resolvedDataSources");
            field.setAccessible(true);
            Map<Object, DataSource> resolved = (Map<Object, DataSource>) field.get(routing);
            if (resolved != null) {
                for (DataSource ds : resolved.values()) {
                    pools.addAll(findHikariPools(ds));
                }
            }
        } catch (Exception e) {
            // Log or handle error
        }
        return pools;
    }

    @GetMapping("/stress")
    public String stressTest(@RequestParam(defaultValue = "5000") int duration) {
        // Run in background so we can see the stats change in real-time
        CompletableFuture.runAsync(() -> poolService.simulateSlowQuery(duration));
        return "Slow query simulation triggered for " + duration + "ms";
    }

    @GetMapping("/leak")
    public String triggerLeak() {
        poolService.simulateLeak();
        return "Connection leak triggered. Check application logs for Hikari leak detection warnings (after 2 seconds).";
    }
}
