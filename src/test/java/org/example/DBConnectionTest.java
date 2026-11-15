package org.example;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    @Test
    void defaultUrlIsUsedWhenEnvNotSet() {
        assertTrue(DBConnection.getUrl().contains("jdbc:h2:file:./data/tempdb"));
        assertTrue(DBConnection.getUrl().contains("AUTO_SERVER=TRUE"));
    }

    @Test
    void getConnectionOpensAndInitializes() throws Exception {
        DBConnection.initializeDatabase();
        try (Connection conn = DBConnection.getConnection()) {
            assertNotNull(conn);
            assertFalse(conn.isClosed());
            assertTrue(conn.getMetaData().getURL().contains("tempdb"));
        }
    }
}
