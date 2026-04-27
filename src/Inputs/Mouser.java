/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;
import UI.GamePanel;
import java.awt.event.MouseWheelEvent;

/**
 *
 * @author blope
 */
public class Mouser implements MouseListener,MouseMotionListener, MouseWheelListener{
    
    private Camera camera;
    private GamePanel jpanel;
    
    private int lastX,lastY;
    boolean dragging = false;
    
    
    public Mouser(Camera camera, GamePanel panel) {
        this.camera = camera;
        this.jpanel = panel;
        
        jpanel.addMouseWheelListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragging = true;
        lastX = e.getX();
        lastY = e.getY();
        jpanel.handleClick(e.getX(), e.getY());
    }
    

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!dragging) return ;
        int dx = e.getX() - lastX;
        int dy = e.getY() - lastY;

        camera.Camerax += dx;
        camera.Cameray += dy;
        
        lastX = e.getX();
        lastY = e.getY();
        jpanel.limitForCamera(camera);
        
        jpanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        double oldZoom = camera.zoom;
        if (e.getWheelRotation() < 0) {
            camera.zoom *= 1.1;
        } else {
            camera.zoom *= 0.9;
        }
        camera.zoom = Math.max(0.5, Math.min(camera.zoom, 3.0));
        double scale = camera.zoom / oldZoom;

        camera.Camerax = (int)(lastX - (lastX - camera.Camerax) * scale);
        camera.Cameray = (int)(lastY - (lastY - camera.Cameray) * scale);

        jpanel.limitForCamera(camera);
        jpanel.repaint();
    }
    
}
