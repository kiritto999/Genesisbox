package Game;

import Entities.Animal;
import Entities.Entitymanager;
import UI.GamePanel;
import Utils.TimeDay;
import World.World;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author blope
 */
public class GameLoop implements Runnable{
    
    private TimeDay timeDay;
    private long lastTime;    
    private boolean running = false; 
    
    // Referencias necesarias para actualizar animales
    private Entitymanager entitymanager;
    private World world;
    
    public GameLoop(){
        timeDay = new TimeDay();
        lastTime= System.nanoTime();
    }
    
    // Setter para inyectar dependencias después de construir

    public void setEntitymanager(Entitymanager em) {
        this.entitymanager = em; 
  
    }
    public void setWorld(World world) {
        this.world = world;
    }
    public void update(double Ftime) {
        if (timeDay.isPaused()) return;
        timeDay.updateTime(Ftime);
        if (entitymanager != null) entitymanager.update(Ftime); // ← agregar esto
    }

    
    private GamePanel gamePanel;

    public void setGamePanel(GamePanel gp) {
        this.gamePanel = gp;
    }

    @Override
    public void run() {
        while (running) {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1_000_000_000.0;
            lastTime = now;

            if (!timeDay.isPaused()) {
                timeDay.updateTime(deltaTime);
                if (entitymanager != null) entitymanager.update(deltaTime);
            }

            if (gamePanel != null) {
                gamePanel.repaint(); // ← ESTO faltaba
            }

            try { Thread.sleep(16); } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

        
        
    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop()  { running = false; }
    
    public TimeDay getTimeDay() { return timeDay; }

    
    
    
}
