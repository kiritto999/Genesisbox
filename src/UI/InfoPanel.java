package UI;

import Entities.*;
import World.Tile;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JColorChooser;


public class InfoPanel extends JPanel {

    private JLabel lblPos;
    private JLabel lblTileType;
    private JLabel lblEntityName;
    private JLabel lblHealth;
    private JLabel lblEnergy;
    private JLabel lblHunger;
    private JLabel lblSpeed;
    private JLabel lblStrength;
    private JLabel lblResourceName;
    private JLabel lblResourceAmount;
    private int selectedTileX;
    private int selectedTileY;
    private Tile selectedTile;
    private Entity selectedEntity;
    private Resource selectedResource;
    private ArrayList<Entity> tileEntities = new ArrayList<>();
    private DefaultListModel<String> entityListModel;
    private JList<String> entityList;
    private JTextField txtName;
    private JButton btnApplyName;
    private JButton btnChangeColor;
    private JButton btnFocus;
    private GamePanel GP;
    private JButton btnFollow;  
    private JButton btnStopFollow;
    private JTextField txtSearch;
    private JButton btnSearch;

    public InfoPanel() {

        new javax.swing.Timer(100, e -> {
            refresh();
        }).start();
        
        // PANEL BASE

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(28, 30, 34));
        setBorder(
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        );
        Font titleFont = new Font("Arial", Font.BOLD, 22);
        Font valueFont = new Font("Arial", Font.PLAIN, 15);
        Font smallFont = new Font("Arial", Font.PLAIN, 13);
        Color panelColor = new Color(52,55,60);

        // POSITION
 

        JPanel panelPos = new JPanel();
        panelPos.setLayout(new BoxLayout(panelPos, BoxLayout.Y_AXIS));
        panelPos.setBackground(panelColor);
        javax.swing.border.TitledBorder borderPos =
            BorderFactory.createTitledBorder("Position");

        borderPos.setTitleColor(Color.WHITE);

        panelPos.setBorder(
            BorderFactory.createCompoundBorder(
                borderPos,
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            )
        );

        panelPos.setMaximumSize(
             new Dimension(Integer.MAX_VALUE, 90)
        );
        lblPos = new JLabel("0,0");
        lblPos.setFont(valueFont);
        lblPos.setForeground(Color.WHITE);
        panelPos.add(lblPos);
        add(panelPos);
        add(Box.createVerticalStrut(14));

        // TILE
        JPanel panelTile = new JPanel();
        panelTile.setLayout(new BoxLayout(panelTile, BoxLayout.Y_AXIS));
        panelTile.setBackground(panelColor);
        javax.swing.border.TitledBorder borderTile =
            BorderFactory.createTitledBorder("Tile");
        borderTile.setTitleColor(Color.WHITE);
        panelTile.setBorder(
            BorderFactory.createCompoundBorder(
                borderTile,
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            )
        );

