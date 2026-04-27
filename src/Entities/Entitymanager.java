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
 
    // Actualización Recursos
 
    public void update() {
        for (Entity e : entities) {
            e.update(world);
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
        for (Entity e : entities) {
            if (e.getTileX() == x && e.getTileY() == y) return true;
        }
        return false;
    }
 
    private void agregarEntidad(Entity e) {
        entities.add(e);
        if (e instanceof Resource r) resources.add(r);
    }
 
 
    public ArrayList<Entity>   getEntities()  { return entities;  }
    public ArrayList<Resource> getResources() { return resources; }
    public ArrayList<Animal> getAnimals() {return animals;}
    public void addEntity(Entity e) {toAdd.add(e);}
    public void addAnimal(Animal a) {animals.add(a);}
    public void addResourse (Resource r) {resources.add(r);}
}