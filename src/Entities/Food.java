/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.World;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Bayas:
 * @author Friedrick
 */
public class Food extends Resource {

    public enum Etapa { NORMAL, MADURA }

    public static final Color COLOR_MADURA  = new Color(220, 50,  80);
    public static final Color COLOR_NORMAL  = new Color(200, 160, 80);

    private Etapa stage;
    private int tiempoCrecimiento;
    private static final int INTERVALO_MADUREZ = 500; 

    public Food(int tileX, int tileY) {
        super("Bayas", tileX, tileY,
                /*maxHealth*/   20,
                /*maxQuantity*/  5,
                /*regenRate*/    0,
                /*regenInterval*/0);
        this.stage = Etapa.NORMAL;
        this.tiempoCrecimiento = 0;
        this.quantity = 0; 
    }

    @Override
    public void update(World world) {
        if (!alive) return;

        // Crecer hacia madura
        if (stage == Etapa.NORMAL) {
            tiempoCrecimiento++;
            if (tiempoCrecimiento >= INTERVALO_MADUREZ) {
                stage = Etapa.MADURA;
                quantity = maxQuantity;
            }
        }

        // Morir si la vida llega a 0
        if (health <= 0) alive = false;
    }

    @Override
    public int harvest(int amount) {
        int taken = super.harvest(amount);
        if (taken > 0) stage = Etapa.NORMAL; // vuelve a normal tras cosechar
        tiempoCrecimiento = 0;
        return taken;
    }

    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        float visualSize = getVisualSize();
        int size = Math.max(6, (int)(tileSize * visualSize));
        int offset = (tileSize - size) / 2;

        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;

        g.setColor(getCurrentColor());
        int bs = size / 3;
        g.fillOval(px + offset,            py + offset,      bs, bs);
        g.fillOval(px + offset + bs,       py + offset + 2,  bs, bs);
        g.fillOval(px + offset + bs / 2,   py + offset + bs, bs, bs);
    }

    public Color getCurrentColor() {
        return stage == Etapa.MADURA ? COLOR_MADURA : COLOR_NORMAL;
    }

    public float getVisualSize() {
        return stage == Etapa.MADURA ? 0.45f : 0.35f; // madura es más grande
    }

    public Etapa getStage() { return stage; }
}

