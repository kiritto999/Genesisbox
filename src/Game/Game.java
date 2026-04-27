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

    public Game() {
        System.out.println("El game se esta ejecutado");
        
        World world = new World();
        Entitymanager manager = new Entitymanager(world);
        InfoPanel infoP = new InfoPanel();

        gameLoop = new GameLoop();
        gameLoop.setEntitymanager(manager);  // primero inyectar
        gameLoop.start();                    // luego arrancar
        ControlPanel cp = new ControlPanel(gameLoop, world, manager,infoP);

        cp.setVisible(true);
    }
}
