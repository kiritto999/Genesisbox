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
 * Zenthra: árbol con 4 etapas.
 */
public class Zenthra extends Resource {

    private static final Logger logger = Logger.getLogger(Zenthra.class.getName());

    public enum Etapa_Crecimiento { SEMILLA, JOVEN, MADURO, VIEJO }

    private static BufferedImage spriteSapling;
    private static BufferedImage spriteJoven;
    private static BufferedImage spriteMaduro;
    private static BufferedImage spriteOld;
    private static boolean spritesLoaded = false;

    public static final Color COLOR_SEMILLA = new Color(219, 54,  36);
    public static final Color COLOR_JOVEN   = new Color(180, 44,  29);
    public static final Color COLOR_MADURO  = new Color(140, 35,  23);
    public static final Color COLOR_VIEJO   = new Color( 79, 20,  13);

    private Etapa_Crecimiento stage;
    private int Tiempo_Crecimiento;
    private static final int INTERVALO_CRECIMIENTO = 1200;

    private static void loadSprites() {
        if (spritesLoaded) return;
        try {
            spriteSapling = ImageIO.read(Zenthra.class.getResourceAsStream("/sprites/zenthra_sapling.png"));
            spriteJoven   = ImageIO.read(Zenthra.class.getResourceAsStream("/sprites/zenthra_joven.png"));
            spriteMaduro  = ImageIO.read(Zenthra.class.getResourceAsStream("/sprites/zenthra_maduro.png"));
            spriteOld     = ImageIO.read(Zenthra.class.getResourceAsStream("/sprites/zenthra_old.png"));
        } catch (IOException | IllegalArgumentException e) {
            logger.log(Level.WARNING, "No se pudieron cargar los sprites de Zenthra", e);
        }
        spritesLoaded = true;
    }

    public Zenthra(int tileX, int tileY) {
        super("Zenthra", tileX, tileY, 50, 10, 0, 0);
        loadSprites();
        this.stage              = Etapa_Crecimiento.SEMILLA;
        this.Tiempo_Crecimiento = 0;
        this.quantity           = 2;
    }

    @Override
    public void update(World world) {
        super.update(world);
        if (!alive) return;
        Tiempo_Crecimiento++;
        if (Tiempo_Crecimiento >= INTERVALO_CRECIMIENTO) {
            Tiempo_Crecimiento = 0;
            avanzarCrecimiento();
        }
        if (health <= 0) alive = false;
    }

    private BufferedImage currentSprite() {
        switch (stage) {
            case SEMILLA: return spriteSapling;
            case JOVEN:   return spriteJoven;
            case MADURO:  return spriteMaduro;
            case VIEJO:   return spriteOld;
            default:      return spriteSapling;
        }
    }

    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;

        BufferedImage sprite = currentSprite();

        if (sprite != null) {
            float escala;
            switch (stage) {
                case SEMILLA: escala = 1.55f; break;
                case JOVEN:   escala = 1.95f; break;
                case MADURO:  escala = 2.40f; break;
                case VIEJO:   escala = 3.00f; break;
                default:      escala = 0.55f; break;
            }
            double ratio = (double) sprite.getWidth() / sprite.getHeight();
            int drawH = (int)(tileSize * escala);
            int drawW = (int)(drawH * ratio);
            int dx = px + (tileSize - drawW) / 2;
            int dy = py + tileSize - drawH;
            g2.drawImage(sprite, dx, dy, drawW, drawH, null);
        } else {
            drawFallback(g2, tileSize, px, py);
        }
    }

    private void drawFallback(Graphics2D g2, int tileSize, int px, int py) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[] slotOffsetX = {0, 1, 0, 1, 0};
        int[] slotOffsetY = {0, 0, 1, 1, 2};
        int half = tileSize / 2;
        int sx = px + slotOffsetX[slot] * half;
        int sy = py + slotOffsetY[slot] * half;
        int cx = sx + half / 2;

        if (stage == Etapa_Crecimiento.SEMILLA) {
            int r = Math.max(3, half / 6);
            g2.setColor(COLOR_SEMILLA);
            g2.fillOval(cx - r, sy + half / 2 - r, r * 2, r * 2);
            return;
        }

        int copaR;
        switch (stage) {
            case JOVEN:  copaR = Math.max(4, (int)(half * 0.30)); break;
            case MADURO: copaR = Math.max(5, (int)(half * 0.38)); break;
            case VIEJO:  copaR = Math.max(5, (int)(half * 0.43)); break;
            default:     copaR = Math.max(4, (int)(half * 0.30)); break;
        }

        int tw     = Math.max(2, half / 8);
        int th     = Math.max(4, half / 3);
        int trunkX = cx - tw / 2;
        int trunkY = sy + half - th;
        int copaY  = Math.max(sy, trunkY - copaR * 2);

        g2.setColor(new Color(100, 60, 20));
        g2.fillRect(trunkX, trunkY, tw, th);
        g2.setColor(getCurrentColor());
        g2.fillOval(cx - copaR, copaY, copaR * 2, copaR * 2);
    }

    private void avanzarCrecimiento() {
        switch (stage) {
            case SEMILLA:
                stage = Etapa_Crecimiento.JOVEN;
                maxQuantity = 5;
                break;
            case JOVEN:
                stage = Etapa_Crecimiento.MADURO;
                maxQuantity = 10;
                maxHealth = 80;
                health = Math.min(health + 20, 80);
                break;
            case MADURO:
                stage = Etapa_Crecimiento.VIEJO;
                maxQuantity = 6;
                break;
            case VIEJO:
                break;
        }
    }

    public Etapa_Crecimiento getStage() { return stage; }

    public Color getCurrentColor() {
        switch (stage) {
            case SEMILLA: return COLOR_SEMILLA;
            case JOVEN:   return COLOR_JOVEN;
            case MADURO:  return COLOR_MADURO;
            case VIEJO:   return COLOR_VIEJO;
            default:      return COLOR_SEMILLA;
        }
    }

    public void setStage(Etapa_Crecimiento stage)  { this.stage = stage; }
    public void setTiempoCrecimiento(int tiempo)   { this.Tiempo_Crecimiento = tiempo; }
}