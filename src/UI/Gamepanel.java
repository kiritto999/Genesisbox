/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UI;

import Entities.Entity;
import Entities.Lummon;
import Inputs.Camera;
import World.Tile;
import World.World;
import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import javax.swing.JPanel;
import Inputs.Mouser;
import Game.GameLoop;
import java.util.ArrayList;

/**
 *
 * @author blope
 */
public class GamePanel extends JPanel{
    private final int UNIT_SIZE = 25;
    World world;
    Camera camera;
    GameLoop gameLoop;
    Color ground=new Color(132, 232, 222);
    Color Beach_blue=new Color(73, 201, 252);
    Color ocean = new Color (89, 131, 171);
    ArrayList<Entity> entities = new ArrayList<>();

    public GamePanel(){
        this.setBackground(ocean);
        world = new World();
        camera = new Camera();
        gameLoop = new GameLoop();
        Mouser mouse = new Mouser(camera, this);        
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        
    }
    
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        DrawIsland(g);
        //DrawTest(g);
        //drawGrid(g);
    }
    
   
    
    private void drawGrid(Graphics g) {
        int columns = world.getColums();
        int rows = world.getRows();
        int mapWidth = columns * UNIT_SIZE;
        int mapHeight = rows * UNIT_SIZE;
        int offsetX = (getWidth() - mapWidth) / 2;
        int offsetY = (getHeight() - mapHeight) / 2;

        g.setColor(Color.BLACK);

        for (int i = 0; i <= columns; i++) {
            int x = offsetX + i * UNIT_SIZE;
            g.drawLine(x, offsetY, x, offsetY + mapHeight);
        }
        for (int i = 0; i <= rows; i++) {
            int y = offsetY + i * UNIT_SIZE;
            g.drawLine(offsetX, y, offsetX + mapWidth, y);
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
       entities.add(new Lummon(5, 5));
        for (Entity e : entities) {
            e.draw(g, (int)(UNIT_SIZE * camera.zoom), camera.Camerax, camera.Cameray);
        }
   }
    
    
    
}
    
    
   
