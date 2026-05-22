/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.World;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Corpse: Cadáver del Zyrox.
 * Usa el sprite zyrox_dead.png como base visual.
 * quantity = número de Lummons que participaron en la caza (máx 4).
 * Cada Lummon puede hacer harvest(1) para alimentarse.
 * Desaparece cuando quantity == 0 o tras MAX_AGE ticks.
 * Los puntitos amarillos indican cuántas porciones quedan.
 */
public class Corpse extends Resource {

    private static final Logger logger = Logger.getLogger(Corpse.class.getName());

    private static final int MAX_AGE = 1500;

    // ── Sprite ─────────────────────────────────────────────────────────
    private static BufferedImage spriteDead;
    private static boolean spriteLoaded = false;

    private int age = 0;

    private static void loadSprite() {
        if (spriteLoaded) return;
        try {
            spriteDead = ImageIO.read(Corpse.class.getResourceAsStream("/sprites/zyrox_dead.png"));
        } catch (IOException | IllegalArgumentException e) {
            logger.log(Level.WARNING, "No se pudo cargar el sprite del Corpse", e);
        }
        spriteLoaded = true;
    }

    // ── Constructor ────────────────────────────────────────────────────
    /**
     * @param tileX     tile X
     * @param tileY     tile Y
     * @param cazadores cuántos Lummons participaron en la caza (1-4)
     */
    public Corpse(int tileX, int tileY, int cazadores) {
        super("Cadaver", tileX, tileY,
                /*maxHealth*/    1,
                /*maxQuantity*/  Math.max(1, Math.min(4, cazadores)),
                /*regenRate*/    0,
                /*regenInterval*/0);
        this.quantity = this.maxQuantity;
        loadSprite();
    }

    // ── Update ─────────────────────────────────────────────────────────
    @Override
    public void update(World world) {
        if (!alive) return;
        age++;
        if (age >= MAX_AGE || quantity <= 0) {
            alive = false;
        }
    }

    // ── Dibujo ─────────────────────────────────────────────────────────
    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;

        if (spriteDead != null) {
            drawSprite(g2, px, py, tileSize);
        } else {
            drawFallback(g2, px, py, tileSize);
        }

        // Puntitos amarillos de porciones restantes (siempre se dibujan)
        drawFoodDots(g2, px, py, tileSize);
    }

    // ── Sprite del cadáver ────────────────────────────────────────────
    private void drawSprite(Graphics2D g2, int px, int py, int tileSize) {
        // El sprite del Zyrox muerto es horizontal (ancho > alto)
        double ratio = (double) spriteDead.getWidth() / spriteDead.getHeight();
        int drawW = (int)(tileSize * 1.6);          // ligeramente más grande que el tile
        int drawH = (int)(drawW / ratio);

        int dx = px + (tileSize - drawW) / 2;
        int dy = py + (tileSize - drawH) / 2 + 2;  // ligero offset hacia abajo

        // Con el tiempo el sprite se vuelve más transparente (podrido)
        float alpha = age < MAX_AGE / 2
                ? 1.0f
                : 1.0f - 0.5f * ((float)(age - MAX_AGE / 2) / (MAX_AGE / 2));
        alpha = Math.max(0.3f, alpha);

        java.awt.AlphaComposite ac = java.awt.AlphaComposite.getInstance(
                java.awt.AlphaComposite.SRC_OVER, alpha);
        g2.setComposite(ac);
        g2.drawImage(spriteDead, dx, dy, drawW, drawH, null);
        g2.setComposite(java.awt.AlphaComposite.getInstance(
                java.awt.AlphaComposite.SRC_OVER, 1.0f));
    }

    // ── Fallback geométrico si no hay sprite ──────────────────────────
    private void drawFallback(Graphics2D g2, int px, int py, int tileSize) {
        Color colorFresh = new Color(160, 40, 40);
        Color colorOld   = new Color( 90, 30, 20);

        int w  = (int)(tileSize * 0.52);
        int h  = (int)(tileSize * 0.28);
        int bx = px + (tileSize - w) / 2;
        int by = py + (tileSize - h) / 2 + 2;

        // Sombra
        g2.setColor(new Color(0, 0, 0, 55));
        g2.fillOval(bx + 3, by + 4, w, h);

        // Cuerpo
        Color base = (age < MAX_AGE / 2) ? colorFresh : colorOld;
        g2.setColor(base);
        g2.fillOval(bx, by, w, h);

        // Cabeza con X en ojos
        int hs = (int)(tileSize * 0.20);
        g2.setColor(base.darker());
        g2.fillOval(bx - hs / 2, by - hs / 3, hs, hs);
        g2.setColor(Color.WHITE);
        int ex = bx - hs / 2 + hs / 5;
        int ey = by - hs / 3 + hs / 5;
        int es = Math.max(2, hs / 4);
        g2.drawLine(ex,      ey,      ex + es, ey + es);
        g2.drawLine(ex + es, ey,      ex,      ey + es);
    }

    // ── Puntitos amarillos de comida restante ─────────────────────────
    private void drawFoodDots(Graphics2D g2, int px, int py, int tileSize) {
        if (quantity <= 0) return;

        int dotSize   = Math.max(5, tileSize / 8);
        int dotGap    = dotSize + 3;
        int totalW    = quantity * dotGap - 3;
        int startX    = px + (tileSize - totalW) / 2;
        int dotY      = py + tileSize - dotSize - 3;

        // Sombra de los puntos
        g2.setColor(new Color(0, 0, 0, 100));
        for (int i = 0; i < quantity; i++) {
            g2.fillOval(startX + i * dotGap + 1, dotY + 1, dotSize, dotSize);
        }

        // Puntos amarillos brillantes
        g2.setColor(new Color(255, 215, 0));
        for (int i = 0; i < quantity; i++) {
            g2.fillOval(startX + i * dotGap, dotY, dotSize, dotSize);
        }

        // Borde de los puntos
        g2.setColor(new Color(180, 140, 0));
        for (int i = 0; i < quantity; i++) {
            g2.drawOval(startX + i * dotGap, dotY, dotSize, dotSize);
        }
    }

    public int getAge() {return age;}
    
    
}
