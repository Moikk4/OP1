package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access object for {@link TempRecord}.
 * A {@link Connection} is supplied by the caller, which makes the DAO
 * trivially testable against an in-memory database.
 */
public class TempRecordDAO {

    private final Connection connection;

    public TempRecordDAO(Connection connection) {
        this.connection = connection;
    }

    public TempRecord save(TempRecord record) {
        String sql = "INSERT INTO temp_records"
                + " (input_value, from_unit, to_unit, result_value, created_at)"
                + " VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, record.getInputValue());
            ps.setString(2, record.getFromUnit().name());
            ps.setString(3, record.getToUnit().name());
            ps.setDouble(4, record.getResultValue());
            ps.setTimestamp(5, Timestamp.valueOf(record.getCreatedAt()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    record.setId(keys.getInt(1));
                }
            }
            return record;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save temp record", e);
        }
    }

    public TempRecord findById(int id) {
        String sql = "SELECT * FROM temp_records WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find temp record " + id, e);
        }
    }

    public List<TempRecord> findAll() {
        String sql = "SELECT * FROM temp_records ORDER BY id";
        List<TempRecord> records = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                records.add(mapRow(rs));
            }
            return records;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to list temp records", e);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM temp_records WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete temp record " + id, e);
        }
    }

    public int count() {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT COUNT(*) FROM temp_records");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to count temp records", e);
        }
    }

    private TempRecord mapRow(ResultSet rs) throws SQLException {
        return new TempRecord(
                rs.getInt("id"),
                rs.getDouble("input_value"),
                TemperatureUnit.fromString(rs.getString("from_unit")),
                TemperatureUnit.fromString(rs.getString("to_unit")),
                rs.getDouble("result_value"),
                rs.getTimestamp("created_at").toLocalDateTime());
    }
}

