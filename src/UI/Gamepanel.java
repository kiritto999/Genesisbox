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
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author blope
 */
public class GamePanel extends JPanel {

    public final int UNIT_SIZE = 32; //unidad de medida de las casillas 32pixeles
    
    InfoPanel infoP;
    World world;
    Camera camera;
    Entitymanager entitymanager;

    Color ground = new Color(16, 79, 23);
    Color Beach_blue = new Color(73, 201, 252);
    Color ocean = new Color(89, 131, 171);
    boolean dgrid= false;
    private Entity followedEntity;
    
    public GamePanel(World world, Entitymanager entitymanager, InfoPanel infoPanel, Camera camera){
        this.setBackground(ocean);
        this.infoP = infoPanel;
        this.world = world;
        this.entitymanager = entitymanager;
        this.camera = camera;
    }

    
    protected void paintComponent(Graphics g) {
        if (followedEntity != null &&
            followedEntity.isAlive()) {
            int size = (int)(UNIT_SIZE * camera.zoom);
            camera.Camerax =
                getWidth()/2 -
                (followedEntity.getTileX() * size);
            camera.Cameray =
                getHeight()/2 -
                (followedEntity.getTileY() * size);
            limitForCamera(camera);
        }
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
   
    public void DrawTest(Graphics g) {
        synchronized (entitymanager) {
            for (Entity e : entitymanager.getResources()) {
                e.draw(g, (int)(UNIT_SIZE * camera.zoom), camera.Camerax, camera.Cameray);
            }
            for (Entity e : entitymanager.getAnimals()) {
                e.draw(g, (int)(UNIT_SIZE * camera.zoom), camera.Camerax, camera.Cameray);
            }
        }
    }
   
   //detector de entidades en las casiilas
   public void handleClick(int mouseX, int mouseY) {
        int size = (int)(UNIT_SIZE * camera.zoom);

        int tileX = (mouseX - camera.Camerax) / size;
        int tileY = (mouseY - camera.Cameray) / size;
        
        // límites
        if (tileX < 0 || tileY < 0 ||
            tileX >= world.getColums() ||
            tileY >= world.getRows()) {

            return;
        }

        Tile tile = world.getMap()[tileY][tileX];
        ArrayList<Entity> entitiesInTile = new ArrayList<>();
        // buscar entidades
        for (Entity e : entitymanager.getEntities()) { 
            if (e.getTileX() == tileX && e.getTileY() == tileY) {
                entitiesInTile.add(e);
            }
        }
        // enviar info al InfoPanel
        if (infoP != null) {
            infoP.setSelected(tileX,tileY, tile,null,null );
            infoP.setTileEntities(entitiesInTile);
        }
    }
   //seguimiento de la entidad
   public void focusEntity(Entity e) {
        int size = (int)(UNIT_SIZE * camera.zoom);
        int targetX =getWidth()/2 - (e.getTileX() * size);
        int targetY =getHeight()/2 - (e.getTileY() * size);

        camera.Camerax = targetX;
        camera.Cameray = targetY;

        limitForCamera(camera);
        repaint();
    }
   
    public void followEntity(Entity e) {
        followedEntity = e;
    }
    public void stopFollowing() {
        followedEntity = null;
    }
    public int getUNIT_SIZE() {
        return UNIT_SIZE;
    }

    public Camera getCamera() {
        return camera;
    }
   public Entitymanager getEntityManager() {
        return entitymanager;
   }
    
}
    
    
    

    
    
   
