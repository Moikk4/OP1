package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Data Access Object for localized units using the Translation Table approach.
 * Fetches unit names and descriptions from the database in the selected language,
 * with automatic fallback to English if a translation is not available.
 */
public class LocalizedUnitDAO {

    private final Connection connection;

    public LocalizedUnitDAO(Connection connection) {
        this.connection = connection;
    }

    public static String normalizeLanguageCode(String language) {
        if (language == null || language.isBlank()) {
            return "en";
        }

        String normalized = language.trim();
        int hyphenIndex = normalized.indexOf('-');
        if (hyphenIndex >= 0) {
            normalized = normalized.substring(0, hyphenIndex);
        }
        normalized = normalized.toLowerCase(Locale.ROOT);

        return switch (normalized) {
            case "en", "eng" -> "en";
            case "fi", "fin" -> "fi";
            case "sv", "swe" -> "sv";
            case "ja", "jpn" -> "ja";
            case "ar", "ara" -> "ar";
            default -> "en";
        };
    }

    /**
     * Get all temperature units in the specified language.
     * Falls back to English if the translation is not available.
     *
     * @param language ISO language code (e.g., "en", "fi", "sv", "ja", "ar")
     * @return List of localized temperature units
     */
    public List<LocalizedUnit> getTemperatureUnits(String language) {
        return getUnitsByType("TEMPERATURE", normalizeLanguageCode(language));
    }

    /**
     * Get all speed units in the specified language.
     * Falls back to English if the translation is not available.
     *
     * @param language ISO language code
     * @return List of localized speed units
     */
    public List<LocalizedUnit> getSpeedUnits(String language) {
        return getUnitsByType("SPEED", normalizeLanguageCode(language));
    }

    /**
     * Get all distance units in the specified language.
     * Falls back to English if the translation is not available.
     *
     * @param language ISO language code
     * @return List of localized distance units
     */
    public List<LocalizedUnit> getDistanceUnits(String language) {
        return getUnitsByType("DISTANCE", normalizeLanguageCode(language));
    }

    /**
     * Get all time units in the specified language.
     * Falls back to English if the translation is not available.
     *
     * @param language ISO language code
     * @return List of localized time units
     */
    public List<LocalizedUnit> getTimeUnits(String language) {
        return getUnitsByType("TIME", normalizeLanguageCode(language));
    }

    /**
     * Get units by type with localization and English fallback.
     * Uses a LEFT JOIN to fetch translations, with COALESCE to fall back to English.
     */
    private List<LocalizedUnit> getUnitsByType(String unitType, String language) {
        String sql =
                "SELECT u.id, u.unit_key, u.symbol, u.unit_type, " +
                "       COALESCE(t.name, t_en.name) AS localized_name, " +
                "       COALESCE(t.description, t_en.description) AS description " +
                "FROM units u " +
                "LEFT JOIN unit_translations t ON u.id = t.unit_id AND t.lang = ? " +
                "LEFT JOIN unit_translations t_en ON u.id = t_en.unit_id AND t_en.lang = 'en' " +
                "WHERE u.unit_type = ? " +
                "ORDER BY u.unit_key";

        List<LocalizedUnit> units = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, language);
            ps.setString(2, unitType);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalizedUnit unit = new LocalizedUnit(
                            rs.getInt("id"),
                            rs.getString("unit_key"),
                            rs.getString("symbol"),
                            rs.getString("unit_type"),
                            rs.getString("localized_name"),
                            rs.getString("description")
                    );
                    units.add(unit);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch " + unitType + " units for language " + language, e);
        }

        return units;
    }

    /**
     * Get a specific unit by its key in the specified language.
     * Falls back to English if the translation is not available.
     *
     * @param unitKey The unit key (e.g., "CELSIUS")
     * @param language ISO language code
     * @return LocalizedUnit or null if not found
     */
    public LocalizedUnit getUnitByKey(String unitKey, String language) {
        String normalizedLanguage = normalizeLanguageCode(language);
        String sql =
                "SELECT u.id, u.unit_key, u.symbol, u.unit_type, " +
                "       COALESCE(t.name, t_en.name) AS localized_name, " +
                "       COALESCE(t.description, t_en.description) AS description " +
                "FROM units u " +
                "LEFT JOIN unit_translations t ON u.id = t.unit_id AND t.lang = ? " +
                "LEFT JOIN unit_translations t_en ON u.id = t_en.unit_id AND t_en.lang = 'en' " +
                "WHERE u.unit_key = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, normalizedLanguage);
            ps.setString(2, unitKey);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new LocalizedUnit(
                            rs.getInt("id"),
                            rs.getString("unit_key"),
                            rs.getString("symbol"),
                            rs.getString("unit_type"),
                            rs.getString("localized_name"),
                            rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch unit " + unitKey + " for language " + normalizedLanguage, e);
        }

        return null;
    }

    /**
     * Check if a translation exists for a specific unit and language.
     * Useful for testing fallback behavior.
     */
    public boolean hasTranslation(String unitKey, String language) {
        String normalizedLanguage = normalizeLanguageCode(language);
        String sql =
                "SELECT COUNT(*) FROM units u " +
                "INNER JOIN unit_translations t ON u.id = t.unit_id " +
                "WHERE u.unit_key = ? AND t.lang = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, unitKey);
            ps.setString(2, normalizedLanguage);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check translation for " + unitKey, e);
        }

        return false;
    }

    /**
     * Get the number of units in the database.
     * Useful for testing.
     */
    public int countUnits() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM units");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count units", e);
        }
        return 0;
    }

    /**
     * Get the number of translations in the database.
     * Useful for testing.
     */
    public int countTranslations() {
        try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(*) FROM unit_translations");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count translations", e);
        }
        return 0;
    }
}
