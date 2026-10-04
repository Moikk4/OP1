package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureUnitDAOTest {

    private Connection conn;
    private TemperatureUnitDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        conn = DriverManager.getConnection("jdbc:h2:mem:tempunittest;DB_CLOSE_DELAY=-1", "sa", "");
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS temperature_units ("
                    + " id IDENTITY PRIMARY KEY,"
                    + " name VARCHAR(50) NOT NULL UNIQUE,"
                    + " symbol VARCHAR(10) NOT NULL)");
            st.execute("DELETE FROM temperature_units");
        }
        dao = new TemperatureUnitDAO(conn);
    }

    @AfterEach
    void tearDown() throws Exception {
        conn.close();
    }

    @Test
    void saveAndFindSymbol() {
        dao.save(TemperatureUnit.CELSIUS);
        assertEquals("°C", dao.findSymbol("CELSIUS"));
    }

    @Test
    void findSymbolReturnsNullWhenMissing() {
        assertNull(dao.findSymbol("NOPE"));
    }

    @Test
    void seedDefaultsInsertsAllUnits() {
        dao.seedDefaults();
        assertEquals(TemperatureUnit.values().length, dao.count());
        List<String> names = dao.findAllNames();
        assertTrue(names.contains("CELSIUS"));
        assertTrue(names.contains("FAHRENHEIT"));
        assertTrue(names.contains("KELVIN"));
    }

    @Test
    void seedDefaultsIsIdempotent() {
        dao.seedDefaults();
        dao.seedDefaults();
        assertEquals(TemperatureUnit.values().length, dao.count());
    }
}

