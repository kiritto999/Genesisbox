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
 * - Se agrupa hasta MAX_GRUPO (2) en la misma casilla.
 * - Come bayas maduras cuando tiene hambre.
 * - Si fue golpeado este tick, contraataca al Lummon adyacente.
 * - Si HP < 20%, huye sin importar nada más.
 */
public class Zyrox extends Animal {

    private static final int    MAX_GRUPO     = 2;
    private static final int    HAMBRE_UMBRAL = CAP_HAMBRE / 2;  // 50%
    private static final int    RANGO_COMIDA  = 8;
    private static final double VIDA_HUIDA    = 0.20;             // 20% HP

    private final Random random = new Random();
    private int hpUltimoTick = -1; // para detectar si fue golpeado

    public Zyrox(int tileX, int tileY, Entitymanager manager) {
        super(tileX, tileY, manager);

        name         = "Zyrox";
        maxHealth    = 40 + random.nextInt(21); // 40-60
        health       = maxHealth;
        capacity     = 3;
        energy       = 50 + random.nextInt(21); // 50-70
        sex          = random.nextBoolean() ? Sex.MALE : Sex.FEMALE;
        habitat      = Tile.GRASS;
        foodType     = FoodType.HERBIVORE;

        hunger = CAP_HAMBRE;
        thirst = CAP_SED;

        speed        = 3 + random.nextInt(3);  // 3-5
        attack       = 3 + random.nextInt(3);  // 3-5
        intelligence = 1 + random.nextInt(3);  // 1-3

        hpUltimoTick = maxHealth;
    }

    // ── IA principal ────────────────────────────────────────────────────
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

        // Prioridad 2: Contraatacar si fue golpeado este tick
        boolean fueGolpeado = (hpUltimoTick > health);
        if (fueGolpeado) {
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

        // Prioridad 4: Agruparse o moverse
        if (!intentarAgruparse(world)) {
            moverAleatorio(world);
        }

        hpUltimoTick = health;
    }

    // ── HP menor al 20% ────────────────────────────────────────────────
    private boolean estaEnPeligroDeMuerte() {
        return health < (int)(maxHealth * VIDA_HUIDA);
    }

    // ── Buscar Lummon vivo adyacente ───────────────────────────────────
    private Lummon buscarLummonAdyacente() {
        for (Animal a : entitymanager.getAnimals()) {
            if (a instanceof Lummon l && l.isAlive() && esAdyacente(l.getTileX(), l.getTileY())) {
                return l;
            }
        }
        return null;
    }

    // ── Buscar Food madura más cercana ─────────────────────────────────
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

    // ── Huir en dirección opuesta ──────────────────────────────────────
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

    // ── Agruparse con otro Zyrox ───────────────────────────────────────
    private boolean intentarAgruparse(World.World world) {
        if (contarZyroxEnCasilla(tileX, tileY) >= MAX_GRUPO) return false;
        int[][] dirs = {{0,-1},{0,1},{1,0},{-1,0}};
        int mejorX = -1, mejorY = -1, mejorCount = 0;
        for (int[] d : dirs) {
            int nx = tileX + d[0], ny = tileY + d[1];
            if (!esMovimientoValido(nx, ny, world)) continue;
            int z = contarZyroxEnCasilla(nx, ny);
            if (z > mejorCount && z < MAX_GRUPO) { mejorCount = z; mejorX = nx; mejorY = ny; }
        }
        if (mejorX != -1) {
            int s = entitymanager.getSlotLibre(mejorX, mejorY, capacity);
            if (s != -1) { tileX = mejorX; tileY = mejorY; slot = s; return true; }
        }
        return false;
    }

    private int contarZyroxEnCasilla(int cx, int cy) {
        int count = 0;
        for (Animal a : entitymanager.getAnimals()) {
            if (a instanceof Zyrox && a.isAlive() && a.getTileX() == cx && a.getTileY() == cy) count++;
        }
        return count;
    }

    // ── Heredadas ──────────────────────────────────────────────────────
    @Override public void update(World.World world) {}

    @Override
    public void update(World.World world, double deltaTime) {
        if (hpUltimoTick == -1) hpUltimoTick = health;
        super.update(world, deltaTime);
    }

    // ── Dibujo: más grande que el Lummon ──────────────────────────────
    // Lummon usa tileSize/3 (~33%) — Zyrox usa 70% del tile
    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        if (slot < 0 || slot >= 5) return;

        int size  = (int)(tileSize * 0.55);
        int px    = cameraX + tileX * tileSize + (tileSize - size) / 2;
        int py    = cameraY + tileY * tileSize + (tileSize - size) / 2;
        int bodyH = (int)(size * 0.60);

        // Color según estado
        Color color;
        if (estaEnPeligroDeMuerte()) {
            color = new Color(180, 30, 30);   // rojo → huyendo
        } else if (hunger <= HAMBRE_UMBRAL) {
            color = new Color(255, 210, 50);  // amarillo → hambriento
        } else {
            color = new Color(210, 120, 40);  // naranja base
        }

        // Orejas puntiagudas
        int earW = size / 7, earH = size / 5;
        g.setColor(color.darker());
        g.fillOval(px + size / 5,             py - earH / 2, earW, earH);
        g.fillOval(px + size / 2,             py - earH / 2, earW, earH);

        // Cuerpo
        g.setColor(color);
        g.fillOval(px, py, size, bodyH);

        // Hocico
        int snoutW = size / 4, snoutH = bodyH / 3;
        g.setColor(new Color(230, 160, 100));
        g.fillOval(px + size - snoutW, py + bodyH / 2 - snoutH / 2, snoutW, snoutH);

        // Borde cuerpo
        g.setColor(Color.BLACK);
        g.drawOval(px, py, size, bodyH);

        // Ojo
        g.setColor(Color.BLACK);
        g.fillOval(px + size * 3 / 4, py + bodyH / 3, Math.max(2, size / 10), Math.max(2, size / 10));
    }
}
