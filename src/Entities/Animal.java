/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.Tile;

enum Sex { MALE, FEMALE }
enum FoodType { HERBIVORE, CARNIVORE }

public abstract class Animal extends Entity {
    
    // ── Caps máximos globales ──────────────────────────────────────────
    public static final int CAP_VIDA          = 300;
    public static final int CAP_ENERGIA       = 150;
    public static final int CAP_ATAQUE        = 50;
    public static final int CAP_VELOCIDAD     = 10;
    public static final int CAP_INTELIGENCIA  = 100;
    public static final int CAP_HAMBRE        = 100;
    public static final int CAP_SED           = 100;
    
    // ── Stats ─────────────────────────────────────────────────────────────
    protected int energy;
    protected int hunger;   // empieza en MAX, baja con el tiempo
    protected int thirst;   // empieza en MAX, baja con el tiempo
    protected int speed;
    protected int attack;
    protected int intelligence;
    protected int capacity;
    
    // ── Info ──────────────────────────────────────────────────────────────
    protected Sex      sex;
    protected int      habitat;
    protected FoodType foodType;
    protected Entitymanager entitymanager;
    
    // ── Timers de juego (en segundos reales) ─────────────────────────
    private double moveTimer    = 0; // se mueve cada 2 segundos
    private double hambreTimer  = 0; // hambre baja cada 10 segundos
    private double sedTimer     = 0; // sed baja cada 7 segundos
    private static final double MOVE_INTERVAL = 2.0; // 2 segundos 
    private static final double HAMBRE_INTERVAL = 1.0;
    private static final double SED_INTERVAL    =  7.0;
 
    public Animal(int tileX, int tileY, Entitymanager manager) {
        super(tileX, tileY, manager);
        this.entitymanager = manager;
        this.hunger = CAP_HAMBRE;
        this.thirst = CAP_SED;
    }
 
    /**
     * Recibe deltaTime en segundos reales desde el GameLoop.
     * Se llama desde update(World, double).
     */
    @Override
    public void update(World.World world) {
        // Sin deltaTime no hacemos nada aquí —
        // usa update(World, double) desde el GameLoop
    }
 
    public void update(World.World world, double deltaTime) {
        if (!alive) return;
 
        // ── Hambre ───────────────────────────────────────────────────────
        hambreTimer += deltaTime;
        if (hambreTimer >= HAMBRE_INTERVAL) {
            hambreTimer = 0;
            hunger = Math.max(0, hunger - 1);
            if (hunger == 0) takeDamage(2); // daño por inanición
        }
 
        // ── Sed ──────────────────────────────────────────────────────────
        sedTimer += deltaTime;
        if (sedTimer >= SED_INTERVAL) {
            sedTimer = 0;
            thirst = Math.max(0, thirst - 1);
            if (thirst == 0) takeDamage(3); // sed es más mortal
        }
 
        // ── Movimiento ───────────────────────────────────────────────────
        moveTimer += deltaTime;
        double interval = 3.0 / speed;  // ← reemplaza MOVE_INTERVAL por esto

        if (moveTimer >= interval) {
            moveTimer = 0;
            mover(world);
            energy = Math.max(0, energy - 1);
        }
    }
 
    /**
     * Mueve el animal a una casilla de hierba adyacente aleatoria.
     */
    protected void mover(World.World world) {
        System.out.println("MOVIENDO " + name + " desde " + tileX + "," + tileY);
        int[][] move = {{0,-1},{0,1},{1,0},{-1,0}};
 
        // Mezcla las direcciones para movimiento aleatorio
        java.util.Random rng = new java.util.Random();
        for (int i = move.length - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int[] tmp = move[i]; move[i] = move[j]; move[j] = tmp;
        }
 
        for (int[] dir : move) {
            int nx = tileX + dir[0];
            int ny = tileY + dir[1];
            if (esMovimientoValido(nx, ny, world)) {
                int nuevoSlot = entitymanager.getSlotLibre(nx, ny, capacity);
                if (nuevoSlot == -1) continue; // ← no hay espacio, probar otra dirección
                tileX = nx;
                tileY = ny;
                slot = nuevoSlot;
                return;
            }
        }
    }
 
    protected boolean esMovimientoValido(int nx, int ny, World.World world) {
        if (nx < 0 || ny < 0 || nx >= world.getColums() || ny >= world.getRows()) return false;
        if (world.getMap()[ny][nx].getType() != Tile.GRASS) return false;
        if (entitymanager.contarEspacioTile(nx, ny) + capacity > 5) return false;
        return world.getMap()[ny][nx].getType() == Tile.GRASS;
    }
 
    
    public int getEnergy()       { return energy; }
    public int getHunger()       { return hunger; }
    public int getThirst()       { return thirst; }
    public int getSpeed()        { return speed; }
    public int getAttack()       { return attack; }
    public int getIntelligence() { return intelligence; }
    public Sex getSex()          { return sex; }
    public FoodType getFoodType(){ return foodType; }
    public int getCapacity()     { return capacity; }
      
}