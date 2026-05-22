/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import World.Tile;

/**
 * Lummon: Carnívoro que caza en manada.
 * - Se agrupa hasta MAX_GRUPO (4) en la misma casilla.
 * - Cuando el hambre cae por debajo del 50%, persigue al Zyrox más cercano.
 * - Necesitan al menos MIN_CAZADORES adyacentes para atacar.
 * - Al matar un Zyrox genera un Corpse con porciones = cazadores (máx 4).
 * - En modo hambre busca cadáveres antes de cazar.
 */
public class Lummon extends Animal {

    private static final Logger logger = Logger.getLogger(Lummon.class.getName());

    // ── Constantes de comportamiento ───────────────────────────────────
    private static final int MAX_GRUPO      = 4;
    private static final int MIN_CAZADORES  = 2;
    private static final int HAMBRE_UMBRAL  = CAP_HAMBRE / 3;
    private static final double VIDA_HERIDO = 0.35; // < 35 % HP → sprite herido
    private static final int DURACION_SPRITE_COMER = 8;

    // ── Sprites (compartidos entre todas las instancias) ───────────────
    private static BufferedImage spriteIdle;
    private static BufferedImage spriteAttack;
    private static BufferedImage spriteHurt;
    private static BufferedImage spriteDead;
    private static BufferedImage spriteEat;
    private static BufferedImage spriteCachorro;
    private static boolean spritesLoaded = false;

    // ── Muerte con delay ───────────────────────────────────────────────
    private static final double DEATH_DISPLAY_TIME = 5.0; // segundos visible muerto
    private boolean dying      = false;
    private double  deathTimer = 0;

    // ── Flag de comer ──────────────────────────────────────────────────
    private boolean estaComiendo = false;
    private int timerComer = 0;
    
    //
    private final Random random = new Random();

    // ── Carga de sprites ───────────────────────────────────────────────
    private static void loadSprites() {
        if (spritesLoaded) return;
        try {
            spriteIdle   = ImageIO.read(Lummon.class.getResourceAsStream("/sprites/lummon_idle.png"));
            spriteAttack = ImageIO.read(Lummon.class.getResourceAsStream("/sprites/lummon_attack.png"));
            spriteHurt   = ImageIO.read(Lummon.class.getResourceAsStream("/sprites/lummon_hurt.png"));
            spriteDead   = ImageIO.read(Lummon.class.getResourceAsStream("/sprites/lummon_dead.png"));
            spriteEat    = ImageIO.read(Lummon.class.getResourceAsStream("/sprites/lummon_comer.png"));
            spriteCachorro = ImageIO.read(Lummon.class.getResourceAsStream("/sprites/lummon_cachorro.png"));

        } catch (IOException | IllegalArgumentException e) {
            logger.log(Level.WARNING, "No se pudieron cargar los sprites del Lummon", e);
        }
        spritesLoaded = true;
    }

    // ── Constructor ────────────────────────────────────────────────────
    public Lummon(int tileX, int tileY, Entitymanager manager) {
        super(tileX, tileY, manager);
        loadSprites();

        this.baseColor = Color.WHITE;
        name         = "Lummon";
        maxHealth    = 180 + random.nextInt(51);   
        energy       = 200 + random.nextInt(51);
        health       = maxHealth;
        capacity     = 1;
        sex          = random.nextBoolean() ? Sex.MALE : Sex.FEMALE;
        habitat      = Tile.GRASS;
        foodType     = FoodType.CARNIVORE;
        hunger       = CAP_HAMBRE;
        etapa    = Etapa.ADULTO;
        edadDias = DIAS_ADULTO;

        speed        = 2 + random.nextInt(3);
        attack       = 2 + random.nextInt(3);
        intelligence = 3 + random.nextInt(3);
    }

    // ── Selección de sprite según estado ──────────────────────────────
    private BufferedImage currentSprite() {
        if (dying && spriteDead != null)                                    return spriteDead;
        if (etapa == Etapa.CACHORRO)                                        return spriteCachorro; 
        if (health < (int)(maxHealth * VIDA_HERIDO) && spriteHurt != null)  return spriteHurt;
        if (timerComer > 0 && spriteEat != null) {
            timerComer--;                                                   return spriteEat;
        }
        if (hunger <= HAMBRE_UMBRAL && spriteAttack != null)                return spriteAttack;
        return spriteIdle;
    }

