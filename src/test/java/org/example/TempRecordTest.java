package org.example;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TempRecordTest {

    @Test
    void constructorSetsFields() {
        TempRecord r = new TempRecord(100, TemperatureUnit.CELSIUS,
                TemperatureUnit.FAHRENHEIT, 212);
        assertEquals(100, r.getInputValue(), 0.001);
        assertEquals(TemperatureUnit.CELSIUS, r.getFromUnit());
        assertEquals(TemperatureUnit.FAHRENHEIT, r.getToUnit());
        assertEquals(212, r.getResultValue(), 0.001);
        assertNotNull(r.getCreatedAt());
    }

    @Test
    void settersAndGetters() {
        TempRecord r = new TempRecord();
        r.setId(7);
        r.setInputValue(32);
        r.setFromUnit(TemperatureUnit.FAHRENHEIT);
        r.setToUnit(TemperatureUnit.CELSIUS);
        r.setResultValue(0);
        LocalDateTime now = LocalDateTime.now();
        r.setCreatedAt(now);

        assertEquals(7, r.getId());
        assertEquals(32, r.getInputValue(), 0.001);
        assertEquals(TemperatureUnit.FAHRENHEIT, r.getFromUnit());
        assertEquals(TemperatureUnit.CELSIUS, r.getToUnit());
        assertEquals(0, r.getResultValue(), 0.001);
        assertEquals(now, r.getCreatedAt());
    }

    @Test
    void equalsAndHashCode() {
        LocalDateTime t = LocalDateTime.of(2026, 1, 1, 12, 0);
        TempRecord a = new TempRecord(1, 100, TemperatureUnit.CELSIUS,
                TemperatureUnit.FAHRENHEIT, 212, t);
        TempRecord b = new TempRecord(1, 100, TemperatureUnit.CELSIUS,
                TemperatureUnit.FAHRENHEIT, 212, t);
        TempRecord c = new TempRecord(2, 0, TemperatureUnit.KELVIN,
                TemperatureUnit.CELSIUS, -273.15, t);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
        assertNotEquals(a, null);
        assertNotEquals(a, "not a record");
        assertEquals(a, a);
    }

    @Test
    void toStringFormatsConversion() {
        TempRecord r = new TempRecord(1, 100, TemperatureUnit.CELSIUS,
                TemperatureUnit.FAHRENHEIT, 212, LocalDateTime.now());
        assertEquals("100.00 °C = 212.00 °F", r.toString());
    }
}

