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
public class Stone extends Resource {
 
    public static final Color COLOR_LLENA  = new Color(49, 120, 34);
    public static final Color COLOR_VACIA  = new Color(37, 77, 26);
 
    public Stone(int tileX, int tileY) {
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
 
    public Color getCurrentColor() {
        return isDepleted() ? COLOR_VACIA : COLOR_LLENA;
    }
 
    public float getVisualSize() { return 0.6f; }
    
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {}
}
