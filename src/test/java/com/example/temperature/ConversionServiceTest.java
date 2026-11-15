package com.example.temperature;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConversionServiceTest {
    private final ConversionService service = new ConversionService();

    @Test
    void convertsCelsiusToFahrenheit() {
        assertEquals(32.0, service.toTarget(0, "C", "F"), 0.01);
    }

    @Test
    void convertsFahrenheitToCelsius() {
        assertEquals(0.0, service.toTarget(32, "F", "C"), 0.01);
    }

    @Test
    void convertsCelsiusToKelvin() {
        assertEquals(273.15, service.toTarget(0, "C", "K"), 0.01);
    }
}

