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
import java.util.Random;
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

    private static final int MAX_GRUPO     = 4;
    private static final int MIN_CAZADORES = 2;
    private static final int HAMBRE_UMBRAL = CAP_HAMBRE / 2;

    private final Random random = new Random();

    public Lummon(int tileX, int tileY, Entitymanager manager) {
        super(tileX, tileY, manager);
        this.baseColor = Color.WHITE;
        name         = "Lummon";
        maxHealth    = 15 + random.nextInt(11);
        health       = maxHealth;
        capacity     = 1;
        energy       = 30 + random.nextInt(21);
        sex          = random.nextBoolean() ? Sex.MALE : Sex.FEMALE;
        habitat      = Tile.GRASS;
        foodType     = FoodType.CARNIVORE;
        hunger       = CAP_HAMBRE;

        speed        = 2 + random.nextInt(3);
        attack       = 2 + random.nextInt(3);
        intelligence = 3 + random.nextInt(3);
    }

    @Override
    protected void decidirAccion(World.World world) {

        if (hunger <= HAMBRE_UMBRAL) {

            // Primero buscar cadáver cercano
            Corpse cadaver = buscarCadaverCercano();
            if (cadaver != null) {
                if (cadaver.getTileX() == tileX && cadaver.getTileY() == tileY
                        || esAdyacente(cadaver.getTileX(), cadaver.getTileY())) {
                    intentarComerCadaver(cadaver);
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
                        // El cadáver lo genera Entitymanager.eliminarMuertas() automáticamente
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
    }

    private Corpse buscarCadaverCercano() {
        Corpse mejor = null;
        int mejorDist = Integer.MAX_VALUE;
        for (Entity e : entitymanager.getResources()) {
            if (!(e instanceof Corpse c) || !c.isAlive() || c.isDepleted()) continue;
            int dist = Math.abs(c.getTileX() - tileX) + Math.abs(c.getTileY() - tileY);
            if (dist < mejorDist) { mejorDist = dist; mejor = c; }
        }
        return mejor;
    }

    private Zyrox buscarZyroxMasCercano() {
        Zyrox mejor = null;
        int mejorDist = Integer.MAX_VALUE;
        for (Animal a : entitymanager.getAnimals()) {
            if (!(a instanceof Zyrox z) || !z.isAlive()) continue;
            int dist = Math.abs(z.getTileX() - tileX) + Math.abs(z.getTileY() - tileY);
            if (dist < mejorDist) { mejorDist = dist; mejor = z; }
        }
        return mejor;
    }

    private int contarLummonsAdyacentesA(Zyrox presa) {
        int count = 0;
        for (Animal a : entitymanager.getAnimals()) {
            if (!(a instanceof Lummon l) || !l.isAlive()) continue;
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
            if (a instanceof Lummon && a.isAlive() && a.getTileX() == cx && a.getTileY() == cy) count++;
        }
        return count;
    }

    @Override public void update(World.World world) {}

    @Override
    public void update(World.World world, double deltaTime) {
        super.update(world, deltaTime);
    }

    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        int[] slotOffsetX = {0, 1, 0, 1, 0};
        int[] slotOffsetY = {0, 0, 1, 1, 2};
        int half = tileSize / 3;
        Color renderColor = getRenderColor();
        int px = cameraX + tileX * tileSize + slotOffsetX[slot] * half;
        int py = cameraY + tileY * tileSize + slotOffsetY[slot] * half;

        g.setColor(hunger <= HAMBRE_UMBRAL ? new Color(255, 80, 80) : renderColor);
        g.fillOval(px, py, half, half);
        g.setColor(Color.BLACK);
        g.drawOval(px, py, half, half);

        int eyeSize = Math.max(2, half / 6);
        g.setColor(Color.RED);
        g.fillOval(px + half / 4, py + half / 3, eyeSize, eyeSize);
        g.fillOval(px + half / 2, py + half / 3, eyeSize, eyeSize);
    }
}