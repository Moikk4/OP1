package com.example.temperature.dao;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class UnitDao {
    private static final String URL = "jdbc:mysql://localhost:3306/temperature_db?useUnicode=true&characterEncoding=utf8mb4&connectionCollation=utf8mb4_unicode_ci";
    private static final String USER = "app";
    private static final String PASS = "secret";

    public Map<String, String> findUnitsByCategory(String categoryCode, String language) throws SQLException {
        Map<String, String> result = new LinkedHashMap<>();
        String sql = """
            SELECT u.code, COALESCE(
                (SELECT ut.name FROM unit_translation ut
                 WHERE ut.unit_id = u.id AND ut.lang = ?),
                (SELECT ut.name FROM unit_translation ut
                 WHERE ut.unit_id = u.id AND ut.lang = 'en')
            ) AS localized_name
            FROM unit u
            JOIN unit_category c ON c.id = u.category_id
            WHERE c.code = ?
            ORDER BY u.id
            """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, language);
            ps.setString(2, categoryCode);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getString("code"), rs.getString("localized_name"));
                }
            }
        }
        return result;
    }
}

