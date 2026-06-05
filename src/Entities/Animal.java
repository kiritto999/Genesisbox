package Entities;

import World.Tile;
import java.util.Random;



public abstract class Animal extends Entity {

    public static enum Sex {
        MALE,
        FEMALE
    }

    enum FoodType {
        HERBIVORE,
        CARNIVORE
    }
    public static final int CAP_VIDA         = 400;
    public static final int CAP_ENERGIA      = 200;
    public static final int CAP_ATAQUE       = 60;
    public static final int CAP_VELOCIDAD    = 12;
    public static final int CAP_INTELIGENCIA = 100;
    public static final int CAP_HAMBRE       = 800;

    // ── Etapas de vida ─────────────────────────────────────────────────
    public enum Etapa { CACHORRO, ADULTO, VIEJO }

    // Días del mundo por etapa (basado en tu TimeDay)
    // 1 año = 160 días en tu sistema
    protected static final int DIAS_ADULTO = 160;   // 1 año → adulto
    private static final int DIAS_VIEJO  = 480;   // 3 años → viejo (1+2)
    private static final int DIAS_MUERTE = 640;   // 4 años → muerte por vejez

    protected Etapa etapa = Etapa.CACHORRO;
    protected int   edadDias = 0;          // días acumulados del mundo
    private   int   ultimoDia = -1;        // para detectar cambio de día

    // ── Stats base ─────────────────────────────────────────────────────
    protected int energy;
    protected int hunger;
    protected int speed;
    protected int attack;
    protected int intelligence;
    protected int capacity;

    protected Sex         sex;
    protected int         habitat;
    protected FoodType    foodType;
    protected Entitymanager entitymanager;

    // ── Timers ─────────────────────────────────────────────────────────
    private double moveTimer   = 0;
    private double hambreTimer = 0;
    private static final double HAMBRE_INTERVAL = 7.0;

    // ── Combate ────────────────────────────────────────────────────────
    private double attackTimer = 0;
    private static final double ATTACK_INTERVAL = 0.6;

    // ── Reproducción ───────────────────────────────────────────────────
    protected double reproTimer = 0;
    public  static final double REPRO_COOLDOWN = 28800.0;
    private static final double HAMBRE_REPRO   = 0.50;
    private static final double ENERGIA_REPRO  = 0.20;

    public Animal(int tileX, int tileY, Entitymanager manager) {
        super(tileX, tileY, manager);
        this.entitymanager = manager;
        this.hunger = CAP_HAMBRE;
    }

    // ── Update principal ───────────────────────────────────────────────
    @Override
    public void update(World.World world) {}

    public void update(World.World world, double deltaTime) {
        if (!alive) return;

        // Edad por días del mundo
        actualizarEdad(world);

        // Muerte por vejez
        if (etapa == Etapa.VIEJO && edadDias >= DIAS_MUERTE) {
            alive = false;
            return;
        }

        // Hambre
        hambreTimer += deltaTime;
        if (hambreTimer >= HAMBRE_INTERVAL) {
            hambreTimer = 0;
            hunger = Math.max(0, hunger - 1);
            if (hunger == 0) takeDamage(2);
        }

        // Cooldown ataque y reproducción
        attackTimer += deltaTime;
        if (reproTimer > 0) reproTimer -= deltaTime;

        // Movimiento — cachorros son más lentos
        moveTimer += deltaTime;
        int speedEfectivo = (etapa == Etapa.CACHORRO) ? Math.max(1, speed / 2) : speed;
        double interval = 3.0 / Math.max(1, speedEfectivo);
        if (moveTimer >= interval) {
            moveTimer = 0;
            decidirAccion(world);
            energy = Math.max(0, energy - 1);
        }
    }

    // ── Edad ───────────────────────────────────────────────────────────
    private void actualizarEdad(World.World world) {
        // Necesitamos acceso al día actual del mundo
        // Lo hacemos a través del entitymanager o un contador propio
        // Usamos un acumulador interno que se incrementa cada update
        // Para sincronizar con los días del mundo usamos un truco:
        // cada vez que el día cambia sumamos 1 a edadDias
    }

    // Llama esto desde GameLoop o TimeDay cuando pasa un día
    public void cumplirDia() {
        edadDias++;
        actualizarEtapa();
    }

    private void actualizarEtapa() {
        if (edadDias >= DIAS_ADULTO && etapa == Etapa.CACHORRO) {
            etapa = Etapa.ADULTO;
            onCrecerAAdulto();
        } else if (edadDias >= DIAS_VIEJO && etapa == Etapa.ADULTO) {
            etapa = Etapa.VIEJO;
            onCrecerAViejo();
        }
    }

