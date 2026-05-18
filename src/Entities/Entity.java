package Entities;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Clase base para todas las entidades del mundo.
 */
public abstract class Entity {

    private static int Next_Id = 1;
    protected int id;
    protected int tileX;
    protected int tileY;
    protected String name;
    protected String customName;
    protected Color baseColor;  // color original de especie
    protected Color customColor = null;// color personalizado del jugador
    protected int health;
    protected int maxHealth;
    protected boolean alive;
    protected int slot = 0;
    protected Entitymanager manager;

    public enum EntityType {
        ANIMAL,
        RESOURCE
    }

    protected EntityType type;

    public Entity(int tileX, int tileY, Entitymanager manager) {
        this.id = Next_Id++;
        this.customName = getClass().getSimpleName();
        this.customColor = null;
        this.tileX = tileX;
        this.tileY = tileY;
        this.alive = true;
        this.type = EntityType.ANIMAL;
        this.manager = manager;
    }

    public Entity(String name,int tileX,int tileY,int maxHealth,EntityType type) {

        this.id = Next_Id++;
        this.name = name;
        this.customName = name;

        this.customColor = null;

        this.tileX = tileX;
        this.tileY = tileY;

        this.maxHealth = maxHealth;
        this.health = maxHealth;

        this.alive = true;

        this.type = type;
    }

    public abstract void update(World.World world);

    public void draw(Graphics g, int tileSize,int cameraX,int cameraY ) {}
    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
        if (health == 0) {
            alive = false;
        }
    }

    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    public Color getRenderColor() {

        if (customColor != null) {
            return customColor;
        }
        return baseColor;
    }

    public int getId() {return id;}
    public int getTileX() { return tileX;}
    public int getTileY() { return tileY;}
    public String getName() {return name;}
    public String getCustomName() { return customName;}
    public Color getCustomColor() {return customColor; }
    public Color getBaseColor() { return baseColor;}
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth;}
    public boolean isAlive() {   return alive; }
    public EntityType getType() { return type; }
    public void setTileX(int x) {    this.tileX = x; }
    public void setTileY(int y) { this.tileY = y; }
    public void setCustomName(String name) {this.customName = name;  }
    public void setCustomColor(Color color) { this.customColor = color; }
}