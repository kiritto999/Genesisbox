package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DatabaseManager {

    private static String URL;
    private static String USER;
    private static String PASSWORD;
    
    private Connection connection;
    
    static {

        try {

            Properties props = new Properties();
            props.load(
                DatabaseManager.class.getResourceAsStream("/db.properties")
            );
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");
            System.out.println("db.properties cargado");
        } catch (IOException e) {
            System.err.println("No se encontró db.properties");
            e.printStackTrace();
        }
    }


    public void connect() {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("MySQL conectado");

        } catch (ClassNotFoundException | SQLException e) {

            e.printStackTrace();
        }
    }

    // =========================================
    // GET CONNECTION
    // =========================================

    public Connection getConnection() {
        return connection;
    }

    // =========================================
    // CREAR TABLAS
    // =========================================

    public void createTables() {

        try {

            Statement stmt = connection.createStatement();

            stmt.execute(

                "CREATE TABLE IF NOT EXISTS world (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "worldRows INT," +
                "worldCols INT," +
                "day INT," +
                "year INT," +
                "hour INT," +
                "minute INT," +
                "second INT" +
                ")"
            );

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS tiles (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "x INT," +
                "y INT," +
                "type INT," +
                "variant INT" +
                ")"
            );

            stmt.execute(
                "CREATE TABLE IF NOT EXISTS entities (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "entityType VARCHAR(50)," +
                "x INT," +
                "y INT," +
                "slot INT," +
                "health INT," +
                "maxHealth INT," +
                "alive BOOLEAN," +
                "energy INT," +
                "hunger INT," +
                "speed INT," +
                "attackStat INT," +
                "intelligence INT," +
                "capacity INT," +
                "sex VARCHAR(20)," +
                "foodType VARCHAR(20)," +
                "habitat INT," +
                "amount INT," +
                "maxAmount INT," +
                "depleted BOOLEAN," +
                "regenRate INT," +
                "regenTimer INT," +
                "regenInterval INT," +
                "customName VARCHAR(100)," +
                "custom1 DOUBLE," +
                "custom2 DOUBLE," +
                "custom3 DOUBLE" +

                ")"
            );
            System.out.println("Tablas creadas");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public void clearTables() {

        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM world");
            stmt.executeUpdate("DELETE FROM tiles");
            stmt.executeUpdate("DELETE FROM entities");
            System.out.println("Datos eliminados");

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Conexion cerrada");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}