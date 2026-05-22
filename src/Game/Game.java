package Game;

import Database.DatabaseManager;
import Database.SaveManager;
import Entities.Entitymanager;
import UI.ControlPanel;
import UI.InfoPanel;
import Utils.Assets;
import Utils.TimeDay;
import World.World;

public class Game {

    private GameLoop gameLoop;
    private Entitymanager entityManager;
    private World world;
    private TimeDay time;
    private SaveManager saveManager;

    public Game() {

        System.out.println("El game se esta ejecutado");
        
        //sprites 
        Assets.init();
        // WORLD

        time = new TimeDay();
        world = new World();
        entityManager = new Entitymanager(world, time);

        // DATABASE

        DatabaseManager db = new DatabaseManager();
        db.connect();
        db.createTables();
        saveManager = new SaveManager(db);

        /// INFO PANEL

        InfoPanel infoP = new InfoPanel();

        // GAME LOOP

        gameLoop = new GameLoop();

        gameLoop.setEntitymanager(entityManager);

        gameLoop.setTime(time);

        // CONTROL PANEL

        ControlPanel cp = new ControlPanel(
                gameLoop,
                world,
                entityManager,
                infoP,
                time,
                saveManager
        );

        // OBTENER GAME PANEL

        var GP = cp.getGamePanel();

        // CONECTAR

        infoP.setGamePanel(GP);

        // INICIAR LOOP

        gameLoop.start();

        cp.setVisible(true);
    }

    public void loadGame() {

        saveManager.loadGame(world,time,entityManager);
        System.out.println("PARTIDA CARGADA");
    }
    public Entitymanager getEntityManager() {
        return entityManager;
    }
}