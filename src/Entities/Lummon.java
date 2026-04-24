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
public class Lummon extends Animal {

    public Lummon(int x, int y) {
        super(x, y);
        life = true;
        health = 50;
        energy = 60;
        sex = 1;
        
        
        hunger = 0;
        thirst = 0;

        speed = 3;
        attack = 2;
        intelligence = 4;
    }

    @Override
    public void update() {

    }
    
    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        g.setColor(Color.WHITE);

        g.fillOval(
            cameraX + x * tileSize,
            cameraY + y * tileSize,
            tileSize,
            tileSize
        );
    }
}
