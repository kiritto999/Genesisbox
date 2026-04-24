package Game;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author blope
 */
import javax.swing.JFrame;
import UI.ControlPanel;

public class Game {
    private GameLoop gameLoop;

    public Game() {
        System.out.println("El game se esta ejecutado");
        gameLoop = new GameLoop();
        gameLoop.start();
        
        ControlPanel panel = new ControlPanel(gameLoop);
        panel.setVisible(true);
        
    }
}
