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
        super("Arbol", tileX, tileY, 50, 10, 0, 0);
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

        int sx = cameraX + tileX * tileSize + slotOffsetX[slot] * half;
        int sy = cameraY + tileY * tileSize + slotOffsetY[slot] * half;
        int cx = sx + half / 2;

        // SEMILLA: solo un punto
        if (stage == Etapa_Crecimiento.SEMILLA) {
            int r = Math.max(3, half / 6);
            g2.setColor(COLOR_SEMILLA);
            g2.fillOval(cx - r, sy + half / 2 - r, r * 2, r * 2);
            return;
        }

        // Radio de copa según etapa
        int copaR = switch (stage) {
            case JOVEN  -> Math.max(4, (int)(half * 0.30));
            case MADURO -> Math.max(5, (int)(half * 0.38));
            case VIEJO  -> Math.max(5, (int)(half * 0.43));
            default     -> Math.max(4, (int)(half * 0.30));
        };

        // Tronco anclado en la base del slot
        int tw = Math.max(2, half / 8);
        int th = Math.max(4, half / 3);
        int trunkX = cx - tw / 2;
        int trunkY = sy + half - th;

        // Copa justo encima del tronco, sin salirse por arriba
        int copaY = Math.max(sy, trunkY - copaR * 2);
        
        // Tronco (dibujado DESPUÉS para que quede encima de la copa)
        g2.setColor(new Color(100, 60, 20));
        g2.fillRect(trunkX, trunkY, tw, th);
        
        // Copa — un solo círculo
        g2.setColor(getCurrentColor());
        g2.fillOval(cx - copaR, copaY, copaR * 2, copaR * 2);

        
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
}