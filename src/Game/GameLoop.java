package Game;

import Entities.*;
import UI.GamePanel;
import Utils.TimeDay;


public class GameLoop implements Runnable {

    private TimeDay timeDay;
    private long lastTime;
    private boolean running = false;
    private int ultimoDiaRegistrado = -1;

    private Entitymanager entitymanager;
    private GamePanel gamePanel;
    private long lastRender = 0;

    private double speedMultiplier = 1.0;

    public GameLoop() {
        timeDay = new TimeDay();
        lastTime = System.nanoTime();
    }


    public void pause() {
        timeDay.setPaused(true);
    }

    public void resume() {
        lastTime = System.nanoTime(); 
        timeDay.setPaused(false);
    }

    @Override
    public void run() {
        while (running) {

            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1_000_000_000.0;
            lastTime = now;

            if (deltaTime > 0.1) {
                deltaTime = 0.1;
            }

            double scaledDelta = deltaTime * speedMultiplier;

            if (!timeDay.isPaused()) {
                // Tiempo del juego
                timeDay.updateTime(scaledDelta);
                if (timeDay.getDay() != ultimoDiaRegistrado) {
                    ultimoDiaRegistrado = timeDay.getDay();
                    if (entitymanager != null) {
                        synchronized (entitymanager) {
                            for (Animal a : entitymanager.getAnimals()) {
                                a.cumplirDia();
                            }
                        }
                    }
                }
                            
                // Entidades
                if (entitymanager != null) {
                    synchronized (entitymanager) {
                        entitymanager.update(scaledDelta);
                    }
                }
            }

            // Render
            if (gamePanel != null) {

            long current = System.currentTimeMillis();

            if (current - lastRender >= 33) { // 30 FPS
                gamePanel.repaint();
                lastRender = current;
            }
        }

            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        if(running){
            return;
        }
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }

    // Dependencias
    public void setEntitymanager(Entitymanager em) {
        this.entitymanager = em;
    }

    public void setGamePanel(GamePanel gp) {
        this.gamePanel = gp;
    }

    // Velocidad
    public void setSpeed(double speed) {
        if(speed < 0.1){
            speed = 0.1;
        }
        this.speedMultiplier = speed;
    }

    public double getSpeed() {
        return speedMultiplier;
    }

    public TimeDay getTimeDay() {
        return timeDay;
    }
    
    public void setTime(TimeDay time) {
        this.timeDay = time;
    }
}