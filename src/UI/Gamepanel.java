package UI;

import Entities.*;
import Inputs.*;
import Utils.Assets;
import World.*;
import Utils.TimeDay;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import Utils.LightningEffect;
import java.awt.image.BufferedImage;
import Utils.StarEffect;

public class GamePanel extends JPanel {

    public final int UNIT_SIZE = 32;

    InfoPanel infoP;
    World world;
    Camera camera;
    Entitymanager entitymanager;

    boolean dgrid = false;

    private Entity followedEntity;
    private TimeDay time;
    // ──────── LIGHTNING EFFCT ────
    private LightningEffect lightning = new LightningEffect();
    
    // ──────── STAR EFFCT ────
    private StarEffect star = new StarEffect();

    // =========================
    // BACKGROUND IMAGE
    // =========================

    //private Image oceanBackground;

    public GamePanel(World world,Entitymanager entitymanager,InfoPanel infoPanel,Camera camera,TimeDay time) {
        this.infoP = infoPanel;
        this.world = world;
        this.entitymanager = entitymanager;
        this.camera = camera;
        this.time = time;

        setOpaque(true);

        // =========================
        // LOAD BACKGROUND
        // =========================

        /*oceanBackground = new ImageIcon(
                getClass().getResource("/resources/Videos/Ocean.png")
        ).getImage();*/
    }

    // =========================
    // RENDER
    // =========================

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // =========================
        // DRAW BACKGROUND
        // =========================
        /*g.drawImage(
                oceanBackground,
                0,
                0,
                getWidth(),
                getHeight(),
                this
        );*/
        g.setColor(new Color(25, 90, 145));

        g.fillRect(
                0,
                0,
                getWidth(),
                getHeight()
        );

        // =========================
        // FOLLOW ENTITY
        // =========================

        if (followedEntity != null && followedEntity.isAlive()) {

            int size = (int) (UNIT_SIZE * camera.zoom);

            camera.Camerax =
                    getWidth() / 2 -
                    (followedEntity.getTileX() * size);

            camera.Cameray =
                    getHeight() / 2 -
                    (followedEntity.getTileY() * size);

            limitForCamera(camera);
        }

        DrawIsland(g);
        DrawTest(g);
        drawGrid(g);
        
        lightning.update();
        lightning.draw(g, (int)(UNIT_SIZE * camera.zoom), camera.Camerax, camera.Cameray, this);
        
        star.update();
        StardImpact();
        star.draw(g, (int)(UNIT_SIZE * camera.zoom), camera.Camerax, camera.Cameray, this);
        
