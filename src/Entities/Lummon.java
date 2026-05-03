/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import World.Tile;

/**
 * Lummon: Conejo carnivoro, cazador en manada.
 * Necesita al menos 3 para cazar un zyrox.
 * @author Friedrick
 */

public class Lummon extends Animal {

    Random random = new Random();

    public Lummon(int tileX, int tileY, Entitymanager manager) {
        super(tileX, tileY, manager);  

        name         = "Lummon";
        maxHealth    = 15 + random.nextInt(11);  // 15-25
        health       = maxHealth;
        capacity     = 1;
        energy       = 30 + random.nextInt(21);  // 30-50
        sex          = random.nextBoolean() ? Sex.MALE : Sex.FEMALE;
        habitat      = Tile.GRASS;
        foodType     = FoodType.CARNIVORE;
        
        hunger = CAP_HAMBRE; // empieza lleno
        thirst = CAP_SED;    // empieza lleno
        speed        = 2 + random.nextInt(3);   // 2-4
        attack       = 2 + random.nextInt(3);   // 2-4
        intelligence = 3 + random.nextInt(3);   // 3-4
    }

    @Override
    public void update(World.World world) {
        // usa update(world, deltaTime) desde el GameLoop
    }
 
    public void update(World.World world, double deltaTime) {
        super.update(world, deltaTime);
    }
 
    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {

        int[] slotOffsetX = {0, 1, 0, 1, 0};
        int[] slotOffsetY = {0, 0, 1, 1, 2};

        int half = tileSize / 3;

        int px = cameraX + tileX * tileSize + slotOffsetX[slot] * half;
        int py = cameraY + tileY * tileSize + slotOffsetY[slot] * half;

        //cuerpo
        g.setColor(Color.CYAN);
        g.fillOval(px, py, half, half);


        //borde del cuerpo 
        g.setColor(Color.BLACK);
        g.drawOval(px, py, half, half);


        //ojos
        int eyeSize = half / 6;
        g.setColor(Color.RED);
        g.fillOval(px + half/4, py + half/3, eyeSize, eyeSize);
        g.fillOval(px + half/2, py + half/3, eyeSize, eyeSize);
    }
}