    // ── Lógica de acción ──────────────────────────────────────────────
    @Override
    protected void decidirAccion(World.World world) {

        // Resetear flag al inicio de cada tick
        estaComiendo = false;

        if (hunger <= HAMBRE_UMBRAL) {

            // Primero buscar cadáver cercano
            Corpse cadaver = buscarCadaverCercano();
            if (cadaver != null) {
                if (cadaver.getTileX() == tileX && cadaver.getTileY() == tileY
                        || esAdyacente(cadaver.getTileX(), cadaver.getTileY())) {
                    if (intentarComerCadaver(cadaver)) {
                        timerComer = DURACION_SPRITE_COMER;
                    }
                    estaComiendo = timerComer > 0;
                } else {
                    moverHacia(cadaver.getTileX(), cadaver.getTileY(), world);
                }
                return;
            }

            // Si no hay cadáver, cazar Zyrox
            Zyrox presa = buscarZyroxMasCercano();
            if (presa != null) {
                int cazadores = contarLummonsAdyacentesA(presa);
                if (esAdyacente(presa.getTileX(), presa.getTileY())) {
                    if (cazadores >= MIN_CAZADORES) {
                        intentarAtacar(presa);
                    }
                } else {
                    if (!intentarAgruparse(world)) {
                        moverHacia(presa.getTileX(), presa.getTileY(), world);
                    }
                }
            } else {
                if (!intentarAgruparse(world)) {
                    moverAleatorio(world);
                }
            }

        } else {
            if (!intentarAgruparse(world)) {
                moverAleatorio(world);
            }
        }
        intentarReproducirse(world);
    }

    // ── Búsquedas ─────────────────────────────────────────────────────
    private Corpse buscarCadaverCercano() {
        Corpse mejor = null;
        int mejorDist = Integer.MAX_VALUE;
        for (Entity e : entitymanager.getResources()) {
            if (!(e instanceof Corpse)) continue;
            Corpse c = (Corpse) e;
            if (!c.isAlive() || c.isDepleted()) continue;
            int dist = Math.abs(c.getTileX() - tileX) + Math.abs(c.getTileY() - tileY);
            if (dist < mejorDist) { mejorDist = dist; mejor = c; }
        }
        return mejor;
    }

    private Zyrox buscarZyroxMasCercano() {
        Zyrox mejor = null;
        int mejorDist = Integer.MAX_VALUE;
        for (Animal a : entitymanager.getAnimals()) {
            if (!(a instanceof Zyrox)) continue;
            Zyrox z = (Zyrox) a;
            if (!z.isAlive()) continue;
            int dist = Math.abs(z.getTileX() - tileX) + Math.abs(z.getTileY() - tileY);
            if (dist < mejorDist) { mejorDist = dist; mejor = z; }
        }
        return mejor;
    }

    private int contarLummonsAdyacentesA(Zyrox presa) {
        int count = 0;
        for (Animal a : entitymanager.getAnimals()) {
            if (!(a instanceof Lummon)) continue;
            Lummon l = (Lummon) a;
            if (!l.isAlive()) continue;
            if (l.esAdyacente(presa.getTileX(), presa.getTileY())) count++;
        }
        return count;
    }

    private boolean intentarAgruparse(World.World world) {
        int enMiCasilla = contarLummonsEnCasilla(tileX, tileY);
        if (enMiCasilla >= MAX_GRUPO) return false;

        int mejorX = -1, mejorY = -1, mejorCount = 0;
        int[][] dirs = {{0,-1},{0,1},{1,0},{-1,0}};
        for (int[] d : dirs) {
            int nx = tileX + d[0], ny = tileY + d[1];
            if (!esMovimientoValido(nx, ny, world)) continue;
            int lums = contarLummonsEnCasilla(nx, ny);
            if (lums > mejorCount && lums < MAX_GRUPO) {
                mejorCount = lums; mejorX = nx; mejorY = ny;
            }
        }
        if (mejorX != -1) {
            int s = entitymanager.getSlotLibre(mejorX, mejorY, capacity);
            if (s != -1) { tileX = mejorX; tileY = mejorY; slot = s; return true; }
        }
        return false;
    }

