/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.Tile;
import World.World;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Friedrick
 */
public class Entitymanager {
 
    private ArrayList<Entity> entities  = new ArrayList<>();
    private ArrayList<Animal> animals = new ArrayList<>();
    private ArrayList<Resource> resources = new ArrayList<>();
    private final List<Entity>   toAdd     = new ArrayList<>();
 
    private final Random rng = new Random();
    private final World  world;
 
    public Entitymanager(World world) {
        this.world = world;
        spawnInicial();
    }
    
    // Spawn inicial 
 
    private void spawnInicial() {
        spawnRecursos(Tree.class,  8);
        spawnRecursos(Nero.class, 5);
        spawnRecursos(Food.class,  6);
    }

    private void spawnRecursos(Class<?> clazz, int cantidad) {
        int colocados = 0;
        int intentos = 0;

        while (colocados < cantidad && intentos < 200) {
            intentos++;

            int x = rng.nextInt(world.getColums());
            int y = rng.nextInt(world.getRows());

            if (world.getMap()[y][x].getType() != Tile.GRASS) continue;

            int capacidad = 0;
            if (clazz == Tree.class) capacidad = 3;
            else if (clazz == Nero.class) capacidad = 4;
            else if (clazz == Food.class) capacidad = 2;

            if (contarEspacioTile(x, y) + capacidad > 5) continue;

            Resource r = crearRecurso(clazz, x, y);
            if (r != null) {
                agregarEntidad(r);
                colocados++;
            }
        }
    }
 
    private Resource crearRecurso(Class<?> clazz, int x, int y) {
        if (clazz == Tree.class)  return new Tree(x, y);
        if (clazz == Nero.class) return new Nero(x, y);
        if (clazz == Food.class)  return new Food(x, y);
        return null;
    }
 
 // Actualización 
 
    public void update(double deltaTime) {
        // Solo recursos con update simple
        for (Entity e : entities) {
            if (!(e instanceof Animal)) {
                e.update(world);
            }
        }
        // Animales con deltaTime
        for (Animal a : animals) {
            a.update(world, deltaTime);
        }
        eliminarMuertas();
 
        for (Entity e : toAdd) {
            entities.add(e);
            if (e instanceof Resource r) resources.add(r);
        }
        toAdd.clear();
    }
 
    private void eliminarMuertas() {
        Iterator<Entity> it = entities.iterator();
        while (it.hasNext()) {
            Entity e = it.next();
            if (!e.isAlive()) {
                it.remove();
                resources.remove(e);
            }
        }
    }
 
    // Utilidades 
    
    private void agregarEntidad(Entity e) {
        int capacidad = obtenerCapacidad(e);
        e.slot = getSlotLibre(e.getTileX(), e.getTileY(), capacidad);

        if (e.slot == -1) return;

        entities.add(e);
        if (e instanceof Resource r) resources.add(r);
    }

    private void agregarAnimal(Animal a) {
        a.slot = getSlotLibre(a.getTileX(), a.getTileY(), a.getCapacity());

        if (a.slot == -1) return;

        animals.add(a);
    }
    
    public int contarEspacioTile(int x, int y){
        int espacio = 0;
        for (Entity e : entities){
            if (e.getTileX() == x && e.getTileY() == y) {
                if (e instanceof Tree)  espacio += 3;
                else if (e instanceof Nero)  espacio += 4;
                else if (e instanceof Food)  espacio += 2;
                else espacio += 1;
            }
        }
        for (Animal a : animals){
            if (a.getTileX() == x && a.getTileY() == y) espacio += a.getCapacity();
        }
        return espacio;
    }
    
    private int obtenerCapacidad(Entity e) {
    if (e instanceof Tree) return 3;
    if (e instanceof Nero) return 4;
    if (e instanceof Food) return 2;
    if (e instanceof Animal a) return a.getCapacity();
    return 1;
    }
    
    public int getSlotLibre(int x, int y, int capacidad) {
        boolean[] ocupados = new boolean[5];

        for (Entity e : entities) {
            if (e.getTileX() == x && e.getTileY() == y) {
                int cap = obtenerCapacidad(e);
                for (int i = e.slot; i < e.slot + cap && i < 5; i++) {
                    ocupados[i] = true;
                }
            }
        }

        for (Animal a : animals) {
            if (a.getTileX() == x && a.getTileY() == y) {
                for (int i = a.slot; i < a.slot + a.getCapacity() && i < 5; i++) {
                    ocupados[i] = true;
                }
            }
        }

        for (int i = 0; i <= 5 - capacidad; i++) {
            boolean libre = true;

            for (int j = i; j < i + capacidad; j++) {
                if (ocupados[j]) {
                    libre = false;
                    break;
                }
            }

            if (libre) return i;
        }

        return -1;
    }
 
    public ArrayList<Entity>   getEntities()  { return entities;  }
    public ArrayList<Resource> getResources() { return resources; }
    public ArrayList<Animal> getAnimals() {return animals;}
    public void addEntity(Entity e) {toAdd.add(e);}
    public void addAnimal(Animal a) {
        a.slot = getSlotLibre(a.getTileX(), a.getTileY(), a.getCapacity());

        if (a.slot == -1) return;

        animals.add(a);
        entities.add(a);
    }

    public void addResourse(Resource r) {
        int capacidad = obtenerCapacidad(r);
        r.slot = getSlotLibre(r.getTileX(), r.getTileY(), capacidad);

        if (r.slot == -1) return;

        resources.add(r);
        entities.add(r);
    }
    
}