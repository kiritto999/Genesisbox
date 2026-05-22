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
                "x, y, type, variant" +
                ") VALUES (?, ?, ?, ?)"
            );
            for (int y = 0; y < world.getRows(); y++) {
                for (int x = 0; x < world.getColums(); x++) {
                    Tile tile = world.getMap()[y][x];
                    ps.setInt(1, x);
                    ps.setInt(2, y);
                    ps.setInt(3, tile.getType());
                    ps.setInt(4, tile.getVariant());
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
                "x,y," +
                "slot," +
                "health,maxHealth," +
                "alive," +
                "energy,hunger," +
                "speed,attackStat,intelligence," +
                "capacity," +
                "sex," +
                "foodType," +
                "habitat," +
                "amount,maxAmount," +
                "depleted," +
                "regenRate,regenTimer,regenInterval," +
                "customName," +
                "custom1,custom2,custom3" +
                ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
            );
            for (Entity e : manager.getEntities()) {

                ps.setString(1, e.getClass().getSimpleName());
                ps.setInt(2, e.getTileX());
                ps.setInt(3, e.getTileY());
                ps.setInt(4, e.getSlot());
                ps.setInt(5, e.getHealth());
                ps.setInt(6, e.getMaxHealth());
                ps.setBoolean(7, e.isAlive());
                ps.setNull(8, java.sql.Types.INTEGER);
                ps.setNull(9, java.sql.Types.INTEGER);
                ps.setNull(10, java.sql.Types.INTEGER);
                ps.setNull(11, java.sql.Types.INTEGER);
                ps.setNull(12, java.sql.Types.INTEGER);
                ps.setNull(13, java.sql.Types.INTEGER);
                ps.setNull(14, java.sql.Types.VARCHAR);
                ps.setNull(15, java.sql.Types.VARCHAR);
                ps.setNull(16, java.sql.Types.INTEGER);
                ps.setNull(17, java.sql.Types.INTEGER);
                ps.setNull(18, java.sql.Types.INTEGER);
                ps.setNull(19, java.sql.Types.BOOLEAN);
                ps.setNull(20, java.sql.Types.INTEGER);
                ps.setNull(21, java.sql.Types.INTEGER);
                ps.setNull(22, java.sql.Types.INTEGER);
                ps.setString(23, e.getCustomName());
                ps.setNull(24, java.sql.Types.DOUBLE);
                ps.setNull(25, java.sql.Types.DOUBLE);
                ps.setNull(26, java.sql.Types.DOUBLE);

                if (e instanceof Animal) {
                    Animal a = (Animal) e;
                    ps.setInt(8, a.getEnergy());
                    ps.setInt(9, a.getHunger());
                    ps.setInt(10, a.getSpeed());
                    ps.setInt(11, a.getAttack());
                    ps.setInt(12, a.getIntelligence());
                    ps.setInt(13, a.getCapacity());
                    ps.setString(14, "" + a.getSex());
                    ps.setString(15, "" + a.getFoodType());
                    ps.setInt(16, a.getHabitat());
                }

                if (e instanceof Resource) {
                    Resource r = (Resource) e;
                    ps.setInt(17, r.getQuantity());
                    ps.setInt(18, r.getMaxQuantity());
                    ps.setBoolean(19, r.isDepleted());
                    ps.setInt(20, r.getRegenRate());
                    ps.setInt(21, r.getRegenTimer());
                    ps.setInt(22, r.getRegenInterval());
                }

                if (e instanceof Zenthra) {
                    Zenthra t = (Zenthra) e;
                    ps.setDouble(24, t.getStage().ordinal());
                }

                if (e instanceof Corpse) {
                    Corpse c = (Corpse) e;
                    ps.setDouble(25, c.getAge());
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
                int type = rs.getInt("type");
                int variant = rs.getInt("variant");
                //Tile tile = new Tile(type);
                //tile.setVariant(variant);
                //world.getMap()[y][x] = tile;
                
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

                    case "Lummon":
                        e = new Lummon(x, y,manager);
                        break;

                    case "Zyrox":
                        e = new Zyrox(x, y,manager);
                        break;

                    case "Zenthra":
                        e = new Zenthra(x, y);
                        break;
                    case "Nero":
                        e = new Nero(x,y);
                        break;
                    case "Blupys":
                        e = new Blupys(x,y);
                        break;
                }

                if(e != null) {
                    e.setHealth(rs.getInt("health"));
                    e.setMaxHealth(rs.getInt("maxHealth"));
                    e.setAlive(rs.getBoolean("alive"));
                    e.setSlot(rs.getInt("slot"));
                    e.setCustomName(rs.getString("customName"));

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