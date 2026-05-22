package Inputs;

import java.awt.event.*;
import UI.GamePanel;
import Utils.*;
import World.World;
import World.Tile;
import Entities.Entitymanager;
import Entities.Lummon;
import Entities.Nero;
import Entities.Zenthra;
import Entities.Zyrox;
import UI.ControlPanel;
import Utils.LightningEffect;

public class Mouser implements MouseListener, MouseMotionListener, MouseWheelListener {

    private Camera camera;
    private GamePanel jpanel;
    private World world;
    private Tool currentTool = Tool.NONE;
    private int lastX, lastY;
    private boolean dragging = false;
    private Entitymanager Emanager;
    private ControlPanel controlPanel;
    
    public Mouser(Camera camera, GamePanel panel, World world,Entitymanager entityManager,ControlPanel cp) {
        this.camera = camera;
        this.jpanel = panel;
        this.world = world;
        this.Emanager = entityManager;
        this.controlPanel = cp;

        jpanel.addMouseListener(this);
        jpanel.addMouseMotionListener(this);
        jpanel.addMouseWheelListener(this);
    }
    
    


    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        lastX = x;
        lastY = y;

        
        if (currentTool == Tool.NONE) {
            jpanel.handleClick(x, y);
            dragging = true;
        }else {
            handleBuild(x, y);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();


        if (isPaintTool()) {
            handleBuild(x, y);
            return;
        }


        if (!dragging) return;

        int dx = x - lastX;
        int dy = y - lastY;

        camera.Camerax += dx;
        camera.Cameray += dy;

        lastX = x;
        lastY = y;

        jpanel.limitForCamera(camera);
        jpanel.repaint();
    }


    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {


        if (currentTool != Tool.NONE) return;

        int mouseX = e.getX();
        int mouseY = e.getY();

        double oldZoom = camera.zoom;

        if (e.getWheelRotation() < 0) {
            camera.zoom *= 1.1;
        } else {
            camera.zoom *= 0.9;
        }

        camera.zoom = Math.max(0.5, Math.min(camera.zoom, 3.0));

        double scale = camera.zoom / oldZoom;

        camera.Camerax = (int)(mouseX - (mouseX - camera.Camerax) * scale);
        camera.Cameray = (int)(mouseY - (mouseY - camera.Cameray) * scale);

        jpanel.limitForCamera(camera);
        jpanel.repaint();
    }


    private void handleBuild(int x, int y) {

        int tileSize = jpanel.getUNIT_SIZE();

        // ?convertir a coordenadas del mundo de entero a double
        double worldX = (x - camera.Camerax) / camera.zoom;
        double worldY = (y - camera.Cameray) / camera.zoom;

        // <?convertir a tile usando floor ya que si lo haces normal no lo hace en un lugar exacto
        int col = (int)Math.floor(worldX / tileSize);//en qué fila del mapa estoy haciendo click
        int row = (int)Math.floor(worldY / tileSize);//floor =asegura que caiga en el bloque correcto/Esto evita bugs en bordes o con cámara
        
        // límites 
        if (row < 0 || col < 0 || row >= world.getRows() || col >= world.getColums()) {
            return;
        }

        switch (currentTool) {

            case WATER:
            world.setTile(row, col, Tile.WATER);
            Emanager.removeEntitiesAt(col, row);
            break;

            case GRASS:
                int variant = controlPanel.getSelectedGroundVariant();
                world.setTile(row, col, variant);
                break;

            case Lummon:
                Emanager.addEntity(new Lummon(col,row ,Emanager));
                break;

            case Zyrox:
                Emanager.addEntity(new Zyrox(col, row,Emanager));
                break;
                    
            case Nero:
                Emanager.addEntity(new Nero(col, row));
                break;

            case Zethar:
                Emanager.addEntity(new Zenthra(col, row));
                break;
                
                
            case RAYO:
                LightningEffect ray = jpanel.getLightning();
                if (!ray.puedeUsarse()) break;
                ray.lanzar(col, row);
                for (Entities.Entity e : Emanager.getEntities()) {
                    if (e.getTileX() == col && e.getTileY() == row) {
                        e.takeDamage(80);
                    }
                }
    break;
        }
        
        

        jpanel.repaint();
    }
    
    private boolean isPaintTool() {
        return currentTool == Tool.WATER ||
               currentTool == Tool.GRASS;
    }


    public void setTool(Tool tool) {
        this.currentTool = tool;
        System.out.println("Tool: " + tool);
    }


    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseMoved(MouseEvent e) {}
}