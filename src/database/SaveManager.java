/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Entities.*;
import Utils.TimeDay;
import World.Tile;
import World.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class SaveManager {
    private DatabaseManager db;
    private Connection connection;
    public SaveManager(DatabaseManager db) {
        this.db = db;
        this.connection = db.getConnection();
    }
    
    public void saveWorld(World world, TimeDay time) {

        try {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO world (" +
                "worldRows, worldCols," +
                "day, year," +
                "hour, minute, second" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            ps.setInt(1, world.getRows());
            ps.setInt(2, world.getColums());
            ps.setInt(3, time.getDay());
            ps.setInt(4, time.getYear());
            ps.setInt(5, time.getHour());
            ps.setInt(6, time.getMinute());
            ps.setInt(7, time.getSecond());
            ps.executeUpdate();
            System.out.println("World guardado");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void saveTiles(World world) {

        try {
            PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO tiles (" +
                "x, y, tileId" +
                ") VALUES (?, ?, ?)"
            );

            for (int y = 0; y < world.getRows(); y++) {

                for (int x = 0; x < world.getColums(); x++) {

                    Tile tile = world.getMap()[y][x];
                    ps.setInt(1, x);
                    ps.setInt(2, y);

                    ps.setInt(3, tile.getType());

                    ps.executeUpdate();
                }
            }

            System.out.println("Tiles guardados");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    public void saveEntities(Entitymanager manager) {

    try {

        PreparedStatement ps = connection.prepareStatement(

            "INSERT INTO entities (" +

            "entityType," +
            "x," +
            "y," +
            "slot," +

            "health," +
            "maxHealth," +
            "alive," +

            "customName," +

            "energy," +
            "hunger," +

            "speed," +
            "attackStat," +
            "intelligence," +

            "capacity," +

            "sex," +
            "foodType," +

            "habitat," +

            "ageDays," +
            "stage," +
            "reproTimer," +

            "amount," +
            "maxAmount," +

            "depleted," +

            "regenRate," +
            "regenTimer," +
            "regenInterval" +

            ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
        );

        for(Entity e : manager.getEntities()) {

            ps.setString(1,
                e.getClass().getSimpleName()
            );

            ps.setInt(2,
                e.getTileX()
            );

            ps.setInt(3,
                e.getTileY()
            );

            ps.setInt(4,
                e.getSlot()
            );

            ps.setInt(5,
                e.getHealth()
            );

            ps.setInt(6,
                e.getMaxHealth()
            );

            ps.setBoolean(7,
                e.isAlive()
            );

            ps.setString(8,
                e.getCustomName()
            );

            // DEFAULTS
            ps.setInt(9, 0);
            ps.setInt(10, 0);

            ps.setInt(11, 0);
            ps.setInt(12, 0);
            ps.setInt(13, 0);

            ps.setInt(14, 0);

            ps.setString(15, "");
            ps.setString(16, "");

            ps.setInt(17, 0);

            ps.setInt(18, 0);
            ps.setString(19, "");
            ps.setDouble(20, 0);

            ps.setInt(21, 0);
            ps.setInt(22, 0);

            ps.setBoolean(23, false);

            ps.setInt(24, 0);
            ps.setInt(25, 0);
            ps.setInt(26, 0);

            // =========================
            // ANIMALS
            // =========================

            if(e instanceof Animal) {

                Animal a = (Animal)e;

                ps.setInt(9,
                    a.getEnergy()
                );

                ps.setInt(10,
                    a.getHunger()
                );

                ps.setInt(11,
                    a.getSpeed()
                );

                ps.setInt(12,
                    a.getAttack()
                );

                ps.setInt(13,
                    a.getIntelligence()
                );

                ps.setInt(14,
                    a.getCapacity()
                );

                ps.setString(15,
                    a.getSex().name()
                );

                ps.setString(16,
                    a.getFoodType().name()
                );

                ps.setInt(17,
                    a.getHabitat()
                );

                ps.setInt(18,
                    a.getEdadDias()
                );

                ps.setString(19,
                    a.getEtapa().name()
                );

                ps.setDouble(20,
                    a.getReproTimer()
                );
            }

            // =========================
            // RESOURCES
            // =========================

            if(e instanceof Resource) {

                Resource r = (Resource)e;

                ps.setInt(21,
                    r.getQuantity()
                );

                ps.setInt(22,
                    r.getMaxQuantity()
                );

                ps.setBoolean(23,
                    r.isDepleted()
                );

                ps.setInt(24,
                    r.getRegenRate()
                );

                ps.setInt(25,
                    r.getRegenTimer()
                );

                ps.setInt(26,
                    r.getRegenInterval()
                );
            }

            ps.executeUpdate();
        }

        System.out.println("Entidades guardadas");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    
    
    public void saveGame(World world,TimeDay time, Entitymanager manager) {
        db.clearTables();
        saveWorld(world, time);
        saveTiles(world);
        saveEntities(manager);
        System.out.println("PARTIDA GUARDADA");
    }
    
    public void loadWorld(World world, TimeDay time) {

        try {

            PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM world LIMIT 1"
            );
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                time.setDay(
                    rs.getInt("day")
                );
                time.setYear(
                    rs.getInt("year")
                );
                time.setHour(
                    rs.getInt("hour")
                );
                time.setMinute(
                    rs.getInt("minute")
                );
                time.setSecond(
                    rs.getInt("second")
                );
                System.out.println("World cargado");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    public void loadTiles(World world) {

        try {

            PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM tiles"
            );

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                int x = rs.getInt("x");
                int y = rs.getInt("y");

                int tileId = rs.getInt("tileId");

                world.setTile(y, x, tileId);
            }

            System.out.println("Tiles cargados");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    
    public void loadEntities(Entitymanager manager) {

        try {

            PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM entities"
            );

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                String type = rs.getString("entityType");

                int x = rs.getInt("x");
                int y = rs.getInt("y");

                Entity e = null;

                switch(type) {

                    // =========================
                    // ANIMALS
                    // =========================

                    case "Lummon":
                        e = new Lummon(x, y, manager);
                        break;

                    case "Zyrox":
                        e = new Zyrox(x, y, manager);
                        break;

                    // =========================
                    // RESOURCES
                    // =========================

                    case "Zenthra":
                        e = new Zenthra(x, y);
                        break;

                    case "Nero":
                        e = new Nero(x, y);
                        break;

                    case "Blupys":
                        e = new Blupys(x, y);
                        break;

                    case "Corpse":
                        e = new Corpse(
                            x,
                            y,
                            rs.getInt("maxAmount")
                        );
                        break;
                }

                // =========================
                // COMMON DATA
                // =========================

                if(e != null) {

                    e.setHealth(
                        rs.getInt("health")
                    );

                    e.setMaxHealth(
                        rs.getInt("maxHealth")
                    );

                    e.setAlive(
                        rs.getBoolean("alive")
                    );

                    e.setSlot(
                        rs.getInt("slot")
                    );

                    e.setCustomName(
                        rs.getString("customName")
                    );

                    // =========================
                    // ANIMAL DATA
                    // =========================

                    if (e instanceof Animal) {

                        Animal a = (Animal) e;

                        a.setEnergy(
                            rs.getInt("energy")
                        );

                        a.setHunger(
                            rs.getInt("hunger")
                        );

                        a.setSpeed(
                            rs.getInt("speed")
                        );

                        a.setAttack(
                            rs.getInt("attackStat")
                        );

                        a.setIntelligence(
                            rs.getInt("intelligence")
                        );

                        a.setCapacity(
                            rs.getInt("capacity")
                        );

                        a.setHabitat(
                            rs.getInt("habitat")
                        );

                        a.setEdadDias(
                            rs.getInt("ageDays")
                        );

                        a.setReproTimer(
                            rs.getDouble("reproTimer")
                        );

                        String stage = rs.getString("stage");

                        if(stage != null) {

                            a.setEtapa(
                                Animal.Etapa.valueOf(stage)
                            );
                        }
                    }

                    // =========================
                    // RESOURCE DATA
                    // =========================

                    if (e instanceof Resource) {

                        Resource r = (Resource) e;

                        r.setQuantity(
                            rs.getInt("amount")
                        );

                        r.setMaxQuantity(
                            rs.getInt("maxAmount")
                        );

                        r.setDepleted(
                            rs.getBoolean("depleted")
                        );

                        r.setRegenRate(
                            rs.getInt("regenRate")
                        );

                        r.setRegenTimer(
                            rs.getInt("regenTimer")
                        );

                        r.setRegenInterval(
                            rs.getInt("regenInterval")
                        );
                    }

                    manager.addEntity(e);
                }
            }

            System.out.println("Entidades cargadas");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    public void loadGame(World world,TimeDay time,Entitymanager manager) {

        manager.getEntities().clear();
        loadWorld(world, time);
        loadTiles(world);
        loadEntities(manager);
        System.out.println("PARTIDA CARGADA");
    }
}