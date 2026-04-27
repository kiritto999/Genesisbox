/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.World;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Graphics2D;

/**
 *
 * @author Friedrick
 */
public class Nero extends Resource {
 
    public static final Color COLOR_LLENA  = new Color(49, 120, 34);
    public static final Color COLOR_VACIA  = new Color(37, 77, 26);
 
    public Nero(int tileX, int tileY) {
        super("Nero", tileX, tileY,
                /*maxHealth*/   100,
                /*maxQuantity*/  15,
                /*regenRate*/     1,
                /*regenInterval*/300);
    }
 
    @Override
    public void update(World world) {
        super.update(world);
    }
    
        @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        int[] slotOffsetX = {0, 1, 0, 1, 0};
        int[] slotOffsetY = {0, 0, 1, 1, 2};
        int half = tileSize / 2;

        int px = cameraX + tileX * tileSize + slotOffsetX[slot] * half;
        int py = cameraY + tileY * tileSize + slotOffsetY[slot] * half;

        float visualSize = getVisualSize();
        int size = (int)(half * visualSize);
        int offset = (half - size) / 2;
 
        // Cuerpo de la piedra
        g.setColor(getCurrentColor());
        g.fillRoundRect(px + offset, py + offset + size / 4, size, size * 3 / 4, 8, 8);
 
        // Borde
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(getCurrentColor().darker());
        g2.setStroke(new BasicStroke(1f));
        g2.drawRoundRect(px + offset, py + offset + size / 4, size, size * 3 / 4, 8, 8);
    }
 
    public Color getCurrentColor() {
        return isDepleted() ? COLOR_VACIA : COLOR_LLENA;
    }
 
    public float getVisualSize() { return 0.90f; }
    
}
