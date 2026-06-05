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
 * Zyrox: Herbívoro presa del Lummon.
 * - capacity = 3 → no caben 2 en el mismo tile.
 * - Come bayas maduras cuando tiene hambre.
 * - Contraataca si fue golpeado.
 * - Huye si HP < 20%.
 *
 * Estados visuales:
 *   IDLE    → zyrox_idle.png
 *   ATTACK  → zyrox_attack.png  (solo cuando golpea, con timer)
 *   HURT    → zyrox_hurt.png    (HP < 35%)
 *   EAT     → zyrox_eat.png     (mientras come)
 */
public class Zyrox extends Animal {

    private static final Logger logger = Logger.getLogger(Zyrox.class.getName());

    // ── Constantes de comportamiento ───────────────────────────────────
    private static final int    MAX_GRUPO     = 2;
    private static final int    HAMBRE_UMBRAL = CAP_HAMBRE / 2;
    private static final int    RANGO_COMIDA  = 20;
    private static final double VIDA_HUIDA    = 0.20;
    private static final double VIDA_HERIDO   = 0.35;

    // ── Sprites ────────────────────────────────────────────────────────
    private static BufferedImage spriteIdle;
    private static BufferedImage spriteAttack;
    private static BufferedImage spriteHurt;
    private static BufferedImage spriteEat;
    private static BufferedImage spriteCachorro;
    private static boolean spritesLoaded = false;

    private final Random random = new Random();
    private int hpUltimoTick = -1;

    // Timer para el sprite de ataque (evita mostrarlo al recibir daño)
    private int timerAtaque = 0;
    private int timerComer = 0;
    private static final int DURACION_SPRITE_ATAQUE = 40;
    private static final int DURACION_SPRITE_COMER  = 60;

    // Flag para el sprite de comer
    private boolean estaComiendo = false;

    // ── Carga de sprites ───────────────────────────────────────────────
    private static void loadSprites() {
        if (spritesLoaded) return;
        try {
            spriteIdle     = ImageIO.read(Zyrox.class.getResourceAsStream("/sprites/zyrox_idle.png"));
            spriteAttack   = ImageIO.read(Zyrox.class.getResourceAsStream("/sprites/zyrox_attack.png"));
            spriteHurt     = ImageIO.read(Zyrox.class.getResourceAsStream("/sprites/zyrox_hurt.png"));
            spriteEat      = ImageIO.read(Zyrox.class.getResourceAsStream("/sprites/zyrox_eat.png"));
            spriteCachorro = ImageIO.read(Zyrox.class.getResourceAsStream("/sprites/zyrox_cachorro.png"));
        } catch (IOException | IllegalArgumentException e) {
            logger.log(Level.WARNING, "No se pudieron cargar los sprites del Zyrox", e);
        }
        spritesLoaded = true;
    }

    // ── Constructor ────────────────────────────────────────────────────
    public Zyrox(int tileX, int tileY, Entitymanager manager) {
        super(tileX, tileY, manager);
        loadSprites();

        this.baseColor = new Color(210, 120, 40);
        name         = "Zyrox";
        maxHealth    = 300 + random.nextInt(81);
        energy       = 200 + random.nextInt(51);
        health       = maxHealth;
        capacity     = 3;       
        sex          = random.nextBoolean() ? Sex.MALE : Sex.FEMALE;
        habitat      = Tile.GRASS;
        foodType     = FoodType.HERBIVORE;
        hunger       = CAP_HAMBRE;

        speed        = 4 + random.nextInt(3);
        attack       = 5 + random.nextInt(5);
        intelligence = 1 + random.nextInt(3);

        // Los spawneados manualmente empiezan como adultos
        etapa    = Etapa.ADULTO;
        edadDias = DIAS_ADULTO;

        hpUltimoTick = maxHealth;
    }

    // ── Selección de sprite según estado ──────────────────────────────
    private BufferedImage currentSprite() {
        // Herido: prioridad máxima
        if (health < (int)(maxHealth * VIDA_HERIDO) && spriteHurt != null) {
            return spriteHurt;
        }
        // Atacando: solo si el timer está activo
        if (timerAtaque > 0 && spriteAttack != null) {
            timerAtaque--;
            return spriteAttack;
        }
        // Comiendo
        if (timerComer > 0 && spriteEat != null) {
            timerComer--;
            return spriteEat;
        }
        // Cachorro: sprite especial si existe
        if (etapa == Etapa.CACHORRO && spriteCachorro != null) {
            return spriteCachorro;
        }
        return spriteIdle;
    }

