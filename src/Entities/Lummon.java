/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import World.Tile;

public class Lummon extends Animal {

    Random random = new Random();

    public Lummon(int tileX, int tileY) {
        super(tileX, tileY);

        name = "Lummon";
        maxHealth = 50;
        
        health = 25 + random.nextInt(25);
        energy = 150 + random.nextInt(150);

        sex = random.nextBoolean() ? Sex.MALE : Sex.FEMALE;
        habitat = Tile.GRASS;
        foodType = FoodType.HERBIVORE;

        hunger = 0;
        thirst = 0;

        speed = 2 + random.nextInt(3);
        attack = 1 + random.nextInt(2);
        intelligence = 1 + random.nextInt(4);
    }

    @Override
    public void update(World.World world) {
        super.update(world);
    }

    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {

        g.setColor(Color.WHITE);

        g.fillOval(
            cameraX + tileX * tileSize + tileSize / 4,
            cameraY + tileY * tileSize + tileSize / 4,
            tileSize / 4,
            tileSize / 4
        );
    }
}