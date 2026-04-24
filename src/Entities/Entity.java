/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.awt.Graphics;

/**
 *
 * @author blope
 */
public abstract class Entity {
    
    protected int x;
    protected int y;

    public Entity(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void update();
    
    public abstract void draw(Graphics g, int tileSize, int cameraX, int cameraY);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    
}
