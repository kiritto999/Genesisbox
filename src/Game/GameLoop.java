package Game;

import Utils.TimeDay;

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
    
    public GameLoop(){
        timeDay = new TimeDay();
        lastTime= System.nanoTime();
    }
    
    public void update(double Ftime) { 
        if (timeDay.isPaused()) {
            lastTime = System.nanoTime(); 
            return;
        }
        timeDay.updateTime(Ftime);
    }
    
    @Override
    public void run() {
        System.out.println("Time INICIADO");

        while (running == true ){

            long now = System.nanoTime();

            if (timeDay.isPaused()) {
                lastTime = now;
            } else {
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                update(deltaTime);
            }
            double deltaTime = (now - lastTime) / 1_000_000_000.0;
            lastTime = now;

            update(deltaTime);
            
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        
        
        
        
    }
    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }   
    
    public TimeDay getTimeDay() {
        return timeDay;
    }
    
    
}
