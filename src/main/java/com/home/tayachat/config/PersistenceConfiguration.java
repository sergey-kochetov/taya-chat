package com.home.tayachat.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

@Configuration
@PropertySource(factory = TayaPropertySourceFactory.class, value = "classpath:datasource.yml")
@ConfigurationProperties(prefix = "taya.datasource")
@Getter
@Setter
@NoArgsConstructor
public class PersistenceConfiguration {
    protected String url;
    protected String username;
    protected String password;
    protected int poolSize;
    protected int idleTimeout;
    protected int minIdle;
    protected String pingSql;
    protected String dialect;
    protected String type;
    protected boolean showSql;
    protected String databaseInitPolicy;
    protected Properties jpaProperties;
}