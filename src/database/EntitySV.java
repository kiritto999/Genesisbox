package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntitySV {

    // INSERT ENTITY y devuelve ID
    public int insertEntity(String type, String subType, int x, int y, boolean alive) {
        int id = -1;

        String sql = "CALL sp_insert_entity(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, type);
            stmt.setString(2, subType);
            stmt.setInt(3, x);
            stmt.setInt(4, y);
            stmt.setBoolean(5, alive);
            stmt.registerOutParameter(6, Types.INTEGER);

            stmt.execute();
            id = stmt.getInt(6);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    public void insertAnimal(int entityId, double health, int capacity) {
        String sql = "CALL sp_insert_animal(?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, entityId);
            stmt.setDouble(2, health);
            stmt.setInt(3, capacity);

            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertResource(int entityId, String type) {
        String sql = "CALL sp_insert_resource(?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, entityId);
            stmt.setString(2, type);

            stmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // LIMPIAR BD
    public void clearAll() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM entities");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CARGAR TODO
    public ResultSet loadAll(Connection conn) throws SQLException {
        String sql = "SELECT * FROM vw_all_entities";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }
}