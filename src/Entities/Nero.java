/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import World.World;
import java.awt.BasicStroke;
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
 * Nero: cristal oscuro enorme que ocupa 4 slots.
 * Usa sprite Nero_sprite.png escalado grande.
 */
public class Nero extends Resource {

    private static final Logger logger = Logger.getLogger(Nero.class.getName());

    public static final Color COLOR_LLENA = new Color(49, 120, 34);
    public static final Color COLOR_VACIA = new Color(37, 77, 26);

    private static BufferedImage sprite;
    private static boolean spriteLoaded = false;

    private static void loadSprite() {
        if (spriteLoaded) return;
        try {
            sprite = ImageIO.read(Nero.class.getResourceAsStream("/sprites/Nero_sprite.png"));
        } catch (IOException | IllegalArgumentException e) {
            logger.log(Level.WARNING, "No se pudo cargar el sprite del Nero", e);
        }
        spriteLoaded = true;
    }

    public Nero(int tileX, int tileY) {
        super("Nero", tileX, tileY,
                /*maxHealth*/   100,
                /*maxQuantity*/  15,
                /*regenRate*/     1,
                /*regenInterval*/300);
        loadSprite();
    }

    @Override
    public void update(World world) {
        super.update(world);
    }

    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;

        if (sprite != null) {
            // Nero es ENORME: escala 1.8x el tile, centrado y anclado a la base
            double ratio = (double) sprite.getWidth() / sprite.getHeight();
            int drawH = (int)(tileSize * 1.8);
            int drawW = (int)(drawH * ratio);
            int dx = px + (tileSize - drawW) / 2;
            int dy = py + tileSize - drawH;      // anclar base al tile
            g2.drawImage(sprite, dx, dy, drawW, drawH, null);

            // Tinte oscuro si está agotado
            if (isDepleted()) {
                g2.setColor(new Color(0, 0, 0, 100));
                g2.fillRect(dx, dy, drawW, drawH);
            }
        } else {
            // Fallback geométrico original
            drawFallback(g2, tileSize, px, py);
        }
    }

    private void drawFallback(Graphics2D g2, int tileSize, int px, int py) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int margin = (int)(tileSize * 0.05);
        int stoneW = tileSize - margin * 2;
        int stoneH = (int)(tileSize * 0.65);
        int sx = px + margin;
        int sy = py + margin;

        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRoundRect(sx + 3, sy + 3, stoneW, stoneH, 14, 14);
        g2.setColor(getCurrentColor());
        g2.fillRoundRect(sx, sy, stoneW, stoneH, 14, 14);
        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillRoundRect(sx + stoneW / 5, sy + stoneH / 10, stoneW * 3 / 5, stoneH / 4, 8, 8);
        g2.setColor(getCurrentColor().darker().darker());
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawLine(sx + stoneW / 3, sy + stoneH / 4, sx + stoneW / 3 + 8, sy + stoneH / 2);
        g2.drawLine(sx + stoneW * 2 / 3, sy + stoneH / 3, sx + stoneW * 2 / 3 - 6, sy + stoneH * 2 / 3);
        g2.setColor(getCurrentColor().darker());
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(sx, sy, stoneW, stoneH, 14, 14);
        g2.setStroke(new BasicStroke(1f));
    }

    public Color getCurrentColor() {
        return isDepleted() ? COLOR_VACIA : COLOR_LLENA;
    }
}
