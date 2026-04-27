/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.World;
import java.awt.Color;
import java.awt.Graphics;
/**
 *
 * @author Friedrick
 */
public class Tree extends Resource {
 
    public enum Etapa_Crecimiento { SEMILLA, JOVEN, MADURO, VIEJO }
 
    private Etapa_Crecimiento stage;
    private int Tiempo_Crecimiento;
    private static final int INTERVALO_CRECIMIENTO = 20000;
 
    public static final Color COLOR_SEMILLA = new Color(219, 54, 36);
    public static final Color COLOR_JOVEN = new Color(180, 44, 29);
    public static final Color COLOR_MADURO = new Color(140, 35, 23);
    public static final Color COLOR_VIEJO = new Color(79, 20, 13);
    
    public Tree(int tileX, int tileY) {
        super("Arbol", tileX, tileY,
                /*VidaMax*/   50,
                /*CantMax*/ 10,
                /*RangeRege*/    1,
                /*RegeIntervalo*/100);
        
        this.stage             = Etapa_Crecimiento.SEMILLA;
        this.Tiempo_Crecimiento = 0;
        this.quantity          = 2;
    }
 
    @Override
    public void update(World world) {
        super.update(world);
        if (!alive) return;
 
        Tiempo_Crecimiento++;
        if (Tiempo_Crecimiento >= INTERVALO_CRECIMIENTO) {
            Tiempo_Crecimiento = 0;
            avanzarCrecimiento();
        }
    }
    
    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        float visualSize = getVisualSize();
        int size = (int)(tileSize * visualSize);
        int offset = (tileSize - size) / 2;
 
        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;
 
        // Tronco
        g.setColor(new Color(100, 60, 20));
        int tw = Math.max(3, size / 4);
        int th = Math.max(3, size / 3);
        g.fillRect(px + offset + (size - tw) / 2, py + offset + size - th, tw, th);
 
        // Copa
        g.setColor(getCurrentColor());
        g.fillOval(px + offset, py + offset, size, size - th / 2);
    }
 
    private void avanzarCrecimiento() {
        switch (stage) {
            case SEMILLA -> { stage = Etapa_Crecimiento.JOVEN;  maxQuantity = 5; }
            case JOVEN   -> { stage = Etapa_Crecimiento.MADURO; maxQuantity = 10; maxHealth = 80; health = Math.min(health + 20, 80); }
            case MADURO  -> { stage = Etapa_Crecimiento.VIEJO;  maxQuantity = 6; }
            case VIEJO   -> { /* ya no crece */ }
        }
    }
 
    public Etapa_Crecimiento getStage() { return stage; }
 
    public Color getCurrentColor() {
        return switch (stage) {
            case SEMILLA -> COLOR_SEMILLA;
            case JOVEN   -> COLOR_JOVEN;
            case MADURO  -> COLOR_MADURO;
            case VIEJO   -> COLOR_VIEJO;
        };
    }
 
    public float getVisualSize() {
        return switch (stage) {
            case SEMILLA -> 0.3f;
            case JOVEN   -> 0.55f;
            case MADURO  -> 0.85f;
            case VIEJO   -> 1.0f;
        };
    }

}
    

