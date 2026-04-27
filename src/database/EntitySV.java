package database;

import java.sql.*;
import Entities.*;

public class EntitySV {
/*
    // =========================
    // INSERT ENTITY (DEVUELVE ID)
    // =========================
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

    // =========================
    // INSERT ANIMAL
    // =========================
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

    // =========================
    // INSERT RESOURCE
    // =========================
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

    // =========================
    // LIMPIAR BD
    // =========================
    public void clearAll() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("DELETE FROM entities");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================
    // CARGAR TODO (RECONSTRUIR)
    // =========================
    public void loadAll(Connection conn, Entitymanager manager) throws SQLException {

        String sql = "SELECT * FROM vw_all_entities";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                String subType = rs.getString("sub_type");
                int x = rs.getInt("x");
                int y = rs.getInt("y");

                Entity e = null;

                // 🌳 RECURSOS
                if (subType.equals("Tree")) {
                    e = new Tree(x, y);

                } else if (subType.equals("Nero")) {
                    e = new Nero(x, y);

                } else if (subType.equals("Food")) {
                    e = new Food(x, y);

                // 🐾 ANIMALES (AJUSTA A LOS TUYOS)
                } else if (subType.equals("Rabbit")) {
                    Rabbit r = new Rabbit(x, y);

                    try {
                        r.setHealth(rs.getDouble("health"));
                        r.setCapacity(rs.getInt("capacity"));
                    } catch (Exception ignored) {}

                    e = r;

                } else if (subType.equals("Fox")) {
                    Zyrox f = new Zyrox(x, y,);

                    try {
                        f.setHealth(rs.getDouble("health"));
                        f.setCapacity(rs.getInt("capacity"));
                    } catch (Exception ignored) {}

                    e = f;
                }

                // AGREGAR SIN DUPLICAR
                if (e != null) {

                    if (e instanceof Animal a) {
                        manager.addAnimal(a);
                    } else {
                        manager.addEntity(e);
                    }
                }
            }
        }
    }*/
}