    private int contarLummonsEnCasilla(int cx, int cy) {
        int count = 0;
        for (Animal a : entitymanager.getAnimals()) {
            if (a instanceof Lummon && a.isAlive()
                    && a.getTileX() == cx && a.getTileY() == cy) count++;
        }
        return count;
    }

    // ── Override takeDamage para interceptar la muerte ─────────────────
    @Override
    public void takeDamage(int amount) {
        if (dying) return; // ya está en fase de muerte, ignorar daño
        health = Math.max(0, health - amount);
        if (health == 0 && !dying) {
            dying = true;
            // NO ponemos alive = false todavía; lo hace el timer
        }
    }

    // ── Updates ───────────────────────────────────────────────────────
    @Override public void update(World.World world) {}

    @Override
    public void update(World.World world, double deltaTime) {
        if (dying) {
            deathTimer += deltaTime;
            if (deathTimer >= DEATH_DISPLAY_TIME) {
                alive = false; // ahora sí desaparece
            }
            return; // no ejecutar lógica normal mientras muere
        }
        super.update(world, deltaTime);
    }
    
    // ── Al crecer ──────────────────────────────────────────────────────
    @Override
    protected void onCrecerAAdulto() {
        // Recupera stats completos al llegar a adulto
        maxHealth = (int)(maxHealth * 1.2);
        health    = maxHealth;
        System.out.println(getCustomName() + " creció a ADULTO");
    }

    @Override
    protected void onCrecerAViejo() {
        // Stats reducidos en vejez
        speed  = Math.max(1, speed  - 1);
        attack = Math.max(1, attack - 1);
        System.out.println(getCustomName() + " llegó a VIEJO");
    }

    // ── Reproducción ──────────────────────────────────────────────────
    private void intentarReproducirse(World.World world) {
        if (!puedeReproducirse()) return;

        for (Animal a : entitymanager.getAnimals()) {
            if (!(a instanceof Lummon)) continue;
            Lummon otro = (Lummon) a;

            if (otro == this)                                   continue;
            if (!otro.isAlive())                                continue;
            if (otro.sex == this.sex)                           continue;
            if (otro.getEtapa() != Etapa.ADULTO)                continue;
            if (!esAdyacente(otro.getTileX(), otro.getTileY())) continue;
            if (!otro.puedeReproducirse())                      continue;

            int[] hijoPos = buscarTileLibre(world);
            if (hijoPos == null) return;

            // Crear cachorro con stats heredados
            Lummon hijo = new Lummon(hijoPos[0], hijoPos[1], entitymanager);
            hijo.etapa        = Etapa.CACHORRO;   // nace cachorro
            hijo.edadDias     = 0;
            hijo.speed        = heredarStat(this.speed,        otro.speed,        1, CAP_VELOCIDAD);
            hijo.attack       = heredarStat(this.attack,       otro.attack,       1, CAP_ATAQUE);
            hijo.intelligence = heredarStat(this.intelligence, otro.intelligence, 1, CAP_INTELIGENCIA);
            hijo.maxHealth    = heredarStat(this.maxHealth,    otro.maxHealth,    5, CAP_VIDA);
            hijo.health       = hijo.maxHealth / 2; // nace con mitad de vida

            entitymanager.addAnimal(hijo);

            this.resetCooldownRepro();
            otro.resetCooldownRepro();

            System.out.println("¡Nació un Lummon cachorro en "
                + hijoPos[0] + "," + hijoPos[1] + "!");
            return;
        }
    }

