package com.example.temperature.service;

import java.util.Map;

public class TemperatureConverter {
    public static double convert(double value, String fromUnit, String toUnit) {
        double celsius = toCelsius(value, fromUnit);
        return fromCelsius(celsius, toUnit);
    }

    private static double toCelsius(double value, String unit) {
        return switch (unit) {
            case "celsius" -> value;
            case "fahrenheit" -> (value - 32.0) * 5.0 / 9.0;
            case "kelvin" -> value - 273.15;
            default -> throw new IllegalArgumentException("Unsupported unit: " + unit);
        };
    }

    private static double fromCelsius(double celsius, String unit) {
        return switch (unit) {
            case "celsius" -> celsius;
            case "fahrenheit" -> (celsius * 9.0 / 5.0) + 32.0;
            case "kelvin" -> celsius + 273.15;
            default -> throw new IllegalArgumentException("Unsupported unit: " + unit);
        };
    }
}

