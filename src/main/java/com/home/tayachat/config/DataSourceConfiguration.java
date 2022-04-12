package com.home.tayachat.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Configuration
@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
@EnableTransactionManagement
//@EnableJpaRepositories(PACKAGE_TO_SCAN)
@ComponentScan(basePackages = "com.home")
public class DataSourceConfiguration {
    private final PersistenceConfiguration persistenceConfiguration;

    @Bean
    public DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(persistenceConfiguration.getUrl());
        hikariConfig.setUsername(persistenceConfiguration.getUsername());
        hikariConfig.setPassword(persistenceConfiguration.getPassword());
        if (persistenceConfiguration.getPoolSize() > 0)
            hikariConfig.setMaximumPoolSize(persistenceConfiguration.getPoolSize());
        if (persistenceConfiguration.getIdleTimeout() > 0)
            hikariConfig.setIdleTimeout(persistenceConfiguration.getIdleTimeout());
        if (persistenceConfiguration.getMinIdle() > 0)
            hikariConfig.setMinimumIdle(persistenceConfiguration.getMinIdle());
        hikariConfig.setConnectionTestQuery(StringUtils.isEmpty(persistenceConfiguration.getPingSql())
                ? "select 1"
                : persistenceConfiguration.getPingSql());
        log.info("Initialising datasource connection within:\n\tUrl: {}\n\tUsername: {}\n\tPooled: {}",
                persistenceConfiguration.getUrl(),
                persistenceConfiguration.getUsername(),
                persistenceConfiguration.getPoolSize() > 0);
        return new HikariDataSource(hikariConfig);
    }
}