        drawNight(g);
    }

    // =========================
    // GRID
    // =========================

    private void drawGrid(Graphics g) {

        if (dgrid) {

            int size = (int) (UNIT_SIZE * camera.zoom);

            g.setColor(Color.BLACK);

            for (int c = 0; c <= world.getColums(); c++) {

                int x = camera.Camerax + (int) (c * size);

                g.drawLine(
                        x,
                        camera.Cameray,
                        x,
                        camera.Cameray + world.getRows() * size
                );
            }

            for (int r = 0; r <= world.getRows(); r++) {

                int y = camera.Cameray + (int) (r * size);

                g.drawLine(
                        camera.Camerax,
                        y,
                        camera.Camerax + world.getColums() * size,
                        y
                );
            }
        }
    }

    // =========================
    // MAP
    // =========================

    public void DrawIsland(Graphics g) {

        Tile[][] map = world.getMap();
        int size = (int) (UNIT_SIZE * camera.zoom);

        int startCol = Math.max(0, (-camera.Camerax / size) - 2);
        int endCol = Math.min(
                world.getColums(),startCol + (getWidth() / size) + 4
        );

        int startRow = Math.max(0, (-camera.Cameray / size) - 2);
        int endRow = Math.min(
                world.getRows(),startRow + (getHeight() / size) + 4
        );

        // Calcular UNA sola vez por frame
        int waterFrame =(int) ((System.currentTimeMillis() / 120)% Assets.waterFrames.length);
        BufferedImage currentWater =Assets.waterFrames[waterFrame];

        for (int r = startRow; r < endRow; r++) {
            for (int c = startCol; c < endCol; c++) {
                int x = camera.Camerax + (c * size);
                int y = camera.Cameray + (r * size);

                Tile tile = map[r][c];
                BufferedImage sprite;
                if (tile.getType() == Tile.WATER) {
                    sprite = currentWater;
                } else {
                    sprite = tile.getSprite();
                }
                g.drawImage(
                        sprite,
                        x,
                        y,
                        size,
                        size,
                        null
                );
            }
        }
    }

    // =========================
    // NIGHT
    // =========================

    private void drawNight(Graphics g) {

        int hour = time.getHour();

        int alpha = 0;

        switch (hour) {

            case 0:
                alpha = 110;
                break;

            case 1:
                alpha = 50;
                break;

            case 2:
                alpha = 0;
                break;

            case 3:
                alpha = 60;
                break;

            case 4:
                alpha = 140;
                break;
        }

        g.setColor(new Color(0, 0, 0, alpha));

        g.fillRect(0, 0, getWidth(), getHeight());
    }

    // =========================
    // CAMERA LIMIT
    // =========================

    public void limitForCamera(Camera camera) {

        int tileSize = (int) (UNIT_SIZE * camera.zoom);
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

    // =========================
    // ENTITIES
    // =========================

    public void DrawTest(Graphics g) {

        synchronized (entitymanager) {

            List<Entity> todo = new ArrayList<>();

            todo.addAll(entitymanager.getResources());

            todo.addAll(entitymanager.getAnimals());

            todo.sort(new Comparator<Entity>() {

                @Override
                public int compare(Entity a, Entity b) {

                    return Integer.compare(a.getTileY(), b.getTileY());
                }
            });

            for (Entity e : todo) {
                e.draw(
                        g,
                        (int) (UNIT_SIZE * camera.zoom),
                        camera.Camerax,
                        camera.Cameray
                );
            }
        }
    }

    // =========================
    // CLICK HANDLER
    // =========================

    public void handleClick(int mouseX, int mouseY) {

        int size = (int)(UNIT_SIZE * camera.zoom);

        int tileX =
                (mouseX - camera.Camerax) / size;

        int tileY =
                (mouseY - camera.Cameray) / size;

        if (tileX < 0 ||
            tileY < 0 ||
            tileX >= world.getColums() ||
            tileY >= world.getRows()) {

            return;
        }

        Tile tile = world.getMap()[tileY][tileX];

        ArrayList<Entity> entitiesInTile =
                new ArrayList<>();

        for (Entity e : entitymanager.getEntities()) {

            if (e.getTileX() == tileX &&
                e.getTileY() == tileY) {

                entitiesInTile.add(e);
            }
        }

        if (infoP != null) {

            infoP.setSelected(
                    tileX,
                    tileY,
                    tile,
                    null,
                    null
            );

            infoP.setTileEntities(entitiesInTile);
        }
    }
    
    

    // =========================
    // CAMERA ENTITY CONTROL
    // =========================

    public void focusEntity(Entity e) {

        if (e == null) return;

        int size = (int)(UNIT_SIZE * camera.zoom);

        int targetX =
                getWidth() / 2 -
                (e.getTileX() * size);

        int targetY =
                getHeight() / 2 -
                (e.getTileY() * size);

        camera.Camerax = targetX;
        camera.Cameray = targetY;

        limitForCamera(camera);

        repaint();
    }
    //crea el hueco del impacto 
    public void StardImpact(){
        if (star.debeAplicarImpacto()) {
        int centerRow = star.getTileY();
        int centerCol = star.getTileX();
        int radio = 4;

        for (int row = centerRow - radio; row <= centerRow + radio; row++) {

            for (int col = centerCol - radio; col <= centerCol + radio; col++) {

                if (row < 0 || row >= world.getRows()
                        || col < 0 || col >= world.getColums()) {
                    continue;
                }

                int dx = col - centerCol;
                int dy = row - centerRow;

                // círculo
                if (dx * dx + dy * dy <= radio * radio) {

                    world.setTile(row, col, Tile.WATER);

                }
            }
        }
        entitymanager.removeEntitiesInRadius(centerCol,centerRow,radio);
        star.marcarImpactoAplicado();
        }
    }
    

    public void followEntity(Entity e) {

        if (e == null) return;

        followedEntity = e;
    }

    public void stopFollowing() {

        followedEntity = null;
    }

    // =========================
    // GETTERS
    // =========================

    public int getUNIT_SIZE() {
        return UNIT_SIZE;
    }

    public Camera getCamera() {
        return camera;
    }

    public Entitymanager getEntityManager() {
        return entitymanager;
    }
    public LightningEffect getLightning() {
        return lightning; 
    }
    public StarEffect getStar() {
        return star; }
}