    // ── Dibujo con sprite ─────────────────────────────────────────────
    // El tile se divide en una cuadrícula 2x2 + 1 celda extra (slot 0-4).
    // Cada Lummon ocupa 1 slot, así hasta 4 caben sin superponerse.
    //
    //  slot 0 | slot 1
    //  -------+-------
    //  slot 2 | slot 3
    //  slot 4 (centro inferior)
    //
    private static final int[] SLOT_OX = {0, 1, 0, 1, 0}; // offset col (0=izq, 1=der)
    private static final int[] SLOT_OY = {0, 0, 1, 1, 1}; // offset fila (0=arr, 1=abj)

    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {

        BufferedImage sprite = currentSprite();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        // ── Estado muerto: sprite en su slot con fade ─────────────────
        if (dying) {
            if (spriteDead == null) return;
            int cellSize = tileSize / 2;
            int safeSlot = (slot >= 0 && slot < 5) ? slot : 0;
            int cellX = cameraX + tileX * tileSize + SLOT_OX[safeSlot] * cellSize;
            int cellY = cameraY + tileY * tileSize + SLOT_OY[safeSlot] * cellSize;

            double ratio = (double) spriteDead.getWidth() / spriteDead.getHeight();
            int drawH = (int)(cellSize * 0.70);
            int drawW = (int)(drawH * ratio);
            int dx = cellX + (cellSize - drawW) / 2;
            int dy = cellY + (cellSize - drawH) / 2;

            // Fade out en el último 40% del tiempo de muerte
            float fadeStart = (float)(DEATH_DISPLAY_TIME * 0.6);
            float alpha = deathTimer < fadeStart ? 1.0f
                    : 1.0f - (float)((deathTimer - fadeStart) / (DEATH_DISPLAY_TIME - fadeStart));
            alpha = Math.max(0f, Math.min(1f, alpha));

            g2.setComposite(java.awt.AlphaComposite.getInstance(
                    java.awt.AlphaComposite.SRC_OVER, alpha));
            g2.drawImage(spriteDead, dx, dy, drawW, drawH, null);
            g2.setComposite(java.awt.AlphaComposite.getInstance(
                    java.awt.AlphaComposite.SRC_OVER, 1.0f));
            return;
        }

        // ── Estado comiendo: sprite centrado en el tile completo ──────
        if (estaComiendo && spriteEat != null) {
            double ratio = (double) spriteEat.getWidth() / spriteEat.getHeight();
            int drawH = (int)(tileSize * 0.75);
            int drawW = (int)(drawH * ratio);
            int dx = cameraX + tileX * tileSize + (tileSize - drawW) / 2;
            int dy = cameraY + tileY * tileSize + (tileSize - drawH) / 2;
            g2.drawImage(spriteEat, dx, dy, drawW, drawH, null);
            return;
        }

        // ── Estado vivo: sprite en subcelda según slot ────────────────
        int cellSize = tileSize / 2;
        int safeSlot = (slot >= 0 && slot < 5) ? slot : 0;
        int cellX = cameraX + tileX * tileSize + SLOT_OX[safeSlot] * cellSize;
        int cellY = cameraY + tileY * tileSize + SLOT_OY[safeSlot] * cellSize;

        int drawW, drawH;
        if (sprite != null) {
            double ratio = (double) sprite.getWidth() / sprite.getHeight();
            float escala;
        if (etapa == Etapa.CACHORRO) {
            escala = 0.42f;
        } else if (estaComiendo) {
            escala = 0.30f;
        } else if (hunger <= HAMBRE_UMBRAL) {
            escala = 0.55f; // ataque mismo tamaño que idle
        } else if (health < (int)(maxHealth * VIDA_HERIDO)) {
            escala = 0.50f;
        } else {
            escala = 0.60f;
        }
        drawH = (int)(tileSize * escala);
            drawW = (int)(drawH * ratio);
        } else {
            drawW = drawH = (int)(cellSize * 0.75);
        }

        int dx = cellX + (cellSize - drawW) / 2;
        int dy = cellY + (cellSize - drawH) / 2;

        if (sprite != null) {
            if (customColor != null) {
                g2.drawImage(sprite, dx, dy, drawW, drawH, null);
                g2.setColor(new Color(customColor.getRed(),
                        customColor.getGreen(), customColor.getBlue(), 60));
                g2.fillRect(dx, dy, drawW, drawH);
            } else {
                g2.drawImage(sprite, dx, dy, drawW, drawH, null);
            }
        } else {
            g2.setColor(hunger <= HAMBRE_UMBRAL ? new Color(255, 80, 80) : Color.WHITE);
            g2.fillOval(dx, dy, drawW, drawH);
            g2.setColor(Color.BLACK);
            g2.drawOval(dx, dy, drawW, drawH);
        }
    }
}