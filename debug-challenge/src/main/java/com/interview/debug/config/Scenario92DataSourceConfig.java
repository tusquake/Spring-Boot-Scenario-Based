package com.interview.debug.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for Read-Write (Primary) and Read-Only (Replica) DataSources.
 */
@Configuration
public class Scenario92DataSourceConfig {

    @Bean
    public DataSource primaryDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:mem:scenario92db;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setPoolName("PrimaryPool");// For identification
        return dataSource;
    }

    @Bean
    public DataSource replicaDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:mem:scenario92db_replica;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setPoolName("ReplicaPool");// For identification
        return dataSource;
    }

    @Bean
    public DataSource readWriteRoutingDataSource() {
        ReadWriteRoutingDataSource routingDataSource = new ReadWriteRoutingDataSource();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.WRITE, primaryDataSource());
        targetDataSources.put(DataSourceType.READ_ONLY, replicaDataSource());

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(primaryDataSource());

        // Initialize schemas in both databases
        initializeDatabase(primaryDataSource());
        initializeDatabase(replicaDataSource());

        return routingDataSource;
    }

    private void initializeDatabase(DataSource dataSource) {
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS scenario93_audits (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "action VARCHAR(255), " +
                    "status VARCHAR(255), " +
                    "detail VARCHAR(255))");
        } catch (Exception e) {
            // It might fail if the table already exist in another way, but IF NOT EXISTS handles it usually.
        }
    }

    /**
     * LazyConnectionDataSourceProxy is CRITICAL because otherwise Spring acquires a connection
     * before the @Transactional annotation is processed by our Aspect.
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(readWriteRoutingDataSource());
    }
}
