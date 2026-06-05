/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author Friedrick
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class StarEffect {

    private static Image gifImage;

    private int tileX, tileY;
    private long startTime;
    private static final long DURACION_MS = 1500;
    private boolean active = false;
    private long lastUsedTime = 0;
    private static final long COOLDOWN_MS = 20000;
    private boolean impactoAplicado = false;
    private static final long IMPACT_TIME = 900;

    public StarEffect() {
        if (gifImage == null) {
            cargarGif();
        }
    }
    public boolean debeAplicarImpacto() {
        return active
            && !impactoAplicado
            && (System.currentTimeMillis() - startTime >= IMPACT_TIME);
    }

    public void marcarImpactoAplicado() {
        impactoAplicado = true;
    }

    private void cargarGif() {
        try {
            URL url = StarEffect.class.getResource("/sprites/meteor.gif");
            if (url != null) {
                gifImage = new ImageIcon(url).getImage();
            } else {
                System.err.println("No se encontró /sprites/meteor.gif");
            }
        } catch (Exception e) {
            System.err.println("Error cargando meteor.gif: " + e.getMessage());
        }
    }

    public boolean puedeUsarse() {
        return System.currentTimeMillis() - lastUsedTime >= COOLDOWN_MS;
    }

    public void lanzar(int tileX, int tileY) {
        if (!puedeUsarse()) return;
        this.tileX = tileX;
        this.tileY = tileY;
        this.startTime = System.currentTimeMillis();
        this.lastUsedTime = System.currentTimeMillis();
        this.active = true;
        this.impactoAplicado = false;

        gifImage = null;
        cargarGif();
    }

    public void update() {
        if (active) {
            if (System.currentTimeMillis() - startTime >= DURACION_MS) {
                active = false;
            }
        }
    }

    public void draw(Graphics g, int tileSize, int cameraX, int cameraY,
                     java.awt.image.ImageObserver observer) {
        if (!active || gifImage == null) return;
        Graphics2D g2 = (Graphics2D) g;
        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;
        int drawW = tileSize * 6;
        int drawH = tileSize * 6;
        int dx = px - drawW / 2 + tileSize / 2;
        int dy = py - drawH + tileSize;
        g2.drawImage(gifImage, dx, dy, drawW, drawH, observer);
    }

    public boolean isActive() { return active; }

    public int getCooldownRestante() {
        long elapsed = System.currentTimeMillis() - lastUsedTime;
        int restante = (int)((COOLDOWN_MS - elapsed) / 1000) + 1;
        return Math.max(0, restante);
    }

    public int getCooldownMax() {
        return (int)(COOLDOWN_MS / 1000);
    }
    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

}
