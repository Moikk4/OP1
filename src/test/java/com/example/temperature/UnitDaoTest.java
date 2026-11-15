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
}

