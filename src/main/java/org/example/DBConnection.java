package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import org.h2.tools.RunScript;

/**
 * Provides JDBC connections to the H2 database and initialises the schema.
 *
 * URL can be overridden with the DB_URL environment variable so the same
 * code works locally, in tests (in-memory) and inside Docker.
 */
public class DBConnection {

    public static final String DEFAULT_URL =
            "jdbc:h2:file:./data/tempdb;AUTO_SERVER=TRUE;MODE=MySQL";
    public static final String DEFAULT_USER = "sa";
    public static final String DEFAULT_PASSWORD = "";

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
     * Create tables and seed data from init.sql if they do not exist yet.
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DROP ALL OBJECTS");

            // Load and execute init.sql
            String initScript = loadInitScript();
            if (initScript != null && !initScript.isBlank()) {
                // Use H2's script runner to execute the SQL script
                RunScript.execute(conn, new BufferedReader(new StringReader(initScript)));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Failed to initialise database", e);
        }
    }

    /**
     * Load init.sql from classpath resources.
     */
    private static String loadInitScript() {
        try (InputStream is = DBConnection.class.getResourceAsStream("/init.sql")) {
            if (is == null) {
                System.err.println("Warning: init.sql not found in resources");
                return null;
            }
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            System.err.println("Warning: Failed to load init.sql: " + e.getMessage());
            return null;
        }
    }
}
