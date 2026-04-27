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
import World.World;


public class Game {
    private GameLoop gameLoop;

    public Game() {
        System.out.println("El game se esta ejecutado");

        gameLoop = new GameLoop();
        gameLoop.start();

        World world = new World();
        Entitymanager manager = new Entitymanager(world);

        ControlPanel cp = new ControlPanel(gameLoop, world, manager);

        cp.setVisible(true);
    }
}
