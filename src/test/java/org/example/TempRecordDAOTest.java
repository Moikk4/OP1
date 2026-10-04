package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TempRecordDAOTest {

    private Connection conn;
    private TempRecordDAO dao;

    @BeforeEach
    void setUp() throws Exception {
        conn = DriverManager.getConnection("jdbc:h2:mem:temprecordtest;DB_CLOSE_DELAY=-1", "sa", "");
        try (Statement st = conn.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS temp_records ("
                    + " id IDENTITY PRIMARY KEY,"
                    + " input_value DOUBLE NOT NULL,"
                    + " from_unit VARCHAR(20) NOT NULL,"
                    + " to_unit VARCHAR(20) NOT NULL,"
                    + " result_value DOUBLE NOT NULL,"
                    + " created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            st.execute("DELETE FROM temp_records");
        }
        dao = new TempRecordDAO(conn);
    }

    @AfterEach
    void tearDown() throws Exception {
        conn.close();
    }

    @Test
    void saveAssignsId() {
        TempRecord r = dao.save(new TempRecord(100, TemperatureUnit.CELSIUS,
                TemperatureUnit.FAHRENHEIT, 212));
        assertTrue(r.getId() > 0);
    }

    @Test
    void findByIdReturnsSavedRecord() {
        TempRecord saved = dao.save(new TempRecord(300, TemperatureUnit.KELVIN,
                TemperatureUnit.CELSIUS, 26.85));
        TempRecord found = dao.findById(saved.getId());
        assertNotNull(found);
        assertEquals(300, found.getInputValue(), 0.001);
        assertEquals(TemperatureUnit.KELVIN, found.getFromUnit());
        assertEquals(TemperatureUnit.CELSIUS, found.getToUnit());
        assertEquals(26.85, found.getResultValue(), 0.001);
    }

    @Test
    void findByIdReturnsNullWhenMissing() {
        assertNull(dao.findById(99999));
    }

    @Test
    void findAllReturnsEverything() {
        dao.save(new TempRecord(0, TemperatureUnit.CELSIUS, TemperatureUnit.KELVIN, 273.15));
        dao.save(new TempRecord(32, TemperatureUnit.FAHRENHEIT, TemperatureUnit.CELSIUS, 0));
        List<TempRecord> all = dao.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void deleteRemovesRecord() {
        TempRecord saved = dao.save(new TempRecord(1, TemperatureUnit.CELSIUS,
                TemperatureUnit.CELSIUS, 1));
        assertTrue(dao.delete(saved.getId()));
        assertNull(dao.findById(saved.getId()));
        assertFalse(dao.delete(saved.getId()));
    }

    @Test
    void countTracksInserts() {
        assertEquals(0, dao.count());
        dao.save(new TempRecord(1, TemperatureUnit.CELSIUS, TemperatureUnit.CELSIUS, 1));
        dao.save(new TempRecord(2, TemperatureUnit.CELSIUS, TemperatureUnit.CELSIUS, 2));
        assertEquals(2, dao.count());
    }
}

