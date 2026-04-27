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
 * Zyrox: zorro herbívoro, presa del Lummon.
 * Requiere al menos 3 Lummons para ser cazado.
 */
public class Zyrox extends Animal {

    Random random = new Random();

    public Zyrox (int tileX, int tileY, Entitymanager manager) {
        super(tileX, tileY, manager);  

        name      = "Zyrox";
        maxHealth = 40 + random.nextInt(21);  // 40-60
        health    = maxHealth;

        energy       = 50 + random.nextInt(21);  // 50-70
        sex          = random.nextBoolean() ? Sex.MALE : Sex.FEMALE;
        habitat      = Tile.GRASS;
        foodType     = FoodType.HERBIVORE;

        hunger = CAP_HAMBRE; // empieza lleno
        thirst = CAP_SED;    // empieza lleno

        speed        = 3 + random.nextInt(3);  // 3-5
        attack       = 3 + random.nextInt(3);  // 3-5 
        intelligence = 1 + random.nextInt(3);  // 1-3
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
        g.setColor(new Color(210, 120, 40)); // Naranjita Naranjón Pipolón
        g.fillOval(
            cameraX + tileX * tileSize + tileSize / 5,
            cameraY + tileY * tileSize + tileSize / 5,
            tileSize * 3/5,   // un poco más grande que el Lummon
            tileSize * 3/5
        );
    }
}
