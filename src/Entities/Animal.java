/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.Tile;

enum Sex { MALE, FEMALE }
enum FoodType { HERBIVORE, CARNIVORE }

public abstract class Animal extends Entity {

    public static final int CAP_VIDA         = 300;
    public static final int CAP_ENERGIA      = 150;
    public static final int CAP_ATAQUE       = 50;
    public static final int CAP_VELOCIDAD    = 10;
    public static final int CAP_INTELIGENCIA = 100;
    public static final int CAP_HAMBRE       = 150;

    protected int energy;
    protected int hunger;
    protected int speed;
    protected int attack;
    protected int intelligence;
    protected int capacity;

    protected Sex      sex;
    protected int      habitat;
    protected FoodType foodType;
    protected Entitymanager entitymanager;

    // ── Timers ─────────────────────────────────────────────────────────
    private double moveTimer   = 0;
    private double hambreTimer = 0;

    private static final double HAMBRE_INTERVAL = 1.0;

    // ── Combate ────────────────────────────────────────────────────────
    private double attackTimer = 0;
    private static final double ATTACK_INTERVAL = 0.6;

    public Animal(int tileX, int tileY, Entitymanager manager) {
        super(tileX, tileY, manager);
        this.entitymanager = manager;
        this.hunger = CAP_HAMBRE;
    }

    @Override
    public void update(World.World world) {}

    public void update(World.World world, double deltaTime) {
        if (!alive) return;

        // ── Hambre ───────────────────────────────────────────────────
        hambreTimer += deltaTime;
        if (hambreTimer >= HAMBRE_INTERVAL) {
            hambreTimer = 0;
            hunger = Math.max(0, hunger - 1);
            if (hunger == 0) takeDamage(2);
        }

        // ── Ataque timer ─────────────────────────────────────────────
        attackTimer += deltaTime;

        // ── Movimiento ───────────────────────────────────────────────
        moveTimer += deltaTime;
        double interval = 3.0 / Math.max(1, speed);
        if (moveTimer >= interval) {
            moveTimer = 0;
            decidirAccion(world);
            energy = Math.max(0, energy - 1);
        }
    }

    protected void decidirAccion(World.World world) {
        moverAleatorio(world);
    }

    // ── Movimiento aleatorio ────────────────────────────────────────────
    protected void moverAleatorio(World.World world) {
        int[][] dirs = {{0,-1},{0,1},{1,0},{-1,0}};
        java.util.Random rng = new java.util.Random();
        for (int i = dirs.length - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int[] tmp = dirs[i]; dirs[i] = dirs[j]; dirs[j] = tmp;
        }
        for (int[] dir : dirs) {
            int nx = tileX + dir[0];
            int ny = tileY + dir[1];
            if (esMovimientoValido(nx, ny, world)) {
                int slot2 = entitymanager.getSlotLibre(nx, ny, capacity);
                if (slot2 == -1) continue;
                tileX = nx; tileY = ny; slot = slot2;
                return;
            }
        }
    }

    // ── Moverse hacia un objetivo ──────────────────────────────────────
    protected void moverHacia(int tx, int ty, World.World world) {
        int dx = Integer.compare(tx, tileX);
        int dy = Integer.compare(ty, tileY);

        int[][] intentos = { {dx, 0}, {0, dy}, {dx, dy} };
        for (int[] d : intentos) {
            if (d[0] == 0 && d[1] == 0) continue;
            int nx = tileX + d[0];
            int ny = tileY + d[1];
            if (esMovimientoValido(nx, ny, world)) {
                int s = entitymanager.getSlotLibre(nx, ny, capacity);
                if (s == -1) continue;
                tileX = nx; tileY = ny; slot = s;
                return;
            }
        }
        moverAleatorio(world);
    }

    // ── Adyacente a tile objetivo ──────────────────────────────────────
    protected boolean esAdyacente(int tx, int ty) {
        int dx = Math.abs(tileX - tx);
        int dy = Math.abs(tileY - ty);
        return (dx + dy) == 1;
    }

    // ── Atacar entidad en tile adyacente ──────────────────────────────
    protected boolean intentarAtacar(Entity objetivo) {
        if (objetivo == null || !objetivo.isAlive()) return false;
        if (!esAdyacente(objetivo.getTileX(), objetivo.getTileY())) return false;

        if (attackTimer >= ATTACK_INTERVAL) {
            attackTimer = 0;
            objetivo.takeDamage(attack);
            return true;
        }
        return false;
    }

    // ── Comer un recurso de comida ─────────────────────────────────────
    protected boolean intentarComer(Food food) {
        if (food == null || !food.isAlive() || food.isDepleted()) return false;
        if (food.getTileX() != tileX || food.getTileY() != tileY) {
            if (!esAdyacente(food.getTileX(), food.getTileY())) return false;
        }
        int cosechado = food.harvest(10);
        if (cosechado > 0) {
            hunger = Math.min(CAP_HAMBRE, hunger + cosechado * 5);
            heal(cosechado * 2);
            return true;
        }
        return false;
    }

    // ── Comer un cadáver ───────────────────────────────────────────────
    protected boolean intentarComerCadaver(Corpse corpse) {
        if (corpse == null || !corpse.isAlive() || corpse.isDepleted()) return false;
        if (corpse.getTileX() != tileX && !esAdyacente(corpse.getTileX(), corpse.getTileY())) return false;

        int cosechado = corpse.harvest(1);
        if (cosechado > 0) {
            hunger = Math.min(CAP_HAMBRE, hunger + 80);
            heal(20);
            return true;
        }
        return false;
    }

    // ── Validación de movimiento ───────────────────────────────────────
    protected boolean esMovimientoValido(int nx, int ny, World.World world) {
        if (nx < 0 || ny < 0 || nx >= world.getColums() || ny >= world.getRows()) return false;
        if (world.getMap()[ny][nx].getType() != Tile.GRASS) return false;
        return entitymanager.contarEspacioTile(nx, ny) + capacity <= 5;
    }

    // ── Getters ────────────────────────────────────────────────────────
    public int      getEnergy()       { return energy; }
    public int      getHunger()       { return hunger; }
    public int      getSpeed()        { return speed; }
    public int      getAttack()       { return attack; }
    public int      getIntelligence() { return intelligence; }
    public Sex      getSex()          { return sex; }
    public FoodType getFoodType()     { return foodType; }
    public int      getCapacity()     { return capacity; }
    public int      getHabitat()      {return habitat;}
    
    
    // -- Setters ────────────────────────────────────────────────────────
    public void setEnergy(int energy) {this.energy = energy;}
    public void setHunger(int hunger) {this.hunger = hunger;}
    public void setSpeed(int speed) {this.speed = speed;}
    public void setAttack(int attack) {this.attack = attack;}
    public void setIntelligence(int intelligence) {this.intelligence = intelligence;}
    public void setCapacity(int capacity) {this.capacity = capacity;}
    public void setHabitat(int habitat) {this.habitat = habitat;}
    public void setSex(Sex sex) {this.sex = sex;}
    public void setFoodType(FoodType foodType) {this.foodType = foodType;}
}