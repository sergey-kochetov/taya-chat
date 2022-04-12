package com.home.tayachat.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TayaPropertySourceFactory implements PropertySourceFactory {
    public TayaPropertySourceFactory() {
    }

    public @NonNull PropertySource<?> createPropertySource(@Nullable String name, @NonNull EncodedResource resource) throws IOException {
        Properties propertiesFromYaml = this.loadYamlIntoProperties(resource);
        name = name != null ? name : resource.getResource().getFilename();
        name  = name != null ? name : "";
        propertiesFromYaml.putAll(this.loadSystemProperties());
        return new PropertiesPropertySource(name, propertiesFromYaml);
    }

    private Properties loadSystemProperties() {
        return System.getProperties();
    }

    private Properties loadYamlIntoProperties(EncodedResource resource) throws FileNotFoundException {
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (IllegalStateException ex) {
            Throwable cause = ex.getCause();
            if (cause instanceof FileNotFoundException) {
                throw (FileNotFoundException) ex.getCause();
            } else {
                throw ex;
            }
        }
    }
}
