package com.example.temperature;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UnitDao {
    private static final String URL = "jdbc:h2:mem:temperature;DB_CLOSE_DELAY=-1;MODE=MYSQL;INIT=RUNSCRIPT FROM 'classpath:db/init.sql'";

    public List<UnitOption> findByCategory(String category, String lang) throws SQLException {
        String sql = """
            SELECT u.unit_id, u.code, COALESCE(t.name, e.name) AS name, u.category
            FROM unit u
            LEFT JOIN unit_translation t
              ON t.unit_id = u.unit_id AND t.lang = ?
            LEFT JOIN unit_translation e
              ON e.unit_id = u.unit_id AND e.lang = 'en'
            WHERE u.category = ?
            ORDER BY u.sort_order
            """;

        List<UnitOption> items = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, lang);
            ps.setString(2, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new UnitOption(rs.getInt("unit_id"), rs.getString("code"), rs.getString("name")));
                }
            }
        }
        return items;
    }

    public void saveConversion(double value, String fromUnit, String toUnit, double result, String lang) throws SQLException {
        String sql = "INSERT INTO conversion_history(record_value, from_unit, to_unit, result_value, language, created_at) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, value);
            ps.setString(2, fromUnit);
            ps.setString(3, toUnit);
            ps.setDouble(4, result);
            ps.setString(5, lang);
            ps.executeUpdate();
        }
    }

    public List<String> findLastFive(String lang) throws SQLException {
        String sql = "SELECT CONCAT(record_value, ' ', from_unit, ' -> ', result_value, ' ', to_unit, ' (', language, ')') FROM conversion_history ORDER BY id DESC LIMIT 5";
        List<String> rows = new ArrayList<>();
        try (Connection c = DriverManager.getConnection(URL);
             PreparedStatement ps = c.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(rs.getString(1));
                }
            }
        }
        return rows;
    }
}
