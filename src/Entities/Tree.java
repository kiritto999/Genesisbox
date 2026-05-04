/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * Tree (Synthra): Árbol con etapas de crecimiento.
 */
public class Tree extends Resource {

    public enum Etapa_Crecimiento { SEMILLA, JOVEN, MADURO, VIEJO }

    private Etapa_Crecimiento stage;
    private int Tiempo_Crecimiento;
    private static final int INTERVALO_CRECIMIENTO = 1200;

    public static final Color COLOR_SEMILLA = new Color(219, 54,  36);
    public static final Color COLOR_JOVEN   = new Color(180, 44,  29);
    public static final Color COLOR_MADURO  = new Color(140, 35,  23);
    public static final Color COLOR_VIEJO   = new Color( 79, 20,  13);

    public Tree(int tileX, int tileY) {
        super("Arbol", tileX, tileY,
                /*VidaMax*/   50,
                /*CantMax*/   10,
                /*RegeRate*/   0,
                /*RegeInter*/  0);
        this.stage              = Etapa_Crecimiento.SEMILLA;
        this.Tiempo_Crecimiento = 0;
        this.quantity           = 2;
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
        if (health <= 0) alive = false;
    }

    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[] slotOffsetX = {0, 1, 0, 1, 0};
        int[] slotOffsetY = {0, 0, 1, 1, 2};
        int half = tileSize / 2;

        int px = cameraX + tileX * tileSize + slotOffsetX[slot] * half;
        int py = cameraY + tileY * tileSize + slotOffsetY[slot] * half;

        float vs   = getVisualSize();
        // Limitar el tamaño al cuadrante (half) para respetar el slot
        int size   = (int)(half * Math.min(vs, 0.95f));
        // Centro del slot
        int cx = px + half / 2;
        int cy = py + half / 2;

        if (stage == Etapa_Crecimiento.SEMILLA) {
            g2.setColor(COLOR_SEMILLA);
            int s = Math.max(4, size);
            g2.fillOval(cx - s / 2, cy - s / 2, s, s);
            return;
        }

        // Tronco centrado en el slot
        int tw = Math.max(4, size / 4);
        int th = Math.max(6, size / 2);
        g2.setColor(new Color(100, 60, 20));
        g2.fillRect(cx - tw / 2, cy + size / 2 - th, tw, th);

        // Copa centrada en el slot
        g2.setColor(getCurrentColor());
        g2.fillOval(cx - size / 2, cy - size / 2, size, size - th / 3);

        // Copa secundaria
        if (stage == Etapa_Crecimiento.MADURO || stage == Etapa_Crecimiento.VIEJO) {
            g2.setColor(getCurrentColor().darker());
            int s2 = (int)(size * 0.60);
            g2.fillOval(cx - s2 / 2 + size / 4, cy - s2 / 4, s2, s2);
        }

        // Brillo
        g2.setColor(new Color(255, 255, 255, 35));
        g2.fillOval(cx - size / 4, cy - size / 3, size / 3, size / 4);
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

    // Tamaños aumentados respecto a la versión anterior
    public float getVisualSize() {
        return switch (stage) {
            case SEMILLA -> 0.18f;  
            case JOVEN   -> 0.80f;  
            case MADURO  -> 1.20f;  
            case VIEJO   -> 1.60f;  
        };
    }
}
    

