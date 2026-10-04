package org.example;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

/**
 * A single recorded temperature conversion.
 */
public class TempRecord {

    private int id;
    private double inputValue;
    private TemperatureUnit fromUnit;
    private TemperatureUnit toUnit;
    private double resultValue;
    private LocalDateTime createdAt;

    public TempRecord() {
    }

    public TempRecord(double inputValue, TemperatureUnit fromUnit,
                      TemperatureUnit toUnit, double resultValue) {
        this(0, inputValue, fromUnit, toUnit, resultValue, LocalDateTime.now());
    }

    public TempRecord(int id, double inputValue, TemperatureUnit fromUnit,
                      TemperatureUnit toUnit, double resultValue, LocalDateTime createdAt) {
        this.id = id;
        this.inputValue = inputValue;
        this.fromUnit = fromUnit;
        this.toUnit = toUnit;
        this.resultValue = resultValue;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getInputValue() {
        return inputValue;
    }

    public void setInputValue(double inputValue) {
        this.inputValue = inputValue;
    }

    public TemperatureUnit getFromUnit() {
        return fromUnit;
    }

    public void setFromUnit(TemperatureUnit fromUnit) {
        this.fromUnit = fromUnit;
    }

    public TemperatureUnit getToUnit() {
        return toUnit;
    }

    public void setToUnit(TemperatureUnit toUnit) {
        this.toUnit = toUnit;
    }

    public double getResultValue() {
        return resultValue;
    }

    public void setResultValue(double resultValue) {
        this.resultValue = resultValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TempRecord)) return false;
        TempRecord that = (TempRecord) o;
        return id == that.id
                && Double.compare(that.inputValue, inputValue) == 0
                && Double.compare(that.resultValue, resultValue) == 0
                && fromUnit == that.fromUnit
                && toUnit == that.toUnit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inputValue, fromUnit, toUnit, resultValue);
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%.2f %s = %.2f %s",
                inputValue, fromUnit.getSymbol(), resultValue, toUnit.getSymbol());
    }
}
