package com.WebExcersise.config;

public enum DatabaseType {
    SQLSERVER("jpa-hibernate-sqlserver"),
    MYSQL("jpa-hibernate-mysql");

    private final String persistenceUnitName;

    DatabaseType(String persistenceUnitName) {
        this.persistenceUnitName = persistenceUnitName;
    }

    public String getPersistenceUnitName() {
        return persistenceUnitName;
    }

    public static DatabaseType from(String value) {
        if (value == null || value.isBlank()) {
            return SQLSERVER;
        }

        return switch (value.trim().toLowerCase()) {
            case "mysql" -> MYSQL;
            case "sqlserver", "mssql", "sql-server" -> SQLSERVER;
            default -> SQLSERVER;
        };
    }
}
