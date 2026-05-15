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

import World.Tile;
import World.World;
import com.sun.jdi.connect.spi.Connection;
import database.DatabaseManager;
import database.EntitySV;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Friedrick
 */
public class Entitymanager {
 
    private final ArrayList<Entity>   entities  = new ArrayList<>();
    private final ArrayList<Animal>   animals   = new ArrayList<>();
    private final ArrayList<Resource> resources = new ArrayList<>();
 
    // ── Buffers para agregar/eliminar sin modificar durante iteración ──
    private final List<Entity>  toAdd    = new ArrayList<>();
    private final List<Entity>  toRemove = new ArrayList<>();
 
    private final Random rng   = new Random();
    private final World  world;
 
    public Entitymanager(World world) {
        this.world = world;
        spawnInicial();
    }
 
    // ── Spawn inicial ──────────────────────────────────────────────────
    private void spawnInicial() {
        spawnRecursos(Tree.class,  8);
        spawnRecursos(Nero.class,  5);
        spawnRecursos(Food.class,  6);
    }
 
    private void spawnRecursos(Class<?> clazz, int cantidad) {
        int colocados = 0, intentos = 0;
        while (colocados < cantidad && intentos < 200) {
            intentos++;
            int x = rng.nextInt(world.getColums());
            int y = rng.nextInt(world.getRows());
            if (world.getMap()[y][x].getType() != Tile.GRASS) continue;
 
            int cap = capacidadDeClase(clazz);
            if (contarEspacioTile(x, y) + cap > 5) continue;
 
            Resource r = crearRecurso(clazz, x, y);
            if (r != null) { agregarEntidadDirecto(r); colocados++; }
        }
    }
 
    private Resource crearRecurso(Class<?> clazz, int x, int y) {
        if (clazz == Tree.class) return new Tree(x, y);
        if (clazz == Nero.class) return new Nero(x, y);
        if (clazz == Food.class) return new Food(x, y);
        return null;
    }
 
    private int capacidadDeClase(Class<?> clazz) {
        if (clazz == Nero.class) return 4;
        return 1;
    }
 
    // ── Actualización principal ────────────────────────────────────────
    public void update(double deltaTime) {
 
        // 1) Aplicar pendientes ANTES de iterar
        flushPending();
 
        // 2) Actualizar recursos (iterar sobre COPIA para evitar CME)
        for (Entity e : new ArrayList<>(entities)) {
            if (!(e instanceof Animal)) {
                e.update(world);
            }
        }
 
        // 3) Actualizar animales (iterar sobre COPIA)
        for (Animal a : new ArrayList<>(animals)) {
            a.update(world, deltaTime);
        }
 
        // 4) Eliminar muertos
        eliminarMuertas();
    }
 
    private void flushPending() {
        for (Entity e : toAdd) {
            entities.add(e);
            if (e instanceof Animal an) animals.add(an);
            if (e instanceof Resource r) resources.add(r);
        }
        toAdd.clear();
    }
 
    private void eliminarMuertas() {
        List<Corpse> cadaveresNuevos = new ArrayList<>();
 
        // 1) Marcar muertos y preparar cadaveres SIN modificar listas todavia
        for (Entity e : entities) {
            if (!e.isAlive()) {
                if (e instanceof Zyrox z) {
                    int cazadores = contarLummonsAdyacentesAl(z);
                    if (cazadores > 0) {
                        Corpse c = new Corpse(z.getTileX(), z.getTileY(), cazadores);
                        c.slot = 0;
                        cadaveresNuevos.add(c);
                    }
                }
                toRemove.add(e);
            }
        }
 
        // 2) Remover muertos (iteracion terminada, seguro modificar)
        for (Entity e : toRemove) {
            entities.remove(e);
            resources.remove(e);
            if (e instanceof Animal a) animals.remove(a);
        }
        toRemove.clear();
 
        // 3) Agregar cadaveres (el Zyrox ya fue removido, tile libre)
        for (Corpse c : cadaveresNuevos) {
            entities.add(c);
            resources.add(c);
        }
    }

