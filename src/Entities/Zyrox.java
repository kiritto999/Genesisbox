/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import World.Tile;

/**
 * Zyrox: Zorro herbívoro, presa del Lummon.
 * - capacity = 3 → no caben 2 en el mismo tile.
 * - Se agrupa de a 2 en tiles adyacentes. Si ya tiene pareja, los demás
 *   buscan otro Zyrox sin pareja.
 * - Come bayas maduras cuando tiene hambre.
 * - Contraataca si fue golpeado.
 * - Huye si HP < 20%.
 */
public class Zyrox extends Animal {

    private static final int    MAX_GRUPO     = 2;
    private static final int    HAMBRE_UMBRAL = CAP_HAMBRE / 2;
    private static final int    RANGO_COMIDA  = 8;
    private static final double VIDA_HUIDA    = 0.20;

    private final Random random = new Random();
    private int hpUltimoTick = -1;

    public Zyrox(int tileX, int tileY, Entitymanager manager) {
        super(tileX, tileY, manager);
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

    @Override
    protected void decidirAccion(World.World world) {

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
                intentarAtacar(objetivo);
                hpUltimoTick = health;
                return;
            }
        }

        // Prioridad 3: Comer si tiene hambre
        if (hunger <= HAMBRE_UMBRAL) {
            Food comida = buscarComidaMasCercana(RANGO_COMIDA);
            if (comida != null) {
                if ((comida.getTileX() == tileX && comida.getTileY() == tileY)
                        || esAdyacente(comida.getTileX(), comida.getTileY())) {
                    intentarComer(comida);
                } else {
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
            if (!(a instanceof Zyrox z) || !z.isAlive() || z == this) continue;
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
            if (!(a instanceof Zyrox z) || z == this || !z.isAlive()) continue;
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
            if (a instanceof Lummon l && l.isAlive() && esAdyacente(l.getTileX(), l.getTileY())) {
                return l;
            }
        }
        return null;
    }

    private Food buscarComidaMasCercana(int radio) {
        Food mejor = null;
        int mejorDist = radio + 1;
        for (Entity e : entitymanager.getResources()) {
            if (!(e instanceof Food f) || !f.isAlive() || f.isDepleted()) continue;
            if (f.getStage() != Food.Etapa.MADURA) continue;
            int dist = Math.abs(f.getTileX() - tileX) + Math.abs(f.getTileY() - tileY);
            if (dist < mejorDist) { mejorDist = dist; mejor = f; }
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

    // ── Update ─────────────────────────────────────────────────────────

    @Override public void update(World.World world) {}

    @Override
    public void update(World.World world, double deltaTime) {
        if (hpUltimoTick == -1) hpUltimoTick = health;
        super.update(world, deltaTime);
    }

    // ── Dibujo ─────────────────────────────────────────────────────────

    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        if (slot < 0 || slot >= 5) return;

        int size  = (int)(tileSize * 0.55);
        int px    = cameraX + tileX * tileSize + (tileSize - size) / 2;
        int py    = cameraY + tileY * tileSize + (tileSize - size) / 2;
        int bodyH = (int)(size * 0.60);

        Color color;
        if (estaEnPeligroDeMuerte())      color = new Color(180, 30,  30);
        else if (hunger <= HAMBRE_UMBRAL) color = new Color(255, 210, 50);
        else                              color = baseColor;

        int earW = size / 7, earH = size / 5;
        g.setColor(color.darker());
        g.fillOval(px + size / 5, py - earH / 2, earW, earH);
        g.fillOval(px + size / 2, py - earH / 2, earW, earH);

        g.setColor(color);
        g.fillOval(px, py, size, bodyH);

        int snoutW = size / 4, snoutH = bodyH / 3;
        g.setColor(new Color(230, 160, 100));
        g.fillOval(px + size - snoutW, py + bodyH / 2 - snoutH / 2, snoutW, snoutH);

        g.setColor(Color.BLACK);
        g.drawOval(px, py, size, bodyH);
        g.fillOval(px + size * 3 / 4, py + bodyH / 3, Math.max(2, size / 10), Math.max(2, size / 10));
    }
}