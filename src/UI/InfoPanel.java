package UI;

import Entities.*;
import World.Tile;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class InfoPanel extends JPanel {

    // LBLs debe coincidir nombres con el del panelInfo
    private JLabel lblLocation;
    private JLabel lblTileType;
    private JLabel lblEntityName;
    private JLabel lblHealth;
    private JLabel lblEnergy;
    private JLabel lblResourceName;
    private JLabel lblResourceAmount;

    public InfoPanel() {

        // Inicializar labels -------------------------
        lblLocation = new JLabel();
        lblTileType = new JLabel();
        lblEntityName = new JLabel();
        lblHealth = new JLabel();
        lblEnergy = new JLabel();
        lblResourceName = new JLabel();
        lblResourceAmount = new JLabel();

        // Añadirlos al panel (simple)
        add(lblLocation);
        add(lblTileType);
        add(lblEntityName);
        add(lblHealth);
        add(lblEnergy);
        add(lblResourceName);
        add(lblResourceAmount);
    }

    public void updateInfo(int x, int y, Tile tile, Entity entity, Resource resource) {

        // POSICIÓN
        lblLocation.setText("Pos: " + x + ", " + y);

        // TILE
        if (tile != null) {
            lblTileType.setText(String.valueOf(tile.getType()));
        } else {
            lblTileType.setText("-");
        }

        // ENTITY
        if (entity != null) {

            lblEntityName.setText(entity.getClass().getSimpleName());

            if (entity instanceof Animal) {
                Animal a = (Animal) entity;

                lblHealth.setText(String.valueOf(a.getHealth()));
                lblEnergy.setText(String.valueOf(a.getEnergy()));
            } else {
                lblHealth.setText("-");
                lblEnergy.setText("-");
            }

        } else {

            lblEntityName.setText("-");
            lblHealth.setText("-");
            lblEnergy.setText("-");
        }

        // RESOURCE
        if (resource != null) {

            lblResourceName.setText(resource.getClass().getSimpleName());
            lblResourceAmount.setText(String.valueOf(resource.getQuantity()));

        } else {

            lblResourceName.setText("-");
            lblResourceAmount.setText("-");
        }
    }
}