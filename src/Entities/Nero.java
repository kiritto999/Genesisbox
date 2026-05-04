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

/**
 * Nero: Piedra grande que ocupa 4 slots.
 * Se dibuja cubriendo los 4 cuadrantes superiores del tile,
 * dejando el slot 4 (esquina inferior izquierda) libre.
 */
public class Nero extends Resource {

    public static final Color COLOR_LLENA = new Color(49, 120, 34);
    public static final Color COLOR_VACIA = new Color(37, 77, 26);

    public Nero(int tileX, int tileY) {
        super("Nero", tileX, tileY,
                /*maxHealth*/   100,
                /*maxQuantity*/  15,
                /*regenRate*/     1,
                /*regenInterval*/300);
    }

    @Override
    public void update(World world) {
        super.update(world);
    }

    @Override
    public void draw(Graphics g, int tileSize, int cameraX, int cameraY) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int px = cameraX + tileX * tileSize;
        int py = cameraY + tileY * tileSize;

        // La piedra cubre los 4 cuadrantes superiores del tile
        // Slots 0,1 = fila superior | Slots 2,3 = fila media
        // Slot 4 (inferior izquierda) queda libre para otras entidades
        int half = tileSize / 2;

        // Tamaño: ocupa 2x2 cuadrantes con un pequeño margen
        int margin = (int)(tileSize * 0.05);
        int stoneW = tileSize - margin * 2;       // ancho: casi todo el tile
        int stoneH = (int)(tileSize * 0.65);      // alto: ~65% del tile (deja slot 4 libre abajo)

        int sx = px + margin;
        int sy = py + margin;

        // Sombra
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillRoundRect(sx + 3, sy + 3, stoneW, stoneH, 14, 14);

        // Cuerpo principal
        g2.setColor(getCurrentColor());
        g2.fillRoundRect(sx, sy, stoneW, stoneH, 14, 14);

        // Brillo superior
        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillRoundRect(sx + stoneW / 5, sy + stoneH / 10,
                stoneW * 3 / 5, stoneH / 4, 8, 8);

        // Grietas (Quitalos si no te gusta owo)
        g2.setColor(getCurrentColor().darker().darker());
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawLine(sx + stoneW / 3,       sy + stoneH / 4,
                    sx + stoneW / 3 + 8,   sy + stoneH / 2);
        g2.drawLine(sx + stoneW * 2 / 3,   sy + stoneH / 3,
                    sx + stoneW * 2 / 3 - 6, sy + stoneH * 2 / 3);

        // Borde
        g2.setColor(getCurrentColor().darker());
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(sx, sy, stoneW, stoneH, 14, 14);


        g2.setStroke(new BasicStroke(1f));
    }

    public Color getCurrentColor() {
        return isDepleted() ? COLOR_VACIA : COLOR_LLENA;
    }
}
