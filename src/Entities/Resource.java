/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.World;
import java.awt.Graphics;
/**
 *
 * @author Friedrick
 */
public abstract class Resource extends Entity {
 
    protected int quantity;
    protected int maxQuantity;
    protected int regenRate;
    protected int regenTimer;
    protected int regenInterval;
    protected boolean depleted;
 
    public Resource(String name, int tileX, int tileY, int maxHealth,
                    int maxQuantity, int regenRate, int regenInterval) {
        super(name, tileX, tileY, maxHealth, EntityType.RESOURCE);
        this.maxQuantity   = maxQuantity;
        this.quantity      = maxQuantity;
        this.regenRate     = regenRate;
        this.regenInterval = regenInterval;
        this.regenTimer    = 0;
        this.depleted      = false;
    }
 
    @Override
    public void update(World world) {
        if (!alive) return;
 
        regenTimer++;
        if (regenTimer >= regenInterval) {
            regenTimer = 0;
            if (quantity < maxQuantity) {
                quantity = Math.min(maxQuantity, quantity + regenRate);
                if (quantity > 0) depleted = false;
            }
        }
    }
 
    public int harvest(int amount) {
        if (depleted) return 0;
        int extracted = Math.min(quantity, amount);
        quantity -= extracted;
        if (quantity == 0) depleted = true;
        return extracted;
    }
 
    public int     getQuantity()    { return quantity; }
    public int     getMaxQuantity() { return maxQuantity; }
    public boolean isDepleted()     { return depleted; }
 
    public float getFillRatio() {
        return maxQuantity == 0 ? 0 : (float) quantity / maxQuantity;
    }
        public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {}

    public int getRegenRate() {return regenRate;}
    public int getRegenTimer() {return regenTimer;}
    public int getRegenInterval() {return regenInterval;}
  
    public void setQuantity(int quantity) {this.quantity = quantity;}
    public void setMaxQuantity(int maxQuantity) {this.maxQuantity = maxQuantity;}
    public void setDepleted(boolean depleted) {this.depleted = depleted;}
    public void setRegenRate(int regenRate) {this.regenRate = regenRate;}
    public void setRegenTimer(int regenTimer) {this.regenTimer = regenTimer;}
    public void setRegenInterval(int regenInterval) {this.regenInterval = regenInterval;}
        
    
}
