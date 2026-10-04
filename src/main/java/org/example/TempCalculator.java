package org.example;

/**
 * Pure temperature conversion logic. No UI or DB dependencies, fully unit-testable.
 */
public class TempCalculator {

    public double celsiusToFahrenheit(double celsius) {
        return celsius * 9.0 / 5.0 + 32.0;
    }

    public double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32.0) * 5.0 / 9.0;
    }

    public double celsiusToKelvin(double celsius) {
        return celsius + 273.15;
    }

    public double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    public double fahrenheitToKelvin(double fahrenheit) {
        return celsiusToKelvin(fahrenheitToCelsius(fahrenheit));
    }

    public double kelvinToFahrenheit(double kelvin) {
        return celsiusToFahrenheit(kelvinToCelsius(kelvin));
    }

    /**
     * Convert a value from one unit to another.
     */
    public double convert(double value, TemperatureUnit from, TemperatureUnit to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Units must not be null");
        }
        double celsius = toCelsius(value, from);
        return fromCelsius(celsius, to);
    }

    public double toCelsius(double value, TemperatureUnit from) {
        switch (from) {
            case CELSIUS:    return value;
            case FAHRENHEIT: return fahrenheitToCelsius(value);
            case KELVIN:     return kelvinToCelsius(value);
            default: throw new IllegalArgumentException("Unknown unit: " + from);
        }
    }

    public double fromCelsius(double celsius, TemperatureUnit to) {
        switch (to) {
            case CELSIUS:    return celsius;
            case FAHRENHEIT: return celsiusToFahrenheit(celsius);
            case KELVIN:     return celsiusToKelvin(celsius);
            default: throw new IllegalArgumentException("Unknown unit: " + to);
        }
    }

    public boolean isExtremeTemperature(double celsius) {
        return celsius < -40.0 || celsius > 50.0;
    }
}