    // Hooks para que Lummon/Zyrox reaccionen al crecer
    protected void onCrecerAAdulto() {}
    protected void onCrecerAViejo()  {}

    // ── Reproducción ───────────────────────────────────────────────────
    public boolean puedeReproducirse() {
        return etapa == Etapa.ADULTO          // solo adultos
            && reproTimer <= 0
            && hunger  >= (int)(CAP_HAMBRE  * HAMBRE_REPRO)
            && energy  >= (int)(CAP_ENERGIA * ENERGIA_REPRO);
    }

    public void resetCooldownRepro() {
        reproTimer = REPRO_COOLDOWN;
        hunger = (int)(hunger * 0.5);
        energy = (int)(energy * 0.5);
    }

    // ── Herencia de stats ──────────────────────────────────────────────
    protected static int heredarStat(int statA, int statB, int min, int max) {
        Random rng = new Random();
        int promedio = (statA + statB) / 2;
        int mutacion = rng.nextInt(3) - 1; // -1, 0 o +1
        return Math.max(min, Math.min(max, promedio + mutacion));
    }

    // ── Movimiento aleatorio ───────────────────────────────────────────
    protected void moverAleatorio(World.World world) {
        int[][] dirs = {{0,-1},{0,1},{1,0},{-1,0}};
        Random rng = new Random();
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
        return (Math.abs(tileX - tx) + Math.abs(tileY - ty)) == 1;
    }

    // ── Atacar entidad en tile adyacente ──────────────────────────────
    protected boolean intentarAtacar(Entity objetivo) {
        // Cachorros no pueden atacar
        if (etapa == Etapa.CACHORRO) return false;
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
    protected boolean intentarComer(Blupys food) {
        if (food == null || !food.isAlive() || food.isDepleted()) return false;
        if (food.getTileX() != tileX || food.getTileY() != tileY) {
            if (!esAdyacente(food.getTileX(), food.getTileY())) return false;
        }
        int cosechado = food.harvest(10);
        if (cosechado > 0) {
            hunger = Math.min(CAP_HAMBRE, hunger + cosechado * 200);
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
            hunger = Math.min(CAP_HAMBRE, hunger + 200);
            heal(100);
            energy = Math.min(CAP_ENERGIA, energy + 100);
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

    protected int[] buscarTileLibre(World.World world) {
        int[][] dirs = {{0,-1},{0,1},{1,0},{-1,0},{1,1},{-1,-1},{1,-1},{-1,1}};
        for (int[] d : dirs) {
            int nx = tileX + d[0];
            int ny = tileY + d[1];
            if (esMovimientoValido(nx, ny, world)) {
                return new int[]{nx, ny};
            }
        }
        return null;
    }

    protected void decidirAccion(World.World world) {
        moverAleatorio(world);
    }

    // ── Getters ────────────────────────────────────────────────────────
    public Etapa  getEtapa()        { return etapa; }
    public int    getEdadDias()     { return edadDias; }
    public int    getEnergy()       { return energy; }
    public int    getHunger()       { return hunger; }
    public int    getSpeed()        { return speed; }
    public int    getAttack()       { return attack; }
    public int    getIntelligence() { return intelligence; }
    public Sex    getSex()          { return sex; }
    public FoodType getFoodType()   { return foodType; }
    public int    getCapacity()     { return capacity; }
    public int    getHabitat()      { return habitat; }
    public double getReproTimer() {return reproTimer;}
    

    // ── Setters ────────────────────────────────────────────────────────
    public void setEnergy(int e)        { this.energy = e; }
    public void setHunger(int h)        { this.hunger = h; }
    public void setSpeed(int s)         { this.speed = s; }
    public void setAttack(int a)        { this.attack = a; }
    public void setIntelligence(int i)  { this.intelligence = i; }
    public void setCapacity(int c)      { this.capacity = c; }
    public void setHabitat(int h)       { this.habitat = h; }
    public void setSex(Sex s)           { this.sex = s; }
    public void setFoodType(FoodType f) { this.foodType = f; }
    public void setEtapa(Etapa etapa)   { this.etapa = etapa; }
    public void setEdadDias(int dias)   { this.edadDias = dias; }
    public void setReproTimer(double reproTimer) {this.reproTimer = reproTimer;}
    
    
    
}