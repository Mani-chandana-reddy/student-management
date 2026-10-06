package com.chandana.sms.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/student_management";
    private static final String DEFAULT_USER = "postgres";

    private static Connection connection;

    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return (value == null || value.isEmpty()) ? fallback : value;
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = env("DB_URL", DEFAULT_URL);
            String user = env("DB_USER", DEFAULT_USER);
            String password = env("DB_PASSWORD", null);
            if (password == null) {
                log.error("DB_PASSWORD environment variable is not set");
                throw new SQLException("DB_PASSWORD environment variable is not set.");
            }
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(url, user, password);
                log.info("Connected to database at {} as {}", url, user);
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL JDBC driver not found", e);
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            log.error("Error closing connection", e);
        }
    }
}