    // ── Lógica de acción ──────────────────────────────────────────────
    @Override
    protected void decidirAccion(World.World world) {

        if (timerComer <= 0) estaComiendo = false;

        Lummon lummonCercano = buscarLummonAdyacente();

        // Prioridad 1: Huir si HP bajo Y hay Lummon cerca
        if (estaEnPeligroDeMuerte() && lummonCercano != null) {
            huirDe(lummonCercano.getTileX(), lummonCercano.getTileY(), world);
            hpUltimoTick = health;
            return;
        }

        // Prioridad 2: Comer si tiene hambre (incluso herido, si no hay Lummon cerca)
        if (hunger <= HAMBRE_UMBRAL) {
            Blupys comida = buscarComidaMasCercana(RANGO_COMIDA);
            if (comida != null) {
                boolean enRango = (comida.getTileX() == tileX && comida.getTileY() == tileY)
                        || esAdyacente(comida.getTileX(), comida.getTileY());
                if (enRango && comida.getStage() == Blupys.Etapa.MADURA) {
                    if (intentarComer(comida)) {
                        timerComer = DURACION_SPRITE_COMER;
                    }
                    estaComiendo = timerComer > 0;
                } else if (!enRango) {
                    moverHacia(comida.getTileX(), comida.getTileY(), world);
                }
                hpUltimoTick = health;
                return;
            }
        }

        // Prioridad 3: Huir si HP bajo aunque no haya Lummon visible
        if (estaEnPeligroDeMuerte()) {
            moverAleatorio(world);
            hpUltimoTick = health;
            return;
        }

        // Prioridad 4: Contraatacar si fue golpeado
        if (hpUltimoTick > health) {
            Lummon objetivo = buscarLummonAdyacente();
            if (objetivo != null) {
                if (intentarAtacar(objetivo)) {
                    timerAtaque = DURACION_SPRITE_ATAQUE;
                }
                hpUltimoTick = health;
                return;
            }
        }

        // Prioridad 5: Agruparse o moverse aleatorio
        if (!intentarAgruparse(world)) {
            moverAleatorio(world);
        }

        hpUltimoTick = health;
        intentarReproducirse(world);
    }
    // ── Agrupación ─────────────────────────────────────────────────────
    /**
     * Cuenta cuántos Zyrox están en tiles adyacentes a (cx, cy).
     */
    private int contarZyroxAdyacentesA(int cx, int cy) {
        int count = 0;
        for (Animal a : entitymanager.getAnimals()) {
            if (!(a instanceof Zyrox)) continue;
            Zyrox z = (Zyrox) a;
            if (!z.isAlive() || z == this) continue;
            int dist = Math.abs(z.getTileX() - cx) + Math.abs(z.getTileY() - cy);
            if (dist == 1) count++;
        }
        return count;
    }

    /**
     * ¿Tengo ya una pareja adyacente? (al menos 1 Zyrox adyacente a mí)
     */
    private boolean tengoParejaAdyacente() {
        return contarZyroxAdyacentesA(tileX, tileY) >= 1;
    }

    /**
     * Intenta moverse hacia un Zyrox sin pareja.
     * Si ya tengo pareja, me quedo (sigo moviéndome con ella aleatoriamente).
     * Si no tengo pareja, busco un Zyrox que tampoco tenga y me acerco.
     */
    private boolean intentarAgruparse(World.World world) {

        // Si ya tengo pareja, moverme aleatorio junto a ella (no buscar más)
        if (tengoParejaAdyacente()) {
            moverAleatorio(world);
            return true;
        }

        // No tengo pareja: buscar el Zyrox más cercano que tampoco tenga pareja
        Zyrox objetivo = null;
        int mejorDist = Integer.MAX_VALUE;
        for (Animal a : entitymanager.getAnimals()) {
            if (!(a instanceof Zyrox)) continue;
            Zyrox z = (Zyrox) a;
            if (z == this || !z.isAlive()) continue;
            // Solo me acerco a Zyrox sin pareja
            if (z.tengoParejaAdyacente()) continue;
            int dist = Math.abs(z.getTileX() - tileX) + Math.abs(z.getTileY() - tileY);
            if (dist > 0 && dist < mejorDist) {
                mejorDist = dist;
                objetivo  = z;
            }
        }

        if (objetivo != null) {
            moverHacia(objetivo.getTileX(), objetivo.getTileY(), world);
            return true;
        }

        return false; // no hay nadie con quien agruparse → moverAleatorio
    }

    // ── Utilidades ─────────────────────────────────────────────────────
    private boolean estaEnPeligroDeMuerte() {
        return health < (int)(maxHealth * VIDA_HUIDA);
    }

    private Lummon buscarLummonAdyacente() {
        for (Animal a : entitymanager.getAnimals()) {
            if (a instanceof Lummon) {
                Lummon l = (Lummon) a;
                if (l.isAlive() && esAdyacente(l.getTileX(), l.getTileY())) return l;
            }
        }
        return null;
    }

    private Blupys buscarComidaMasCercana(int radio) {
        Blupys mejor = null;
        int mejorDist = Integer.MAX_VALUE;

        for (Entity e : entitymanager.getResources()) {
            if (!(e instanceof Blupys)) continue;
            Blupys f = (Blupys) e;
            if (!f.isAlive() || f.isDepleted()) continue;
            int dist = Math.abs(f.getTileX() - tileX) + Math.abs(f.getTileY() - tileY);
            if (dist <= radio && dist < mejorDist) {
                if (mejor == null
                        || (f.getStage() == Blupys.Etapa.MADURA && mejor.getStage() != Blupys.Etapa.MADURA)
                        || (f.getStage() == mejor.getStage() && dist < mejorDist)) {
                    mejorDist = dist;
                    mejor = f;
                }
            }
        }
        return mejor;
    }

