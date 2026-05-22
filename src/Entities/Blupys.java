/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.World;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Blupys: arbusto de bayas, reemplaza a Food.
 * Etapas visuales: NORMAL → sprite young, MADURA → sprite madure
 * Al agotarse entra en MURIENDO con fade out.
 */
public class Blupys extends Resource {

    private static final Logger logger = Logger.getLogger(Blupys.class.getName());

    public enum Etapa { NORMAL, MADURA, MURIENDO }

    public static final Color COLOR_MADURA = new Color(220, 50,  80);
    public static final Color COLOR_NORMAL = new Color(200, 160, 80);

    // Sprites compartidos
    private static BufferedImage spriteYoung;
    private static BufferedImage spriteMadure;
    private static boolean spritesLoaded = false;

    private Etapa stage;
    private int tiempoCrecimiento;
    private int timerMuerte = 0;

    private static final int INTERVALO_MADUREZ = 500;
    private static final int DURACION_MUERTE   = 80;

    private static void loadSprites() {
        if (spritesLoaded) return;
        try {
            spriteYoung  = ImageIO.read(Blupys.class.getResourceAsStream("/sprites/blupys_young.png"));
            spriteMadure = ImageIO.read(Blupys.class.getResourceAsStream("/sprites/blupys_madure.png"));
        } catch (IOException | IllegalArgumentException e) {
            logger.log(Level.WARNING, "No se pudieron cargar los sprites del Blupys", e);
        }
        spritesLoaded = true;
    }

    public Blupys(int tileX, int tileY) {
        super("Blupys", tileX, tileY,
                /*maxHealth*/   20,
                /*maxQuantity*/  5,
                /*regenRate*/    0,
                /*regenInterval*/0);
        loadSprites();
        this.stage = Etapa.NORMAL;
        this.tiempoCrecimiento = 0;
        this.quantity = 0;
    }

    @Override
    public void update(World world) {
        if (!alive) return;
        
        // Mientras se desvanece solo cuenta el timer
        if (stage == Etapa.MURIENDO) {
            timerMuerte++;
            if (timerMuerte >= DURACION_MUERTE)
                alive = false;
            return;
        }

        // Crecer hacia madura
        if (stage == Etapa.NORMAL) {
            tiempoCrecimiento++;
            if (tiempoCrecimiento >= INTERVALO_MADUREZ) {
                stage = Etapa.MADURA;
                quantity = maxQuantity;
            }
        }

        // Iniciar fade cuando se agota
        if (isDepleted()) stage = Etapa.MURIENDO;
        if (health <= 0)  alive = false;
    }

    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // Alpha para el fade
        float alpha = (stage == Etapa.MURIENDO)
                ? 1f - (float) timerMuerte / DURACION_MUERTE
                : 1f;

        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;

        BufferedImage sprite = (stage == Etapa.MADURA || stage == Etapa.MURIENDO)
                ? spriteMadure : spriteYoung;

        Composite original = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, alpha)));

        if (sprite != null) {
            // Escalar manteniendo proporción, ocupar ~90% del tile
            double ratio = (double) sprite.getWidth() / sprite.getHeight();
            int drawH = (int)(tileSize * 0.90);
            int drawW = (int)(drawH * ratio);
            int dx = px + (tileSize - drawW) / 2;
            int dy = py + (tileSize - drawH) / 2;
            g2.drawImage(sprite, dx, dy, drawW, drawH, null);
        } else {
            // Fallback geométrico
            int[] slotOffsetX = {0, 1, 0, 1, 0};
            int[] slotOffsetY = {0, 0, 1, 1, 2};
            int half = tileSize / 2;
            int spx = px + slotOffsetX[slot] * half;
            int spy = py + slotOffsetY[slot] * half;
            int size   = Math.max(6, (int)(half * getVisualSize()));
            int offset = (half - size) / 2;
            g2.setColor(getCurrentColor());
            int bs = size / 3;
            g2.fillOval(spx + offset,            spy + offset,      bs, bs);
            g2.fillOval(spx + offset + bs,       spy + offset + 2,  bs, bs);
            g2.fillOval(spx + offset + bs / 2,   spy + offset + bs, bs, bs);
        }

        g2.setComposite(original);
    }

    public Color getCurrentColor() {
        return stage == Etapa.MADURA ? COLOR_MADURA : COLOR_NORMAL;
    }

    public float getVisualSize() {
        return stage == Etapa.MADURA ? 0.45f : 0.35f;
    }

    public Etapa getStage() { return stage; }
}

