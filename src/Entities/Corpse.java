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
 * Corpse: Cadáver del Zyrox.
 * quantity = número de Lummons que participaron en la caza (máx 4).
 * Cada Lummon puede hacer harvest(1) para alimentarse.
 * Desaparece cuando quantity == 0 o tras MAX_AGE ticks.
 */
public class Corpse extends Resource {

    private static final Color COLOR_FRESH = new Color(160, 40,  40);
    private static final Color COLOR_OLD   = new Color( 90, 30,  20);
    private static final int   MAX_AGE     = 1500;

    private int age = 0;

    /**
     * @param tileX     tile X
     * @param tileY     tile Y
     * @param cazadores cuántos Lummons participaron en la caza (1-4)
     */
    public Corpse(int tileX, int tileY, int cazadores) {
        super("Cadaver", tileX, tileY,
                /*maxHealth*/    1,
                /*maxQuantity*/  Math.max(1, Math.min(4, cazadores)),
                /*regenRate*/    0,
                /*regenInterval*/0);
        this.quantity = this.maxQuantity;
    }

    @Override
    public void update(World world) {
        if (!alive) return;
        age++;
        if (age >= MAX_AGE || quantity <= 0) {
            alive = false;
        }
    }

    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;

        int w  = (int)(tileSize * 0.52);
        int h  = (int)(tileSize * 0.28);
        int bx = px + (tileSize - w) / 2;
        int by = py + (tileSize - h) / 2 + 2;

        // Sombra
        g2.setColor(new Color(0, 0, 0, 55));
        g2.fillOval(bx + 3, by + 4, w, h);

        // Cuerpo
        Color base = (age < MAX_AGE / 2) ? COLOR_FRESH : COLOR_OLD;
        g2.setColor(base);
        g2.fillOval(bx, by, w, h);

        // Cabeza
        int hs = (int)(tileSize * 0.20);
        g2.setColor(base.darker());
        g2.fillOval(bx - hs / 2, by - hs / 3, hs, hs);

        // X en los ojos
        g2.setColor(Color.WHITE);
        int ex = bx - hs / 2 + hs / 5;
        int ey = by - hs / 3 + hs / 5;
        int es = Math.max(2, hs / 4);
        g2.drawLine(ex,      ey,      ex + es, ey + es);
        g2.drawLine(ex + es, ey,      ex,      ey + es);

        // Puntitos de porciones restantes
        g2.setColor(new Color(255, 210, 80));
        for (int i = 0; i < quantity; i++) {
            g2.fillOval(bx + 5 + i * 9, by + h - 7, 6, 6);
        }
    }

    public int getAge() {return age;}
    
    
}
