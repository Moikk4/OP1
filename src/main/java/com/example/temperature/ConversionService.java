package com.example.temperature;

public class ConversionService {
    public double toTarget(double value, String fromUnit, String toUnit) {
        double celsius = switch (fromUnit) {
            case "C" -> value;
            case "F" -> (value - 32) * 5.0 / 9.0;
            case "K" -> value - 273.15;
            default -> throw new IllegalArgumentException("Unknown unit: " + fromUnit);
        };

        return switch (toUnit) {
            case "C" -> celsius;
            case "F" -> (celsius * 9.0 / 5.0) + 32;
            case "K" -> celsius + 273.15;
            default -> throw new IllegalArgumentException("Unknown unit: " + toUnit);
        };
    }
}

