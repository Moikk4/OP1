package org.example;

/**
 * Supported temperature units.
 */
public enum TemperatureUnit {
    CELSIUS("Celsius", "°C"),
    FAHRENHEIT("Fahrenheit", "°F"),
    KELVIN("Kelvin", "K");

    private final String displayName;
    private final String symbol;

    TemperatureUnit(String displayName, String symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    /**
     * Case-insensitive lookup by enum name or display name.
     */
    public static TemperatureUnit fromString(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Unit text must not be null");
        }
        for (TemperatureUnit u : values()) {
            if (u.name().equalsIgnoreCase(text.trim())
                    || u.displayName.equalsIgnoreCase(text.trim())) {
                return u;
            }
        }
        throw new IllegalArgumentException("Unknown temperature unit: " + text);
    }

    @Override
    public String toString() {
        return displayName + " (" + symbol + ")";
    }
}

