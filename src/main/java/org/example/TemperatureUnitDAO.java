package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for the temperature_units lookup table.
 */
public class TemperatureUnitDAO {

    private final Connection connection;

    public TemperatureUnitDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(TemperatureUnit unit) {
        String sql = "MERGE INTO temperature_units (name, symbol) KEY(name) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, unit.name());
            ps.setString(2, unit.getSymbol());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save unit " + unit, e);
        }
    }

    /**
     * Make sure all enum values exist in the lookup table.
     */
    public void seedDefaults() {
        for (TemperatureUnit u : TemperatureUnit.values()) {
            save(u);
        }
    }

    public List<String> findAllNames() {
        List<String> names = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT name FROM temperature_units ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                names.add(rs.getString(1));
            }
            return names;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list units", e);
        }
    }

    public String findSymbol(String name) {
        String sql = "SELECT symbol FROM temperature_units WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString(1) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find symbol for " + name, e);
        }
    }

    public int count() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT COUNT(*) FROM temperature_units");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count units", e);
        }
    }
}

