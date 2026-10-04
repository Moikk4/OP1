package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureUnitTest {

    @Test
    void hasThreeUnits() {
        assertEquals(3, TemperatureUnit.values().length);
    }

    @Test
    void displayNamesAndSymbols() {
        assertEquals("Celsius", TemperatureUnit.CELSIUS.getDisplayName());
        assertEquals("°C", TemperatureUnit.CELSIUS.getSymbol());
        assertEquals("Fahrenheit", TemperatureUnit.FAHRENHEIT.getDisplayName());
        assertEquals("°F", TemperatureUnit.FAHRENHEIT.getSymbol());
        assertEquals("Kelvin", TemperatureUnit.KELVIN.getDisplayName());
        assertEquals("K", TemperatureUnit.KELVIN.getSymbol());
    }

    @Test
    void fromStringIsCaseInsensitive() {
        assertEquals(TemperatureUnit.CELSIUS, TemperatureUnit.fromString("celsius"));
        assertEquals(TemperatureUnit.CELSIUS, TemperatureUnit.fromString("CELSIUS"));
        assertEquals(TemperatureUnit.FAHRENHEIT, TemperatureUnit.fromString("Fahrenheit"));
        assertEquals(TemperatureUnit.KELVIN, TemperatureUnit.fromString("kelvin"));
    }

    @Test
    void fromStringRejectsUnknown() {
        assertThrows(IllegalArgumentException.class, () -> TemperatureUnit.fromString("rankine"));
        assertThrows(IllegalArgumentException.class, () -> TemperatureUnit.fromString(null));
    }

    @Test
    void toStringContainsNameAndSymbol() {
        assertEquals("Celsius (°C)", TemperatureUnit.CELSIUS.toString());
    }
}

