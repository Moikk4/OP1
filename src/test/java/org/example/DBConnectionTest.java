package org.example;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    @Test
    void defaultUrlIsUsedWhenEnvNotSet() {
        // DB_URL is not set in the test environment, so the default applies
        assertEquals("jdbc:h2:file:./data/tempdb;AUTO_SERVER=TRUE", DBConnection.getUrl());
    }

    @Test
    void getConnectionOpensAndInitializes() throws Exception {
        DBConnection.initializeDatabase();
        try (Connection conn = DBConnection.getConnection()) {
            assertNotNull(conn);
            assertFalse(conn.isClosed());
        }
    }
}

