/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.World;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Random;


/**
 *
 * @author blope
 */
public class Lummon extends Animal {
    Random random;
    
    public Lummon(int x, int y) {
        super(x, y);
        life = true;
        
        health = random.nextInt(25,50);
        energy = random.nextInt(150,300);
        
        sex = 1;
        habitat= 1;
        food=1;
        
        hunger = 0;
        thirst = 0;

        speed = random.nextInt(4,5);
        attack = random.nextInt(1,2);
        intelligence = random.nextInt(1,4);
    }

    @Override
    public void update(World world) {
        super.update(world);
    }
    
   
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        g.setColor(Color.WHITE);

        g.fillOval(
            cameraX + tileX * tileSize,
            cameraY + tileY * tileSize,
            tileSize,
            tileSize
        );
    }
}
