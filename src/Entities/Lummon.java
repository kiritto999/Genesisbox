/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import World.Tile;

/**
 * Lummon: Carnívoro que caza en manada.
 * - Se agrupa hasta MAX_GRUPO (4) en la misma casilla.
 * - Cuando el hambre cae por debajo del 50%, persigue al Zyrox más cercano.
 * - Para atacar se posicionan en casillas adyacentes al Zyrox.
 * - Necesitan al menos MIN_CAZADORES en casillas adyacentes para atacar.
 */
public class Lummon extends Animal {

    private static final int MAX_GRUPO     = 4;  // máx Lummons en una casilla
    private static final int MIN_CAZADORES = 2;  // mínimo para iniciar el ataque
    private static final int HAMBRE_UMBRAL = CAP_HAMBRE / 2; // 50%

    private final Random random = new Random();

    public Lummon(int tileX, int tileY, Entitymanager manager) {
        super(tileX, tileY, manager);

        name         = "Lummon";
        maxHealth    = 15 + random.nextInt(11); // 15-25
        health       = maxHealth;
        capacity     = 1;
        energy       = 30 + random.nextInt(21); // 30-50
        sex          = random.nextBoolean() ? Sex.MALE : Sex.FEMALE;
        habitat      = Tile.GRASS;
        foodType     = FoodType.CARNIVORE;

        hunger = CAP_HAMBRE;
        thirst = CAP_SED;

        speed        = 2 + random.nextInt(3);  // 2-4
        attack       = 2 + random.nextInt(3);  // 2-4
        intelligence = 3 + random.nextInt(3);  // 3-5
    }

    // ── IA principal ────────────────────────────────────────────────────
    @Override
    protected void decidirAccion(World.World world) {

        if (hunger <= HAMBRE_UMBRAL) {
            // ── MODO CAZA ──────────────────────────────────────────────
            Zyrox presa = buscarZyroxMasCercano();

            if (presa != null) {
                // Contar cuántos Lummons están adyacentes al Zyrox
                int cazadoresAdyacentes = contarLummonsAdyacentesA(presa);

                if (esAdyacente(presa.getTileX(), presa.getTileY())) {
                    // Ya estoy adyacente: atacar si hay suficientes cazadores
                    if (cazadoresAdyacentes >= MIN_CAZADORES) {
                        boolean atacado = intentarAtacar(presa);
                        if (atacado && !presa.isAlive()) {
                            // Presa muerta: comer y recuperar hambre/vida
                            comerPresa(presa);
                        }
                    }
                    // Si no hay suficientes, esperar (quedarse quieto)
                } else {
                    // Acercarme: primero intentar agruparme con otros Lummons
                    if (!intentarAgruparse(world)) {
                        // Si ya somos suficientes o no hay grupo, ir directo
                        moverHacia(presa.getTileX(), presa.getTileY(), world);
                    }
                }
            } else {
                // Sin presa visible: agruparse o moverse aleatorio
                if (!intentarAgruparse(world)) {
                    moverAleatorio(world);
                }
            }

        } else {
            // ── MODO GRUPO (hambre OK) ─────────────────────────────────
            if (!intentarAgruparse(world)) {
                moverAleatorio(world);
            }
        }
    }

    // ── Buscar el Zyrox vivo más cercano ───────────────────────────────
    private Zyrox buscarZyroxMasCercano() {
        Zyrox mejor = null;
        int mejorDist = Integer.MAX_VALUE;

        for (Animal a : entitymanager.getAnimals()) {
            if (!(a instanceof Zyrox z) || !z.isAlive()) continue;
            int dist = Math.abs(z.getTileX() - tileX) + Math.abs(z.getTileY() - tileY);
            if (dist < mejorDist) {
                mejorDist = dist;
                mejor = z;
            }
        }
        return mejor;
    }

    // ── Cuántos Lummons están en casillas adyacentes al Zyrox ──────────
    private int contarLummonsAdyacentesA(Zyrox presa) {
        int count = 0;
        for (Animal a : entitymanager.getAnimals()) {
            if (!(a instanceof Lummon l) || !l.isAlive()) continue;
            if (l.esAdyacente(presa.getTileX(), presa.getTileY())) count++;
        }
        return count;
    }

    // ── Intentar moverse a una casilla con otros Lummons (reagruparse) ──
    /**
     * Busca una casilla adyacente que tenga Lummons y no esté llena (< MAX_GRUPO).
     * @return true si se movió a agruparse
     */
    private boolean intentarAgruparse(World.World world) {
        // Contar Lummons en casilla propia
        int enMiCasilla = contarLummonsEnCasilla(tileX, tileY);
        if (enMiCasilla >= MAX_GRUPO) return false; // ya somos suficientes aquí

        // Buscar casilla adyacente con Lummons y espacio
        int mejorCasX = -1, mejorCasY = -1, mejorCount = 0;

        int[][] dirs = {{0,-1},{0,1},{1,0},{-1,0}};
        for (int[] d : dirs) {
            int nx = tileX + d[0];
            int ny = tileY + d[1];
            if (!esMovimientoValido(nx, ny, world)) continue;

            int lumsAhi = contarLummonsEnCasilla(nx, ny);
            if (lumsAhi > mejorCount && lumsAhi < MAX_GRUPO) {
                mejorCount = lumsAhi;
                mejorCasX = nx;
                mejorCasY = ny;
            }
        }

        if (mejorCasX != -1) {
            int s = entitymanager.getSlotLibre(mejorCasX, mejorCasY, capacity);
            if (s != -1) {
                tileX = mejorCasX; tileY = mejorCasY; slot = s;
                return true;
            }
        }
        return false;
    }

    // ── Contar Lummons vivos en una casilla ─────────────────────────────
    private int contarLummonsEnCasilla(int cx, int cy) {
        int count = 0;
        for (Animal a : entitymanager.getAnimals()) {
            if (a instanceof Lummon && a.isAlive() && a.getTileX() == cx && a.getTileY() == cy) {
                count++;
            }
        }
        return count;
    }

    // ── Comer la presa muerta ───────────────────────────────────────────
    private void comerPresa(Zyrox presa) {
        hunger = Math.min(CAP_HAMBRE, hunger + 60);
        heal(15);
    }

    // ── Llamadas heredadas (el GameLoop usa update(world, delta)) ───────
    @Override
    public void update(World.World world) {}

    @Override
    public void update(World.World world, double deltaTime) {
        super.update(world, deltaTime);
    }

    // ── Dibujo ─────────────────────────────────────────────────────────
    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        int[] slotOffsetX = {0, 1, 0, 1, 0};
        int[] slotOffsetY = {0, 0, 1, 1, 2};
        int half = tileSize / 3;

        int px = cameraX + tileX * tileSize + slotOffsetX[slot] * half;
        int py = cameraY + tileY * tileSize + slotOffsetY[slot] * half;

        // Color cambia si está cazando (hambre baja)
        g.setColor(hunger <= HAMBRE_UMBRAL ? new Color(255, 80, 80) : Color.WHITE);
        g.fillOval(px, py, half, half);

        g.setColor(Color.BLACK);
        g.drawOval(px, py, half, half);

        int eyeSize = Math.max(2, half / 6);
        g.setColor(Color.RED);
        g.fillOval(px + half / 4, py + half / 3, eyeSize, eyeSize);
        g.fillOval(px + half / 2, py + half / 3, eyeSize, eyeSize);
    }
}