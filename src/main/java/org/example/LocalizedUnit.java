package org.example;

import java.util.Objects;

/**
 * Represents a unit with its localized name and description from the database.
 * Used to display temperature, speed, distance, and time units in the user's language.
 */
public class LocalizedUnit {

    private final int id;
    private final String unitKey;       // e.g., "CELSIUS", "KM_PER_HOUR"
    private final String symbol;        // e.g., "°C", "km/h"
    private final String unitType;      // e.g., "TEMPERATURE", "SPEED"
    private final String localizedName; // e.g., "セルシウス" in Japanese
    private final String description;   // Optional localized description

    public LocalizedUnit(int id, String unitKey, String symbol, String unitType,
                         String localizedName, String description) {
        this.id = id;
        this.unitKey = unitKey;
        this.symbol = symbol;
        this.unitType = unitType;
        this.localizedName = localizedName;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getUnitKey() {
        return unitKey;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getUnitType() {
        return unitType;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns the display name with symbol, e.g., "Celsius (°C)"
     */
    public String getDisplayName() {
        return localizedName + " (" + symbol + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocalizedUnit)) return false;
        LocalizedUnit that = (LocalizedUnit) o;
        return id == that.id
                && Objects.equals(unitKey, that.unitKey)
                && Objects.equals(localizedName, that.localizedName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, unitKey, localizedName);
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
