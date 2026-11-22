package com.example.temperature;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UnitDaoTest {
    private final UnitDao dao = new UnitDao();

    @Test
    void loadsLocalizedName() throws SQLException {
        List<UnitOption> units = dao.findByCategory("temperature", "ja");
        assertFalse(units.isEmpty());
        assertTrue(units.stream().anyMatch(u -> u.getName().contains("セル")));
    }

    @Test
    void fallsBackToEnglishWhenLanguageMissing() throws SQLException {
        List<UnitOption> units = dao.findByCategory("temperature", "fr");
        assertFalse(units.isEmpty());
        assertTrue(units.stream().anyMatch(u -> u.getName().equals("Celsius")));
    }

    @Test
    void returnsEmptyForUnknownCategory() throws SQLException {
        List<UnitOption> units = dao.findByCategory("nonexistent", "en");
        assertTrue(units.isEmpty());
    }

    @Test
    void saveConversionAndFindLastFive() throws SQLException {
        dao.saveConversion(100.0, "C", "F", 212.0, "en");
        dao.saveConversion(0.0, "C", "K", 273.15, "fi");
        List<String> last = dao.findLastFive("en");
        assertFalse(last.isEmpty());
        assertTrue(last.stream().anyMatch(s -> s.contains("212.0")));
    }

    @Test
    void findLastFiveReturnsAtMostFive() throws SQLException {
        for (int i = 0; i < 8; i++) {
            dao.saveConversion(i, "C", "F", i * 1.8 + 32, "en");
        }
        List<String> last = dao.findLastFive("en");
        assertTrue(last.size() <= 5);
    }
}
