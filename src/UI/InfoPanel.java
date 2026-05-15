package UI;

import Entities.*;
import World.Tile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel {

    private JLabel lblPos;
    private JLabel lblTileType;
    private JLabel lblEntityName;
    private JLabel lblHealth;
    private JLabel lblEnergy;
    private JLabel lblHunger;
    private JLabel lblThirst;
    private JLabel lblSpeed;
    private JLabel lblStrength;

    private JLabel lblResourceName;
    private JLabel lblResourceAmount;

    public InfoPanel() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(40, 40, 40));

        Font titleFont = new Font("Arial", Font.BOLD, 14);
        Font valueFont = new Font("Arial", Font.PLAIN, 16);

        //cordenadas------------------------------------
        JPanel panelPos = new JPanel();
        panelPos.setLayout(new BoxLayout(panelPos, BoxLayout.Y_AXIS));
        panelPos.setBackground(new Color(60, 60, 60));
        panelPos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Posición"),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panelPos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        lblPos = new JLabel("0,0");
        lblPos.setFont(valueFont);
        lblPos.setForeground(Color.WHITE);

        panelPos.add(new JLabel("POSICIÓN"));
        panelPos.add(lblPos);
        add(panelPos);
        add(Box.createVerticalStrut(10));


        //tile------------------------------------
        JPanel panelTile = new JPanel();
        panelTile.setLayout(new BoxLayout(panelTile, BoxLayout.Y_AXIS));
        panelTile.setBackground(new Color(60, 60, 60));
        panelTile.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Tile"),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panelTile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        lblTileType = new JLabel("-");
        lblTileType.setFont(valueFont);
        lblTileType.setForeground(Color.WHITE);

        panelTile.add(new JLabel("TILE"));
        panelTile.add(lblTileType);
        add(panelTile);
        add(Box.createVerticalStrut(10));


        //entity------------------------------------
        JPanel panelEntity = new JPanel();
        panelEntity.setLayout(new BoxLayout(panelEntity, BoxLayout.Y_AXIS));
        panelEntity.setBackground(new Color(60, 60, 60));
        panelEntity.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Entidad"),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        panelEntity.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        lblEntityName = new JLabel("-");
        lblHealth = new JLabel("-");
        lblEnergy = new JLabel("-");
        lblHunger = new JLabel("-");
        lblThirst = new JLabel("-");
        lblSpeed = new JLabel("-");
        lblStrength = new JLabel("-");

        lblEntityName.setFont(valueFont);
        lblHealth.setFont(valueFont);
        lblEnergy.setFont(valueFont);
        lblHunger.setFont(valueFont);
        lblThirst.setFont(valueFont);
        lblSpeed.setFont(valueFont);
        lblStrength.setFont(valueFont);

        lblEntityName.setForeground(Color.WHITE);
        lblHealth.setForeground(Color.GREEN);
        lblEnergy.setForeground(Color.YELLOW);
        lblHunger.setForeground(Color.ORANGE);
        lblThirst.setForeground(Color.CYAN);
        lblSpeed.setForeground(Color.PINK);
        lblStrength.setForeground(Color.RED);

        panelEntity.add(lblEntityName);
        panelEntity.add(lblHealth);
        panelEntity.add(lblEnergy);
        panelEntity.add(lblHunger);
        panelEntity.add(lblThirst);
        panelEntity.add(lblSpeed);
        panelEntity.add(lblStrength);

        add(panelEntity);
        add(Box.createVerticalStrut(10));


        //resource------------------------------------
        JPanel panelResource = new JPanel();
        panelResource.setLayout(new BoxLayout(panelResource, BoxLayout.Y_AXIS));
        panelResource.setBackground(new Color(60, 60, 60));
        panelResource.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Recurso"),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panelResource.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        lblResourceName = new JLabel("-");
        lblResourceAmount = new JLabel("-");

        lblResourceName.setFont(valueFont);
        lblResourceAmount.setFont(valueFont);

        lblResourceName.setForeground(Color.WHITE);
        lblResourceAmount.setForeground(Color.CYAN);

        panelResource.add(lblResourceName);
        panelResource.add(lblResourceAmount);

        add(panelResource);
    }

    public void updateInfo(int x, int y, Tile tile, Entity entity, Resource resource) {

        lblPos.setText(x + ", " + y);

        // TILE TEXTO
        if (tile != null) {
            if (tile.getType() == Tile.WATER) {
                lblTileType.setText("Type: WATER");
            } else if (tile.getType() == Tile.GRASS) {
                lblTileType.setText("Type: GRASS");
            }
        } else {
            lblTileType.setText("-");
        }

        // ENTITY
        if (entity instanceof Animal a) {

            lblEntityName.setText("Name: " + a.getClass().getSimpleName());
            lblHealth.setText("Heal: " + a.getHealth());
            lblEnergy.setText("Energy: " + a.getEnergy());
            lblHunger.setText("Hunger: " + a.getHunger());           
            lblSpeed.setText("Speed: " + a.getSpeed());
            lblStrength.setText("Strenght: " + a.getAttack());

        } else {
            lblEntityName.setText("-");
            lblHealth.setText("-");
            lblEnergy.setText("-");
            lblHunger.setText("-");
            lblThirst.setText("-");
            lblSpeed.setText("-");
            lblStrength.setText("-");
        }

        // RESOURCE
        if (resource != null) {
            lblResourceName.setText("Recurso: " + resource.getClass().getSimpleName());
            lblResourceAmount.setText("Cantidad: " + resource.getQuantity());
        } else {
            lblResourceName.setText("-");
            lblResourceAmount.setText("-");
        }
    }
}