    private void huirDe(int peligroX, int peligroY, World.World world) {
        int dx = Integer.compare(tileX, peligroX);
        int dy = Integer.compare(tileY, peligroY);
        int[][] intentos = { {dx, dy}, {dx, 0}, {0, dy}, {-dx, 0}, {0, -dy} };
        for (int[] d : intentos) {
            if (d[0] == 0 && d[1] == 0) continue;
            int nx = tileX + d[0], ny = tileY + d[1];
            if (esMovimientoValido(nx, ny, world)) {
                int s = entitymanager.getSlotLibre(nx, ny, capacity);
                if (s != -1) { tileX = nx; tileY = ny; slot = s; return; }
            }
        }
        moverAleatorio(world);
    }

    // ── Updates ───────────────────────────────────────────────────────
    @Override public void update(World.World world) {}

    @Override
    public void update(World.World world, double deltaTime) {
        if (hpUltimoTick == -1) hpUltimoTick = health;
        super.update(world, deltaTime);
    }

    // ── Al crecer ──────────────────────────────────────────────────────
    @Override
    protected void onCrecerAAdulto() {
        maxHealth = (int)(maxHealth * 1.2);
        health    = maxHealth;
        System.out.println(getCustomName() + " Zyrox creció a ADULTO");
    }

    @Override
    protected void onCrecerAViejo() {
        speed  = Math.max(1, speed  - 1);
        attack = Math.max(1, attack - 1);
        System.out.println(getCustomName() + " Zyrox llegó a VIEJO");
    }

    // ── Reproducción ──────────────────────────────────────────────────
    private void intentarReproducirse(World.World world) {
        if (!puedeReproducirse()) return;

        for (Animal a : entitymanager.getAnimals()) {
            if (!(a instanceof Zyrox)) continue;
            Zyrox otro = (Zyrox) a;

            if (otro == this)                                  continue;
            if (!otro.isAlive())                               continue;
            if (otro.sex == this.sex)                          continue;
            if (otro.getEtapa() != Etapa.ADULTO)               continue;
            if (!esAdyacente(otro.getTileX(), otro.getTileY())) continue;
            if (!otro.puedeReproducirse())                     continue;

            int[] hijoPos = buscarTileLibre(world);
            if (hijoPos == null) return;

            Zyrox hijo = new Zyrox(hijoPos[0], hijoPos[1], entitymanager);
            // El hijo nace cachorro, sobreescribir lo que puso el constructor
            hijo.etapa        = Etapa.CACHORRO;
            hijo.edadDias     = 0;
            hijo.speed        = heredarStat(this.speed,        otro.speed,        1, CAP_VELOCIDAD);
            hijo.attack       = heredarStat(this.attack,       otro.attack,       1, CAP_ATAQUE);
            hijo.intelligence = heredarStat(this.intelligence, otro.intelligence, 1, CAP_INTELIGENCIA);
            hijo.maxHealth    = heredarStat(this.maxHealth,    otro.maxHealth,    5, CAP_VIDA);
            hijo.health       = hijo.maxHealth / 2;

            entitymanager.addAnimal(hijo);

            this.resetCooldownRepro();
            otro.resetCooldownRepro();

            System.out.println("Nació un Zyrox cachorro en "
                + hijoPos[0] + "," + hijoPos[1] + "!");
            return;
        }
    }

    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        if (slot < 0 || slot >= 5) return;

        BufferedImage sprite = currentSprite();

        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;

        // Escala por estado
        float escala;
        if (etapa == Etapa.CACHORRO) {
            escala = 0.55f;
        } else if (health < (int)(maxHealth * VIDA_HERIDO)) {
            escala = 0.70f; // herido más pequeño
        } else if (estaComiendo) {
            escala = 0.85f;
        } else {
            escala = 0.90f;
        }

        int drawW, drawH;
        if (sprite != null) {
            double ratio = (double) sprite.getWidth() / sprite.getHeight();
            drawH = (int)(tileSize * escala);
            drawW = (int)(drawH * ratio);
        } else {
            drawW = (int)(tileSize * escala);
            drawH = (int)(tileSize * escala);
        }

        int dx = px + (tileSize - drawW) / 2;
        int dy = py + (tileSize - drawH) / 2;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        if (sprite != null) {
            g2.drawImage(sprite, dx, dy, drawW, drawH, null);
            if (customColor != null) {
                g2.setColor(new Color(
                    customColor.getRed(),
                    customColor.getGreen(),
                    customColor.getBlue(), 60));
                g2.fillRect(dx, dy, drawW, drawH);
            }
        } else {
            Color color;
            if (estaEnPeligroDeMuerte())      color = new Color(180, 30, 30);
            else if (hunger <= HAMBRE_UMBRAL) color = new Color(255, 210, 50);
            else                              color = baseColor;
            g2.setColor(color);
            g2.fillOval(dx, dy, drawW, drawH);
            g2.setColor(Color.BLACK);
            g2.drawOval(dx, dy, drawW, drawH);
        }

    }
}