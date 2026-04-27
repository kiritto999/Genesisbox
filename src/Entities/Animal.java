/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.World;

/**
 *
 * @author blope
 */
public abstract class Animal extends Entity {

    protected boolean life;
    
    protected int health;
    protected int energy;
    
    protected int sex;
    protected int habitat;
    protected int food;
    
    protected int hunger;
    protected int thirst;

    protected int speed;
    protected int attack;
    protected int intelligence;

    public Animal(int x, int y) {
        super(x, y);
    }

    @Override
    public void update(World world) {      
    
        hunger++;
        thirst++;
        energy--;
    }
}