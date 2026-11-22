package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TempCalculatorTest {

    private final TempCalculator calc = new TempCalculator();

    @Test
    void celsiusToFahrenheit() {
        assertEquals(32.0, calc.celsiusToFahrenheit(0), 0.001);
        assertEquals(212.0, calc.celsiusToFahrenheit(100), 0.001);
        assertEquals(-40.0, calc.celsiusToFahrenheit(-40), 0.001);
    }

    @Test
    void fahrenheitToCelsius() {
        assertEquals(0.0, calc.fahrenheitToCelsius(32), 0.001);
        assertEquals(100.0, calc.fahrenheitToCelsius(212), 0.001);
    }

    @Test
    void celsiusToKelvin() {
        assertEquals(273.15, calc.celsiusToKelvin(0), 0.001);
        assertEquals(373.15, calc.celsiusToKelvin(100), 0.001);
    }

    @Test
    void kelvinToCelsius() {
        assertEquals(0.0, calc.kelvinToCelsius(273.15), 0.001);
        assertEquals(-273.15, calc.kelvinToCelsius(0), 0.001);
    }

    @Test
    void fahrenheitToKelvin() {
        assertEquals(273.15, calc.fahrenheitToKelvin(32), 0.001);
    }

    @Test
    void kelvinToFahrenheit() {
        assertEquals(32.0, calc.kelvinToFahrenheit(273.15), 0.001);
    }

    @Test
    void convertBetweenAllUnits() {
        assertEquals(212.0, calc.convert(100, TemperatureUnit.CELSIUS, TemperatureUnit.FAHRENHEIT), 0.001);
        assertEquals(373.15, calc.convert(100, TemperatureUnit.CELSIUS, TemperatureUnit.KELVIN), 0.001);
        assertEquals(100.0, calc.convert(212, TemperatureUnit.FAHRENHEIT, TemperatureUnit.CELSIUS), 0.001);
        assertEquals(100.0, calc.convert(373.15, TemperatureUnit.KELVIN, TemperatureUnit.CELSIUS), 0.001);
        assertEquals(50.0, calc.convert(50, TemperatureUnit.CELSIUS, TemperatureUnit.CELSIUS), 0.001);
    }

    @Test
    void convertRejectsNullUnits() {
        assertThrows(IllegalArgumentException.class,
                () -> calc.convert(1, null, TemperatureUnit.CELSIUS));
        assertThrows(IllegalArgumentException.class,
                () -> calc.convert(1, TemperatureUnit.CELSIUS, null));
    }

    @Test
    void toCelsiusCoversAllBranches() {
        assertEquals(10.0, calc.toCelsius(10, TemperatureUnit.CELSIUS), 0.001);
        assertEquals(10.0, calc.toCelsius(50, TemperatureUnit.FAHRENHEIT), 0.001);
        assertEquals(10.0, calc.toCelsius(283.15, TemperatureUnit.KELVIN), 0.001);
    }

    @Test
    void fromCelsiusCoversAllBranches() {
        assertEquals(10.0, calc.fromCelsius(10, TemperatureUnit.CELSIUS), 0.001);
        assertEquals(50.0, calc.fromCelsius(10, TemperatureUnit.FAHRENHEIT), 0.001);
        assertEquals(283.15, calc.fromCelsius(10, TemperatureUnit.KELVIN), 0.001);
    }

    @Test
    void isExtremeTemperature() {
        assertTrue(calc.isExtremeTemperature(-41));
        assertTrue(calc.isExtremeTemperature(51));
        assertFalse(calc.isExtremeTemperature(-40));
        assertFalse(calc.isExtremeTemperature(50));
        assertFalse(calc.isExtremeTemperature(20));
    }
}

