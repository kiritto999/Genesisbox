package Game;

import Database.DatabaseManager;
import Database.SaveManager;
import Entities.Entitymanager;
import UI.ControlPanel;
import UI.InfoPanel;
import Utils.TimeDay;
import World.World;

public class Game {

    private GameLoop gameLoop;

    private Entitymanager entityManager;

    public Game() {

        System.out.println("El game se esta ejecutado");

        // WORLD
        
        TimeDay time = new TimeDay();
        World world = new World();
        entityManager = new Entitymanager(world);

        // DATABASE

        DatabaseManager db = new DatabaseManager();
        db.connect();
        db.createTables();
        SaveManager SV = new SaveManager(db);

        // INFO PANEL


        InfoPanel infoP = new InfoPanel();

        // GAME LOOP

        gameLoop = new GameLoop();
        gameLoop.setEntitymanager(entityManager);
        gameLoop.setTime(time);
        gameLoop.start();

        // CONTROL PANEL

        ControlPanel cp = new ControlPanel(
                gameLoop,
                world,
                entityManager,
                infoP,
                time,
                SV
        );
        cp.setVisible(true);
    }

    public Entitymanager getEntityManager() {
        return entityManager;
    }
}
