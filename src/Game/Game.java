package Game;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author blope
 */
import Entities.Entitymanager;
import javax.swing.JFrame;
import UI.ControlPanel;
import UI.GamePanel;
import UI.InfoPanel;
import World.World;

public class Game {

    private GameLoop gameLoop;
    private Entitymanager entityManager;

    public Game() {
        System.out.println("El game se esta ejecutado");

        World world = new World();
        entityManager = new Entitymanager(world);

        InfoPanel infoP = new InfoPanel();

        gameLoop = new GameLoop();
        gameLoop.setEntitymanager(entityManager);
        gameLoop.start();

        ControlPanel cp = new ControlPanel(gameLoop, world, entityManager, infoP);
        cp.setVisible(true);
    }

    public Entitymanager getEntityManager() {
        return entityManager;
    }
}
