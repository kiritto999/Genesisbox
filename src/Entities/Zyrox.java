/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author blope
 */
public class Zyrox extends Animal{
    
        public Zyrox(int tileX, int tileY) {
            super(tileX, tileY);
        }
    
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {

        g.setColor(Color.orange);

        g.fillOval(
            cameraX + tileX * tileSize + tileSize / 4,
            cameraY + tileY * tileSize + tileSize / 4,
            tileSize / 2,
            tileSize / 2
        );
    }
}
