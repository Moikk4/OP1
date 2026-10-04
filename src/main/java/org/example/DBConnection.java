package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Provides JDBC connections to the H2 database and initialises the schema.
 *
 * URL can be overridden with the DB_URL environment variable so the same
 * code works locally, in tests (in-memory) and inside Docker.
 */
public class DBConnection {

    public static final String DEFAULT_URL =
            "jdbc:h2:file:./data/tempdb;AUTO_SERVER=TRUE";
    public static final String DEFAULT_USER = "sa";
    public static final String DEFAULT_PASSWORD = "";

    private static final String CREATE_TEMP_RECORDS =
            "CREATE TABLE IF NOT EXISTS temp_records ("
                    + " id IDENTITY PRIMARY KEY,"
                    + " input_value DOUBLE NOT NULL,"
                    + " from_unit VARCHAR(20) NOT NULL,"
                    + " to_unit VARCHAR(20) NOT NULL,"
                    + " result_value DOUBLE NOT NULL,"
                    + " created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP"
                    + " )";

    private static final String CREATE_TEMPERATURE_UNITS =
            "CREATE TABLE IF NOT EXISTS temperature_units ("
                    + " id IDENTITY PRIMARY KEY,"
                    + " name VARCHAR(50) NOT NULL UNIQUE,"
                    + " symbol VARCHAR(10) NOT NULL"
                    + " )";

    private DBConnection() {
        // utility class
    }

    public static String getUrl() {
        String url = System.getenv("DB_URL");
        return (url == null || url.isBlank()) ? DEFAULT_URL : url;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl(), DEFAULT_USER, DEFAULT_PASSWORD);
    }

    /**
     * Create tables if they do not exist yet.
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TEMP_RECORDS);
            stmt.execute(CREATE_TEMPERATURE_UNITS);
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to initialise database", e);
        }
    }
}

