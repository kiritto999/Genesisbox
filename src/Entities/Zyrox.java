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
    private static boolean spritesLoaded = false;

    private final Random random = new Random();
    private int hpUltimoTick = -1;

    // Timer para el sprite de ataque (evita mostrarlo al recibir daño)
    private int timerAtaque = 0;
    private static final int DURACION_SPRITE_ATAQUE = 3;

    // Flag para el sprite de comer
    private boolean estaComiendo = false;

    // ── Carga de sprites ───────────────────────────────────────────────
    private static void loadSprites() {
        if (spritesLoaded) return;
        try {
            spriteIdle   = ImageIO.read(Zyrox.class.getResourceAsStream("/sprites/zyrox_idle.png"));
            spriteAttack = ImageIO.read(Zyrox.class.getResourceAsStream("/sprites/zyrox_attack.png"));
            spriteHurt   = ImageIO.read(Zyrox.class.getResourceAsStream("/sprites/zyrox_hurt.png"));
            spriteEat    = ImageIO.read(Zyrox.class.getResourceAsStream("/sprites/zyrox_eat.png"));
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
        maxHealth    = 40 + random.nextInt(21);
        health       = maxHealth;
        capacity     = 3;
        energy       = 50 + random.nextInt(21);
        sex          = random.nextBoolean() ? Sex.MALE : Sex.FEMALE;
        habitat      = Tile.GRASS;
        foodType     = FoodType.HERBIVORE;
        hunger       = CAP_HAMBRE;

        speed        = 3 + random.nextInt(3);
        attack       = 3 + random.nextInt(3);
        intelligence = 1 + random.nextInt(3);

        hpUltimoTick = maxHealth;
    }

    // ── Selección de sprite según estado ──────────────────────────────
    private BufferedImage currentSprite() {
        // Herido: prioridad máxima
        if (health < (int)(maxHealth * VIDA_HERIDO) && spriteHurt != null) {
            return spriteHurt;
        }
        // Atacando: solo si el timer está activo (lo activamos al golpear)
        if (timerAtaque > 0 && spriteAttack != null) {
            timerAtaque--;
            return spriteAttack;
        }
        // Comiendo
        if (estaComiendo && spriteEat != null) {
            return spriteEat;
        }
        return spriteIdle;
    }

    // ── Lógica de acción ──────────────────────────────────────────────
    @Override
    protected void decidirAccion(World.World world) {

        // Resetear flags al inicio de cada tick
        estaComiendo = false;

        // Prioridad 1: Huir si HP bajo
        if (estaEnPeligroDeMuerte()) {
            Lummon lummonCercano = buscarLummonAdyacente();
            if (lummonCercano != null) {
                huirDe(lummonCercano.getTileX(), lummonCercano.getTileY(), world);
            } else {
                moverAleatorio(world);
            }
            hpUltimoTick = health;
            return;
        }

        // Prioridad 2: Contraatacar si fue golpeado
        if (hpUltimoTick > health) {
            Lummon objetivo = buscarLummonAdyacente();
            if (objetivo != null) {
                if (intentarAtacar(objetivo)) {
                    timerAtaque = DURACION_SPRITE_ATAQUE; // activar sprite solo si golpeó
                }
                hpUltimoTick = health;
                return;
            }
        }

        // Prioridad 3: Comer si tiene hambre
        if (hunger <= HAMBRE_UMBRAL) {
            Blupys comida = buscarComidaMasCercana(RANGO_COMIDA);
            if (comida != null) {
                boolean enRango = (comida.getTileX() == tileX && comida.getTileY() == tileY)
                        || esAdyacente(comida.getTileX(), comida.getTileY());
                if (enRango && comida.getStage() == Blupys.Etapa.MADURA) {
                    estaComiendo = intentarComer(comida); // activar sprite eat
                } else if (!enRango) {
                    moverHacia(comida.getTileX(), comida.getTileY(), world);
                }
                hpUltimoTick = health;
                return;
            }
        }

        // Prioridad 4: Agruparse o moverse aleatorio
        if (!intentarAgruparse(world)) {
            moverAleatorio(world);
        }

        hpUltimoTick = health;
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
                        || f.getStage() == Blupys.Etapa.MADURA && mejor.getStage() != Blupys.Etapa.MADURA
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

    // ── Dibujo ────────────────────────────────────────────────────────
    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        if (slot < 0 || slot >= 5) return;

        BufferedImage sprite = currentSprite();

        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;

        int drawW, drawH;
        if (sprite != null) {
            double ratio = (double) sprite.getWidth() / sprite.getHeight();
            drawH = (int)(tileSize * 0.90);
            drawW = (int)(drawH * ratio);
        } else {
            drawW = (int)(tileSize * 0.55);
            drawH = (int)(tileSize * 0.55);
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

        drawHealthBar(g2, px, py, tileSize);
    }

    private void drawHealthBar(Graphics2D g2, int px, int py, int tileSize) {
        int barW = tileSize - 6;
        int barH = 3;
        int bx   = px + 3;
        int by   = py + tileSize - barH - 2;

        float ratio = (float) health / maxHealth;
        Color barColor = ratio > 0.5f ? new Color(80, 220, 80)
                       : ratio > 0.25f ? new Color(255, 200, 0)
                       : new Color(220, 50, 50);

        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(bx, by, barW, barH);
        g2.setColor(barColor);
        g2.fillRect(bx, by, (int)(barW * ratio), barH);
    }
}