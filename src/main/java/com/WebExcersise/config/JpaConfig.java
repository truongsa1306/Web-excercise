package com.WebExcersise.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public final class JpaConfig {
    private static final Map<DatabaseType, EntityManagerFactory> FACTORIES = new EnumMap<>(DatabaseType.class);

    private JpaConfig() {
    }

    public static EntityManager getEntityManager() {
        return getEntityManager(getDefaultDatabaseType());
    }

    public static EntityManager getEntityManager(DatabaseType databaseType) {
        return getEntityManagerFactory(databaseType).createEntityManager();
    }

    public static DatabaseType getDefaultDatabaseType() {
        String configuredDatabase = System.getProperty("app.database");
        if (configuredDatabase == null || configuredDatabase.isBlank()) {
            configuredDatabase = System.getenv("APP_DATABASE");
        }
        return DatabaseType.from(configuredDatabase);
    }

    public static synchronized EntityManagerFactory getEntityManagerFactory(DatabaseType databaseType) {
        return FACTORIES.computeIfAbsent(databaseType, type ->
                Persistence.createEntityManagerFactory(type.getPersistenceUnitName(), getJpaOverrides()));
    }

    public static synchronized void shutdown() {
        FACTORIES.values().forEach(factory -> {
            if (factory.isOpen()) {
                factory.close();
            }
        });
        FACTORIES.clear();
    }

    private static Map<String, String> getJpaOverrides() {
        Map<String, String> properties = new HashMap<>();
        putIfConfigured(properties, "jakarta.persistence.jdbc.driver", "app.db.driver", "APP_DB_DRIVER");
        putIfConfigured(properties, "jakarta.persistence.jdbc.url", "app.db.url", "APP_DB_URL");
        putIfConfigured(properties, "jakarta.persistence.jdbc.user", "app.db.user", "APP_DB_USER");
        putIfConfigured(properties, "jakarta.persistence.jdbc.password", "app.db.password", "APP_DB_PASSWORD");
        return properties;
    }

    private static void putIfConfigured(Map<String, String> properties, String jpaKey, String systemKey, String envKey) {
        String value = System.getProperty(systemKey);
        if (value == null || value.isBlank()) {
            value = System.getenv(envKey);
        }
        if (value != null && !value.isBlank()) {
            properties.put(jpaKey, value);
        }
    }
}
