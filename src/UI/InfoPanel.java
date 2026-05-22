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


public class InfoPanel extends JPanel {

    private JLabel lblPos;
    private JLabel lblTileType;
    private JLabel lblEntityName;
    
    private javax.swing.JProgressBar barHealth;
    private javax.swing.JProgressBar barEnergy;
    private javax.swing.JProgressBar barHunger;
    private javax.swing.JProgressBar barSpeed;
    private javax.swing.JProgressBar barStrength;
    
    private JLabel lblGender;
    private JLabel lblSpeedValue;
    private JLabel lblStrengthValue;
    
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
    private JButton btnFocus;
    private GamePanel GP;
    private JButton btnFollow;  
    private JButton btnStopFollow;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JButton btnHeal;
    private JButton btnDelete;

    public InfoPanel() {

        new javax.swing.Timer(100, e -> {
            refresh();
        }).start();
        
        // PANEL BASE

        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0,0));
        setBackground(new Color(28, 30, 34));
        JPanel content = new JPanel();
        content.setPreferredSize(
            new Dimension(320, 1200)
        );
        content.setLayout(
            new BoxLayout(content, BoxLayout.Y_AXIS)
        );
        content.setBackground(new Color(28, 30, 34));
        setBorder(
            BorderFactory.createEmptyBorder(14, 14, 14, 36)
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
                BorderFactory.createEmptyBorder(10, 12, 10, 36)
            )
        );

        panelPos.setMaximumSize(
             new Dimension(Integer.MAX_VALUE, 46)
        );
        lblPos = new JLabel("0,0");
        lblPos.setFont(valueFont);
        lblPos.setForeground(Color.WHITE);
        panelPos.add(lblPos);
        content.add(panelPos);
        content.add(Box.createVerticalStrut(14));

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
            new Dimension(Integer.MAX_VALUE, 50)
        );
        lblTileType = new JLabel("-");
        lblTileType.setFont(valueFont);
        lblTileType.setForeground(Color.WHITE);
        panelTile.add(lblTileType);
        content.add(panelTile);
        content.add(Box.createVerticalStrut(14));

  
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
            new Dimension(Integer.MAX_VALUE, 100)
        );

        scroll.setPreferredSize(
            new Dimension(320, 110)
        );

        content.add(scroll);

        content.add(Box.createVerticalStrut(14));

        // ENTITY INFO

        JPanel panelEntity = new JPanel();

        panelEntity.setLayout(
            new BoxLayout(panelEntity, BoxLayout.Y_AXIS)
        );

        panelEntity.setBackground(panelColor);

        javax.swing.border.TitledBorder borderEntity =
            BorderFactory.createTitledBorder(
                "Selected Entity"
            );

        borderEntity.setTitleColor(Color.WHITE);

        panelEntity.setBorder(
            BorderFactory.createCompoundBorder(
                borderEntity,
                BorderFactory.createEmptyBorder(
                    16,
                    16,
                    16,
                    16
                )
            )
        );

        panelEntity.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                350
            )
        );

        panelEntity.setPreferredSize(
            new Dimension(
                320,
                350
            )
        );

        // NAME

        lblEntityName = new JLabel("-");

        lblEntityName.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                24
            )
        );

        lblEntityName.setForeground(
            new Color(120,220,255)
        );

        // GENDER

        lblGender = new JLabel("-");

        lblGender.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                16
            )
        );

        // BARS

        barHealth = createBar(
            new Color(90,220,120)
        );

        barEnergy = createBar(
            new Color(80,180,255)
        );

        barHunger = createBar(
            new Color(255,180,70)
        );

        // STAT BOXES

        lblSpeedValue = createStatBox(
            new Color(70,140,255)
        );

        lblStrengthValue = createStatBox(
            new Color(220,70,70)
        );

        // ADD COMPONENTS

        panelEntity.add(lblEntityName);

        panelEntity.add(
            Box.createVerticalStrut(6)
        );

        panelEntity.add(lblGender);

        panelEntity.add(
            Box.createVerticalStrut(18)
        );

        // HEALTH

        panelEntity.add(
            createStatLabel("Health")
        );

        panelEntity.add(barHealth);

        panelEntity.add(
            Box.createVerticalStrut(12)
        );

        // ENERGY

        panelEntity.add(
            createStatLabel("Energy")
        );

        panelEntity.add(barEnergy);

        panelEntity.add(
            Box.createVerticalStrut(12)
        );

        // HUNGER

        panelEntity.add(
            createStatLabel("Hunger")
        );

        panelEntity.add(barHunger);

        panelEntity.add(
            Box.createVerticalStrut(18)
        );

        // SPEED + STRENGTH ROW

        JPanel statBoxes = new JPanel();

        statBoxes.setLayout(
            new java.awt.GridLayout(
                1,
                2,
                14,
                0
            )
        );

        statBoxes.setBackground(panelColor);

        statBoxes.add(
            createStatCard(
                "Speed",
                lblSpeedValue
            )
        );

        statBoxes.add(
            createStatCard(
                "Strength",
                lblStrengthValue
            )
        );

        panelEntity.add(statBoxes);

        content.add(panelEntity);

        content.add(
            Box.createVerticalStrut(14)
        );
        
        

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
            new Dimension(Integer.MAX_VALUE, 80)
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
        content.add(panelResource);
        content.add(Box.createVerticalStrut(14));

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
            new Dimension(Integer.MAX_VALUE, 300)
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
            new Dimension(Integer.MAX_VALUE, 36)
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
            new Dimension(Integer.MAX_VALUE, 36)
        );

        btnSearch = new JButton("Search");
        btnHeal = new JButton("Heal");
        btnDelete = new JButton("Kill");
        JPanel panelSearch = new JPanel();
        panelSearch.setBackground(panelColor);
        panelSearch.setLayout(
            new java.awt.BorderLayout(8,0)
        );
        panelSearch.add(txtSearch, java.awt.BorderLayout.CENTER);
        panelSearch.add(btnSearch, java.awt.BorderLayout.EAST);
        panelEdit.add(panelSearch);
        
        panelEdit.add(
            Box.createVerticalStrut(18)
        );

        JLabel lblActions =
            new JLabel("Actions");

        lblActions.setForeground(
            Color.LIGHT_GRAY
        );

        lblActions.setFont(smallFont);

        panelEdit.add(lblActions);

        panelEdit.add(
            Box.createVerticalStrut(8)
        );

        JPanel panelActions =
            new JPanel();

        panelActions.setBackground(
            panelColor
        );

        panelActions.setLayout(
            new java.awt.GridLayout(
                1,
                2,
                8,
                0
            )
        );

        btnHeal.setBackground(
            new Color(70,170,90)
        );

        btnHeal.setForeground(
            Color.WHITE
        );

        btnDelete.setBackground(
            new Color(180,60,60)
        );

        btnDelete.setForeground(
            Color.WHITE
        );

        panelActions.add(btnHeal);

        panelActions.add(btnDelete);

        panelEdit.add(panelActions);

        content.add(panelEdit);

        content.add(Box.createVerticalStrut(14));

        entityList.addListSelectionListener(e -> {

            int index = entityList.getSelectedIndex();
            if (index >= 0 &&
                index < tileEntities.size()) {
                selectedEntity = tileEntities.get(index);
                if (selectedEntity instanceof Resource) {
                    selectedResource = (Resource) selectedEntity;
                } else {
                    selectedResource = null;
                }
            }
        });

        btnFocus.addActionListener(e -> {

            if (selectedEntity == null || GP == null) {
                return;
            }

            GP.focusEntity(selectedEntity);
        });

        btnFollow.addActionListener(e -> {

            if (selectedEntity == null || GP == null) {
                return;
            }

            GP.followEntity(selectedEntity);
        });

        btnStopFollow.addActionListener(e -> {

            if (GP == null) {
                return;
            }

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
        Dimension btnSize = new Dimension(110, 36);
        
        btnHeal.addActionListener(e -> {

            if (!(selectedEntity instanceof Animal)) {
                return;
            }

            Animal a =
                (Animal) selectedEntity;

            a.heal(
                a.getMaxHealth()
            );

            a.setEnergy(
                Animal.CAP_ENERGIA
            );

            a.setHunger(
                Animal.CAP_HAMBRE
            );
        });

        btnDelete.addActionListener(e -> {

            if (selectedEntity == null) {
                return;
            }

            selectedEntity.setAlive(false);

            selectedEntity = null;

            selectedResource = null;
        });
        
        btnFocus.setPreferredSize(btnSize);
        btnFollow.setPreferredSize(btnSize);
        btnStopFollow.setPreferredSize(btnSize);
        btnApplyName.setPreferredSize(btnSize);
        btnSearch.setPreferredSize(btnSize);
        btnHeal.setPreferredSize(btnSize);
        btnDelete.setPreferredSize(btnSize);
        panelPos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelTile.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEntity.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelResource.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelEdit.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        add(content);
    }
    
    //Creacion de los barsssssss
    private javax.swing.JProgressBar createBar(Color color) {

        javax.swing.JProgressBar bar =
            new javax.swing.JProgressBar();

        bar.setMinimum(0);

        bar.setMaximum(100);

        bar.setValue(0);

        bar.setStringPainted(true);

        bar.setForeground(color);

        bar.setBackground(
            new Color(28,28,28)
        );

        bar.setBorder(
            BorderFactory.createLineBorder(
                new Color(70,70,70),
                1
            )
        );

        bar.setFont(
            new Font(
                "Consolas",
                Font.BOLD,
                13
            )
        );

        bar.setPreferredSize(
            new Dimension(260, 30)
        );

        bar.setMaximumSize(
            new Dimension(
                Integer.MAX_VALUE,
                30
            )
        );

        return bar;
    }


    //Update de los Bars-------------
    private void updateBar(
        javax.swing.JProgressBar bar,
        int value,
        int max
    ) {

        value = Math.max(
            0,
            Math.min(value, max)
        );

        bar.setMaximum(max);

        bar.setValue(value);

        int percent =
            (int)((value / (double)max) * 100);

        bar.setString(
            value + " / " + max
            + "   (" + percent + "%)"
        );

        bar.repaint();
    }

    private JLabel createStatLabel(String text) {

        JLabel lbl = new JLabel(text);

        lbl.setForeground(Color.LIGHT_GRAY);

        lbl.setFont(new Font("Arial", Font.BOLD, 13));

        return lbl;
    }
    private JLabel createStatBox(Color color) {

        JLabel lbl = new JLabel(
            "0",
            JLabel.CENTER
        );

        lbl.setOpaque(true);

        lbl.setBackground(color);

        lbl.setForeground(Color.WHITE);

        lbl.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                26
            )
        );

        lbl.setPreferredSize(
            new Dimension(
                110,
                70
            )
        );

        lbl.setBorder(
            BorderFactory.createLineBorder(
                color.brighter(),
                2
            )
        );

        return lbl;
    }

    private JPanel createStatCard(
        String title,
        JLabel valueLabel
    ) {

        JPanel panel = new JPanel();

        panel.setLayout(
            new BoxLayout(
                panel,
                BoxLayout.Y_AXIS
            )
        );

        panel.setBackground(
            new Color(52,55,60)
        );

        JLabel titleLabel =
            new JLabel(title);

        titleLabel.setForeground(
            Color.LIGHT_GRAY
        );

        titleLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                15
            )
        );

        titleLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        valueLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        panel.add(titleLabel);

        panel.add(
            Box.createVerticalStrut(8)
        );

        panel.add(valueLabel);

        return panel;
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
            if (selectedEntity instanceof Resource) {
                selectedResource = (Resource) selectedEntity;
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

        if (selectedEntity instanceof Animal) {

            Animal a = (Animal) selectedEntity;

            if (!a.isAlive()) {
                selectedEntity = null;
                return;
            }

            lblEntityName.setText(
                "<html>"
                + a.getCustomName()
                + "<br>"
                + "<span style='font-size:15px;color:white;'>"
                + a.getClass().getSimpleName()
                + "   •   ID #"
                + a.getId()
                + "</span>"
                + "</html>"
            );

            // GENDER

            String genderText;

            Color genderColor;

            if (a.getSex() == Animal.Sex.MALE) {
                genderText = "♂ Male";

                genderColor =
                    new Color(90,170,255);

            } else {

                genderText = "♀ Female";

                genderColor =
                    new Color(255,120,180);
            }

            lblGender.setText(genderText);

            lblGender.setForeground(genderColor);

            // BARS

            updateBar(
                barHealth,
                a.getHealth(),
                a.getMaxHealth()
            );

            updateBar(
                barEnergy,
                a.getEnergy(),
                Animal.CAP_ENERGIA
            );

            updateBar(
                barHunger,
                a.getHunger(),
                Animal.CAP_HAMBRE
            );

            // STAT BOXES

            lblSpeedValue.setText(
                String.valueOf(
                    a.getSpeed()
                )
            );

            lblStrengthValue.setText(
                String.valueOf(
                    a.getAttack()
                )
            );

        } else {

            lblEntityName.setText("-");

            lblGender.setText("-");

            updateBar(
                barHealth,
                0,
                100
            );

            updateBar(
                barEnergy,
                0,
                100
            );

            updateBar(
                barHunger,
                0,
                100
            );

            lblSpeedValue.setText("0");

            lblStrengthValue.setText("0");
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