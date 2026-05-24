/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
/**
 *
 * @author Friedrick
 */
public class LightningEffect {

    private static Image gifImage;

    // Posición en tiles donde cayó el rayo
    private int tileX, tileY;

    // Duración en ms y tiempo de inicio
    private long startTime;
    private static final long DURACION_MS = 1200; // duración del GIF visible

    private boolean active = false;
    private long lastUsedTime = 0;
    private static final long COOLDOWN_MS = 5000;

    // Constructor
    public LightningEffect() {
        if (gifImage == null) {
            try {
                java.net.URL url = LightningEffect.class.getResource("/sprites/Lightning.gif");
                if (url != null) {
                    gifImage = new ImageIcon(url).getImage();
                } else {
                    System.err.println("No se encontró /sprites/rayo.gif");
                }
            } catch (Exception e) {
                System.err.println("Error cargando rayo.gif: " + e.getMessage());
            }
        }
    }

    // Lanzar el rayo en un tile
    public void lanzar(int tileX, int tileY) {
        if (!puedeUsarse()) return;   // ← bloquea si está en cooldown
        this.tileX        = tileX;
        this.tileY        = tileY;
        this.startTime    = System.currentTimeMillis();
        this.lastUsedTime = System.currentTimeMillis();  // ← registra el uso
        this.active       = true;
    }

    // Actualizar: desactivar cuando termina el tiempo
    public void update() {
        if (active) {
            if (System.currentTimeMillis() - startTime >= DURACION_MS) {
                active = false;
            }
        }
    }
    
    public boolean puedeUsarse() {
        return System.currentTimeMillis() - lastUsedTime >= COOLDOWN_MS;
    }
    
    public int getCooldownRestante() {
        long elapsed = System.currentTimeMillis() - lastUsedTime;
        long restanteMs = COOLDOWN_MS - elapsed;
        if (restanteMs <= 0) {
            return 0;
        }
        return (int) Math.ceil(restanteMs / 1000.0);
    }

    public void draw(Graphics g, int tileSize, int cameraX, int cameraY, java.awt.image.ImageObserver observer) {
        if (!active || gifImage == null) return;

        Graphics2D g2 = (Graphics2D) g;

        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;

        // El GIF se dibuja 2 tiles de alto, centrado horizontalmente
        int drawW = tileSize * 2;
        int drawH = tileSize * 2;
        int dx = px - tileSize / 2;   // centrar un poco
        int dy = py - tileSize;        // arriba del tile objetivo

        g2.drawImage(gifImage, dx, dy, drawW, drawH, observer);
    }

    public boolean isActive() { return active; }
    public int getTileX()     { return tileX; }
    public int getTileY()     { return tileY; }
    public int getCooldownMax() {return (int) (COOLDOWN_MS / 1000);}
}
