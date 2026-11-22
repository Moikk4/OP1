package com.example.temperature.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureConverterTest {

    @Test
    void convertsCelsiusToFahrenheitAndKelvin() {
        assertEquals(212.0, TemperatureConverter.convert(100, "celsius", "fahrenheit"), 0.001);
        assertEquals(373.15, TemperatureConverter.convert(100, "celsius", "kelvin"), 0.001);
    }

    @Test
    void convertsFahrenheitToCelsiusAndKelvin() {
        assertEquals(100.0, TemperatureConverter.convert(212, "fahrenheit", "celsius"), 0.001);
        assertEquals(373.15, TemperatureConverter.convert(212, "fahrenheit", "kelvin"), 0.001);
    }

    @Test
    void convertsKelvinToCelsiusAndFahrenheit() {
        assertEquals(100.0, TemperatureConverter.convert(373.15, "kelvin", "celsius"), 0.001);
        assertEquals(212.0, TemperatureConverter.convert(373.15, "kelvin", "fahrenheit"), 0.001);
    }

    @Test
    void sameUnitReturnsValue() {
        assertEquals(42.0, TemperatureConverter.convert(42, "celsius", "celsius"), 0.001);
    }

    @Test
    void rejectsUnknownFromUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> TemperatureConverter.convert(1, "rankine", "celsius"));
    }

    @Test
    void rejectsUnknownToUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> TemperatureConverter.convert(1, "celsius", "rankine"));
    }
}