        panelTile.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 90)
        );
        lblTileType = new JLabel("-");
        lblTileType.setFont(valueFont);
        lblTileType.setForeground(Color.WHITE);
        panelTile.add(lblTileType);
        add(panelTile);
        add(Box.createVerticalStrut(14));

  
        // ENTITY LIST

        entityListModel = new DefaultListModel<>();
        entityList = new JList<>(entityListModel);
        entityList.setFont(new Font("Consolas", Font.PLAIN, 14));
        entityList.setBackground(new Color(35,35,35));
        entityList.setForeground(Color.WHITE);
        entityList.setSelectionBackground(new Color(90,90,90));
        entityList.setSelectionForeground(Color.CYAN);
        entityList.setFixedCellHeight(24);
        entityList.setBorder(
            BorderFactory.createEmptyBorder(4,4,4,4)
        );

        JScrollPane scroll = new JScrollPane(entityList);

        scroll.setBackground(new Color(35,35,35));

        scroll.getViewport().setBackground(new Color(35,35,35));

        scroll.setBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY)
        );

        scroll.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 140)
        );

        scroll.setPreferredSize(
            new Dimension(320, 110)
        );

        add(scroll);

        add(Box.createVerticalStrut(14));

        // ENTITY INFO

        JPanel panelEntity = new JPanel();
        panelEntity.setLayout(new BoxLayout(panelEntity, BoxLayout.Y_AXIS));
        panelEntity.setBackground(panelColor);
        javax.swing.border.TitledBorder borderEntity =
            BorderFactory.createTitledBorder("Selected Entity");

        borderEntity.setTitleColor(Color.WHITE);

        panelEntity.setBorder(
            BorderFactory.createCompoundBorder(
                borderEntity,
                BorderFactory.createEmptyBorder(14, 14, 14, 14)
            )
        );

        panelEntity.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 260)
        );

        lblEntityName = new JLabel("-");
        lblHealth = new JLabel("-");
        lblEnergy = new JLabel("-");
        lblHunger = new JLabel("-");
        lblSpeed = new JLabel("-");
        lblStrength = new JLabel("-");
        lblEntityName.setFont(titleFont);
        lblHealth.setFont(valueFont);
        lblEnergy.setFont(valueFont);
        lblHunger.setFont(valueFont);
        lblSpeed.setFont(valueFont);
        lblStrength.setFont(valueFont);
        lblEntityName.setForeground(new Color(120,220,255));
        lblHealth.setForeground(new Color(100,255,100));
        lblEnergy.setForeground(new Color(120,220,255));
        lblHunger.setForeground(new Color(255,180,80));
        lblSpeed.setForeground(new Color(255,120,220));
        lblStrength.setForeground(new Color(255,120,120));
        panelEntity.add(lblEntityName);
        panelEntity.add(Box.createVerticalStrut(14));
        panelEntity.add(lblHealth);
        panelEntity.add(Box.createVerticalStrut(6));
        panelEntity.add(lblEnergy);
        panelEntity.add(Box.createVerticalStrut(6));
        panelEntity.add(lblHunger);
        panelEntity.add(Box.createVerticalStrut(6));
        panelEntity.add(lblSpeed);
        panelEntity.add(Box.createVerticalStrut(6));
        panelEntity.add(lblStrength);
        add(panelEntity);
        add(Box.createVerticalStrut(14));

        //Resources
        JPanel panelResource = new JPanel();
        panelResource.setLayout(new BoxLayout(panelResource, BoxLayout.Y_AXIS));
        panelResource.setBackground(panelColor);

        javax.swing.border.TitledBorder borderResource =
            BorderFactory.createTitledBorder("Resource");

        borderResource.setTitleColor(Color.WHITE);

        panelResource.setBorder(
            BorderFactory.createCompoundBorder(
                borderResource,
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
            )
        );

        panelResource.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 260)
        );

        lblResourceName = new JLabel("-");
        lblResourceAmount = new JLabel("-");
        lblResourceName.setFont(valueFont);
        lblResourceAmount.setFont(valueFont);
        lblResourceName.setForeground(Color.WHITE);
        lblResourceAmount.setForeground(Color.CYAN);
        panelResource.add(lblResourceName);
        panelResource.add(Box.createVerticalStrut(4));
        panelResource.add(lblResourceAmount);
        add(panelResource);
        add(Box.createVerticalStrut(14));

        //editor
        JPanel panelEdit = new JPanel();
        panelEdit.setLayout(new BoxLayout(panelEdit, BoxLayout.Y_AXIS));
        panelEdit.setBackground(panelColor);
        javax.swing.border.TitledBorder borderEditor =
            BorderFactory.createTitledBorder("Editor");

        borderEditor.setTitleColor(Color.WHITE);

        panelEdit.setBorder(
            BorderFactory.createCompoundBorder(
                borderEditor,
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            )
        );

        panelEdit.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 340)
        );

        JLabel lblRename = new JLabel("Rename");
        lblRename.setForeground(Color.LIGHT_GRAY);
        lblRename.setFont(smallFont);
        panelEdit.add(lblRename);
        panelEdit.add(Box.createVerticalStrut(6));
        txtName = new JTextField();
        txtName.setBackground(new Color(35,35,35));
        txtName.setForeground(Color.WHITE);
        txtName.setCaretColor(Color.WHITE);
        txtName.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 340)
        );

        JPanel panelRename = new JPanel();
        panelRename.setBackground(panelColor);
        panelRename.setLayout(
            new java.awt.BorderLayout(8,0)
        );
        btnApplyName = new JButton("Apply");
        panelRename.add(txtName, java.awt.BorderLayout.CENTER);
        panelRename.add(btnApplyName, java.awt.BorderLayout.EAST);
        panelEdit.add(panelRename);
        panelEdit.add(Box.createVerticalStrut(10));

        
        btnChangeColor = new JButton("Change Color");

        JPanel colorPanel = new JPanel();

        colorPanel.setBackground(panelColor);

        colorPanel.setLayout(
            new java.awt.FlowLayout(
                java.awt.FlowLayout.CENTER
            )
        );

        colorPanel.add(btnChangeColor);

        panelEdit.add(colorPanel);
        
        panelEdit.add(Box.createVerticalStrut(12));

        JLabel lblCamera = new JLabel("Camera");
        lblCamera.setForeground(Color.LIGHT_GRAY);
        lblCamera.setFont(smallFont);
        panelEdit.add(lblCamera);
        panelEdit.add(Box.createVerticalStrut(6));
        btnFocus = new JButton("Focus");
        btnFollow = new JButton("Follow");
        btnStopFollow = new JButton("Stop");
        JPanel panelCameraButtons = new JPanel();
        panelCameraButtons.setBackground(panelColor);
        panelCameraButtons.setLayout(
            new java.awt.GridLayout(1, 3, 8, 0)
        );
        panelCameraButtons.add(btnFocus);
        panelCameraButtons.add(btnFollow);
        panelCameraButtons.add(btnStopFollow);
        panelEdit.add(panelCameraButtons);
        panelEdit.add(Box.createVerticalStrut(14));


        JLabel lblSearch = new JLabel("Search Entity");
        lblSearch.setForeground(Color.LIGHT_GRAY);
        lblSearch.setFont(smallFont);
        panelEdit.add(lblSearch);
        panelEdit.add(Box.createVerticalStrut(6));
        txtSearch = new JTextField();
        txtSearch.setBackground(new Color(35,35,35));
        txtSearch.setForeground(Color.WHITE);
        txtSearch.setCaretColor(Color.WHITE);
        txtSearch.setMaximumSize(
            new Dimension(5000, 90)
        );

        btnSearch = new JButton("Search");
        JPanel panelSearch = new JPanel();
        panelSearch.setBackground(panelColor);
        panelSearch.setLayout(
            new java.awt.BorderLayout(8,0)
        );
        panelSearch.add(txtSearch, java.awt.BorderLayout.CENTER);
        panelSearch.add(btnSearch, java.awt.BorderLayout.EAST);
        panelEdit.add(panelSearch);

        add(panelEdit);

        add(Box.createVerticalStrut(14));

        entityList.addListSelectionListener(e -> {

            int index = entityList.getSelectedIndex();
            if (index >= 0 &&
                index < tileEntities.size()) {
                selectedEntity = tileEntities.get(index);
                if (selectedEntity instanceof Resource r) {
                    selectedResource = r;
                } else {
                    selectedResource = null;
                }
            }
        });

        btnFocus.addActionListener(e -> {

            if (selectedEntity == null) return;
            GP.focusEntity(selectedEntity);
        });

        btnFollow.addActionListener(e -> {
            if (selectedEntity == null) return;
            GP.followEntity(selectedEntity);
        });

        btnStopFollow.addActionListener(e -> {
            GP.stopFollowing();
        });

        btnSearch.addActionListener(e -> {

            String text = txtSearch.getText();
            if (text.isBlank()) return;
            Entity found = null;

            try {
                int id = Integer.parseInt(text);
                found = GP.getEntityManager()
                          .findById(id);

            } catch (NumberFormatException ex) {

                ArrayList<Entity> results =
                    GP.getEntityManager()
                      .findByName(text);
                if (!results.isEmpty()) {
                    found = results.get(0);
                }
            }

            if (found != null) {
                selectedEntity = found;
                GP.focusEntity(found);
            }
        });

        btnApplyName.addActionListener(e -> {
            if (selectedEntity == null) return;
            String newName = txtName.getText();
            if (newName == null ||
                newName.isBlank()) return;
            selectedEntity.setCustomName(newName);
            entityList.repaint();
        });

        btnChangeColor.addActionListener(e -> {

            if (selectedEntity == null) return;
            Color newColor = JColorChooser.showDialog(
                null,
                "Choose Color",
                selectedEntity.getCustomColor()
            );
            if (newColor != null) {
                selectedEntity.setCustomColor(newColor);
            }
        });
        Dimension btnSize = new Dimension(110, 36);

        btnFocus.setPreferredSize(btnSize);
        btnFollow.setPreferredSize(btnSize);
        btnStopFollow.setPreferredSize(btnSize);
        btnApplyName.setPreferredSize(btnSize);
        btnSearch.setPreferredSize(btnSize);
        btnChangeColor.setPreferredSize(btnSize);
        panelPos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTile.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEntity.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelResource.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEdit.setAlignmentX(Component.LEFT_ALIGNMENT);
    }
    
    public void setSelected( int tileX,int tileY,Tile tile,Entity entity,Resource resource){
        this.selectedTileX = tileX;
        this.selectedTileY = tileY;
        this.selectedTile = tile;

        this.selectedEntity = entity;
        this.selectedResource = resource;
    }


    public void setTileEntities(ArrayList<Entity> entities) {
        
        tileEntities = entities;
        entityListModel.clear();
        for (Entity e : entities) {
            entityListModel.addElement(
                e.getCustomName()+ " ("+ e.getClass().getSimpleName()+ ") #"+ e.getId()
            );
        }

        if (!entities.isEmpty()) {

            selectedEntity = entities.get(0);
            if (selectedEntity instanceof Resource r) {
                selectedResource = r;
            } else {
                selectedResource = null;
            }

        } else {
            selectedEntity = null;
            selectedResource = null;
        }
    }

    
    
    public void refresh() {

        // POSITION
        lblPos.setText(
            selectedTileX + ", " + selectedTileY
        );

        // TILE
        if (selectedTile != null) {

            if (selectedTile.getType() == Tile.WATER) {

                lblTileType.setText("Type: WATER");

            } else if (selectedTile.getType() == Tile.GRASS) {

                lblTileType.setText("Type: GRASS");
            }

        } else {

            lblTileType.setText("-");
        }

        // ENTITY
        if (selectedEntity instanceof Animal a) {
            if (!a.isAlive()) {
                selectedEntity = null;
                return;
            }
            lblEntityName.setText(
                "Name: " + a.getCustomName() +  "( " +a.getClass().getSimpleName() +") "
            );

            lblHealth.setText(
                "Health: " + a.getHealth()
            );

            lblEnergy.setText(
                "Energy: " + a.getEnergy()
            );

            lblHunger.setText(
                "Hunger: " + a.getHunger()
            );

            lblSpeed.setText(
                "Speed: " + a.getSpeed()
            );

            lblStrength.setText(
                "Strength: " + a.getAttack()
            );

        } else {
            lblEntityName.setText("-");
            lblHealth.setText("-");
            lblEnergy.setText("-");
            lblHunger.setText("-");
            lblSpeed.setText("-");
            lblStrength.setText("-");
        }

        // RESOURCE
        if (selectedResource != null) {

            lblResourceName.setText(
                "Resource: " +
                selectedResource.getClass().getSimpleName()
            );

            lblResourceAmount.setText(
                "Quantity: " +
                selectedResource.getQuantity()
            );

        } else {
            lblResourceName.setText("-");
            lblResourceAmount.setText("-");
        }
    }
    public void setGamePanel(GamePanel gp) {
        this.GP = gp;
    }
}