    private int contarLummonsAdyacentesAl(Zyrox z) {
        int count = 0;
        for (Animal a : animals) {
            if (!(a instanceof Lummon l) || !l.isAlive()) continue;
            int dx = Math.abs(l.getTileX() - z.getTileX());
            int dy = Math.abs(l.getTileY() - z.getTileY());
            if (dx + dy <= 1) count++; // adyacente o mismo tile
        }
        return Math.max(1, Math.min(4, count)); // mínimo 1, máximo 4
    }
 
    // ── Agregar directamente (solo en init, antes de cualquier iteración) ──
    private void agregarEntidadDirecto(Entity e) {
        int cap = obtenerCapacidad(e);
        e.slot = getSlotLibre(e.getTileX(), e.getTileY(), cap);
        if (e.slot == -1) return;
 
        entities.add(e);
        if (e instanceof Resource r) resources.add(r);
        if (e instanceof Animal a)   animals.add(a);
    }
 
    // ── API pública para agregar durante el juego (usa buffer) ─────────
    public void addAnimal(Animal a) {
        a.slot = getSlotLibre(a.getTileX(), a.getTileY(), a.getCapacity());
        if (a.slot == -1) return;
        toAdd.add(a);
    }
 
    public void addResourse(Resource r) {
        int cap = obtenerCapacidad(r);
        r.slot = getSlotLibre(r.getTileX(), r.getTileY(), cap);
        if (r.slot == -1) return;
        toAdd.add(r);   // ← antes iba directo a las listas, aquí estaba el CME
    }
 
    public void addEntity(Entity e) {
        toAdd.add(e);
    }
 
    // ── Utilidades de espacio ──────────────────────────────────────────
    public int contarEspacioTile(int x, int y) {
        int espacio = 0;
        for (Entity e : entities) {
            if (e.getTileX() == x && e.getTileY() == y) {
                espacio += obtenerCapacidad(e);
            }
        }
        // También contar los pendientes de agregar
        for (Entity e : toAdd) {
            if (e.getTileX() == x && e.getTileY() == y) {
                espacio += obtenerCapacidad(e);
            }
        }
        return espacio;
    }
 
    private int obtenerCapacidad(Entity e) {
        if (e instanceof Nero)   return 4;
        if (e instanceof Animal a) return a.getCapacity();
        return 1;
    }
 
    public int getSlotLibre(int x, int y, int capacidad) {
        if (capacidad < 1 || capacidad > 5) return -1;
 
        boolean[] ocupados = new boolean[5];
 
        for (Entity e : entities) {
            if (e.getTileX() == x && e.getTileY() == y) {
                if (e.slot < 0 || e.slot >= 5) continue;
                int cap = obtenerCapacidad(e);
                int fin = Math.min(e.slot + cap, 5);
                for (int i = e.slot; i < fin; i++) ocupados[i] = true;
            }
        }
        // También los pendientes
        for (Entity e : toAdd) {
            if (e.getTileX() == x && e.getTileY() == y) {
                if (e.slot < 0 || e.slot >= 5) continue;
                int cap = obtenerCapacidad(e);
                int fin = Math.min(e.slot + cap, 5);
                for (int i = e.slot; i < fin; i++) ocupados[i] = true;
            }
        }
 
        for (int i = 0; i <= 5 - capacidad; i++) {
            boolean libre = true;
            for (int j = i; j < i + capacidad; j++) {
                if (ocupados[j]) { libre = false; break; }
            }
            if (libre) return i;
        }
        return -1;
    }
 
    // ── Getters ────────────────────────────────────────────────────────
    public ArrayList<Entity>   getEntities()  { return entities;  }
    public ArrayList<Resource> getResources() { return resources; }
    public ArrayList<Animal>   getAnimals()   { return animals;   }
}