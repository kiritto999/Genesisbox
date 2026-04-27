/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.World;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Bayas:
 * @author Friedrick
 */
public class Food extends Resource {
 
    public static final Color COLOR_MADURA   = new Color(220, 50,  80);
    public static final Color COLOR_CRECIENDO = new Color(200, 160, 80);
    public static final Color COLOR_VACIA    = new Color(100, 80,  60);
 
    private boolean madura;
 
    public Food(int tileX, int tileY) {
        super("Bayas", tileX, tileY,
                /*maxHealth*/   20,
                /*maxQuantity*/  5,
                /*regenRate*/    1,
                /*regenInterval*/60);
        this.madura = true;
    }
 
    @Override
    public void update(World world) {
        super.update(world);
        madura = (quantity == maxQuantity);
    }
 
    @Override
    public int harvest(int amount) {
        int taken = super.harvest(amount);
        if (taken > 0) madura = false;
        return taken;
    }
    
     @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        float visualSize = getVisualSize();
        int size = Math.max(6, (int)(tileSize * visualSize));
        int offset = (tileSize - size) / 2;
 
        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;
 
        g.setColor(getCurrentColor());
        int bs = size / 3;
        // 3 bolitas formando un triángulo
        g.fillOval(px + offset,              py + offset,      bs, bs);
        g.fillOval(px + offset + bs,         py + offset + 2,  bs, bs);
        g.fillOval(px + offset + bs / 2,     py + offset + bs, bs, bs);
    }
 
    public boolean isMadura() { return madura; }
 
    public Color getCurrentColor() {
        if (isDepleted())            return COLOR_VACIA;
        if (getFillRatio() >= 0.8f)  return COLOR_MADURA;
        return COLOR_CRECIENDO;
    }
 
    public float getVisualSize() { return 0.35f; }
}
