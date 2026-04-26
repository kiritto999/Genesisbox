/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.Tile;

enum Sex { MALE, FEMALE }
enum FoodType { HERBIVORE, CARNIVORE }

public abstract class Animal extends Entity {

    protected int energy;

    protected Sex sex;
    protected int habitat;
    protected FoodType foodType;

    protected int hunger;
    protected int thirst;

    protected int speed;
    protected int attack;
    protected int intelligence;

    public Animal(int tileX, int tileY) {
        super(tileX, tileY);
    }

    @Override
    public void update(World.World world) {
        hunger++;
        thirst++;
        energy--;
    }
}