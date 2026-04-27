/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import Entities.*;
import Inputs.*;
import World.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import javax.swing.JPanel;
import Game.GameLoop;
import java.util.ArrayList;

/**
 *
 * @author blope
 */
public class GamePanel extends JPanel {

    private final int UNIT_SIZE = 25;
    private ControlPanel controlPanel;
    World world;
    Camera camera;
    Entitymanager entitymanager;

    Color ground = new Color(16, 79, 23);
    Color Beach_blue = new Color(73, 201, 252);
    Color ocean = new Color(89, 131, 171);
    boolean dgrid= false;
    public GamePanel(World world, Entitymanager entitymanager,ControlPanel cp){
        this.setBackground(ocean);

        this.controlPanel = cp;
        this.world = world;
        this.entitymanager = entitymanager;

        camera = new Camera();

        Mouser mouse = new Mouser(camera, this);        
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        
    }

    
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        DrawIsland(g);
        DrawTest(g);
        drawGrid(g);
    }
    
   
    
    private void drawGrid(Graphics g) {
    
    if (dgrid){
        int size = (int)(UNIT_SIZE * camera.zoom);

        g.setColor(Color.BLACK);

        // líneas verticales
        for (int c = 0; c <= world.getColums(); c++) {
            int x = camera.Camerax + (int)(c * size);
            g.drawLine(
                x,
                camera.Cameray,
                x,
                camera.Cameray + world.getRows() * size
            );
        }

        // líneas horizontales
        for (int r = 0; r <= world.getRows(); r++) {
            int y = camera.Cameray + (int)(r * size);
            g.drawLine(
                camera.Camerax,
                y,
                camera.Camerax + world.getColums() * size,
                y
            );
        }
    }
    }
    
    public void DrawIsland(Graphics g){
        Tile[][] map= world.getMap();
        
        int mapWidth = (int)(world.getColums() * UNIT_SIZE * camera.zoom);
        int mapHeight = (int)(world.getRows() * UNIT_SIZE * camera.zoom);
        
        int size = (int)(UNIT_SIZE * camera.zoom);
        
        
        for (int r=0 ; r < world.getRows() ; r++){
            for (int c=0;c< world.getColums();c++){
                if (map[r][c].getType() == Tile.WATER ){
                    g.setColor(Beach_blue); 
                }else{
                    g.setColor(ground);
                    }
                
                int x = camera.Camerax + (int)(c * size);
                int y = camera.Cameray + (int)(r * size);
                g.fillRect(
                    x,
                    y,  
                    size,
                    size
                );
            }       
        }       
    }
    
   public void limitForCamera(Camera camera) {

    int tileSize = (int)(UNIT_SIZE * camera.zoom);
    int mapWidth = world.getColums() * tileSize;
    int mapHeight = world.getRows() * tileSize;
    int panelWidth = getWidth();
    int panelHeight = getHeight();
    int minX = panelWidth - mapWidth;
    int maxX = 0;
    int minY = panelHeight - mapHeight;
    int maxY = 0;

    if (mapWidth <= panelWidth) {
        camera.Camerax = (panelWidth - mapWidth) / 2;
    } else {
        camera.Camerax = Math.max(minX, Math.min(camera.Camerax, maxX));
    }

    if (mapHeight <= panelHeight) {
        camera.Cameray = (panelHeight - mapHeight) / 2;
    } else {
        camera.Cameray = Math.max(minY, Math.min(camera.Cameray, maxY));
    }
}
   public void DrawTest(Graphics g){

        for (Entity e : entitymanager.getResources()) {
            e.draw(g, (int)(UNIT_SIZE * camera.zoom), camera.Camerax, camera.Cameray);
        }
        for (Entity e : entitymanager.getAnimals()) {
            e.draw(g, (int)(UNIT_SIZE * camera.zoom), camera.Camerax, camera.Cameray);
        }
   }
}
    
    
    

    
    
   
