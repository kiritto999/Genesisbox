package Entities;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Clase base para todas las entidades del mundo.
 */
public abstract class Entity {

    protected int tileX;
    protected int tileY;
    protected String name;
    protected int health;
    protected int maxHealth;
    protected boolean alive;

    public enum EntityType {
        ANIMAL, RESOURCE
    }

    protected EntityType type;

// En Entity.java dos constructores:
public Entity(int tileX, int tileY) {
    this.tileX   = tileX;
    this.tileY   = tileY;
    this.alive   = true;
    this.type    = EntityType.ANIMAL;
}

public Entity(String name, int tileX, int tileY, int maxHealth, EntityType type) {
    this.name      = name;
    this.tileX     = tileX;
    this.tileY     = tileY;
    this.maxHealth = maxHealth;
    this.health    = maxHealth;
    this.alive     = true;
    this.type      = type;
}

    public abstract void update(World.World world);
    
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {}

    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
        if (health == 0) alive = false;
    }

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public int        getTileX()     { return tileX; }
    public int        getTileY()     { return tileY; }
    public String     getName()      { return name; }
    public int        getHealth()    { return health; }
    public int        getMaxHealth() { return maxHealth; }
    public boolean    isAlive()      { return alive; }
    public EntityType getType()      { return type; }

    public void setTileX(int x) { this.tileX = x; }
    public void setTileY(int y) { this.tileY = y; }
}