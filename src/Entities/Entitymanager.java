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
        int intentos  = 0;
        while (colocados < cantidad && intentos < 200) {
            intentos++;
            int x = rng.nextInt(world.getColums());
            int y = rng.nextInt(world.getRows());
            if (world.getMap()[y][x].getType() != Tile.GRASS) continue;
            if (tileOcupado(x, y)) continue;
 
            Resource r = crearRecurso(clazz, x, y);
            if (r != null) { agregarEntidad(r); colocados++; }
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
 
    private boolean tileOcupado(int x, int y) {
        return contarEspacioTile(x, y) >= 5;
    }
    
    private void agregarEntidad(Entity e) {
        e.slot = getSlotLibre(e.getTileX(), e.getTileY());
        entities.add(e);
        if (e instanceof Resource r) resources.add(r);
    }
    
    private void agregarAnimal(Animal a) {
        a.slot = getSlotLibre(a.getTileX(), a.getTileY());
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
    
    public int getSlotLibre(int x, int y) {
    boolean[] ocupados = new boolean[5];
    for (Entity e : entities) {
        if (e.getTileX() == x && e.getTileY() == y) ocupados[e.slot] = true;
    }
    for (Animal a : animals) {
        if (a.getTileX() == x && a.getTileY() == y) ocupados[a.slot] = true;
    }
    for (int i = 0; i < 5; i++) {
        if (!ocupados[i]) return i;
    }
    return 0;
}
 
    public ArrayList<Entity>   getEntities()  { return entities;  }
    public ArrayList<Resource> getResources() { return resources; }
    public ArrayList<Animal> getAnimals() {return animals;}
    public void addEntity(Entity e) {toAdd.add(e);}
    public void addAnimal(Animal a) {animals.add(a); entities.add(a); a.slot = getSlotLibre(a.getTileX(), a.getTileY()); }
    public void addResourse (Resource r) {entities.add(r); resources.add(r); r.slot = getSlotLibre(r.getTileX(), r.getTileY());}
    
}