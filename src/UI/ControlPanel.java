/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import Game.GameLoop;
import Entities.*;
import Inputs.Camera;
import Inputs.Mouser;
import World.World;
import java.awt.CardLayout;
import java.util.Random;
import Utils.Mode;
import Utils.Tool;
import java.awt.Color;
import Game.Game;
/**
 *
 * @author blope
 */
public class ControlPanel extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(ControlPanel.class.getName());
    
    /**
     * Creates new form ControlPanel
     */
    Random rng = new Random();
    private Game game;
    private GamePanel GP;  
    private GameLoop GLoop;
    private World world;
    private Entitymanager EManager;
    private InfoPanel infoPanel;
    private Mouser mouser;
    private Camera camera;
    private Mode currentMode = Mode.SPAWN; 
    private Tool currentBuildTool = Tool.NONE;
    
    String[] animals = {"Elegir","Lummon", "Zyrox"};
    String[] resources = {"Elegir","Food","Nero","Zenthra",};
    
    public ControlPanel(GameLoop loop, World world, Entitymanager manager, InfoPanel infoPanel ) {
        initComponents();
        SizeAdapted();

        this.GLoop = loop;
        this.world = world;
        this.EManager = manager;
        this.infoPanel = infoPanel; 
        
        camera = new Camera();
        GP = new GamePanel(world, EManager, this.infoPanel, camera);
        mouser = new Mouser(camera, GP, world,EManager);
        infoPanel.setGamePanel(GP);
        //se agreg el redibujado 
        GLoop.setGamePanel(GP);      
        
        panelGame.setLayout(new BorderLayout());
        panelGame.add(GP, BorderLayout.CENTER);
        panelGame.revalidate();

        // usar panelInfo como contenedor del InfoPanel
        panelInfo.removeAll(); //por si acaso
        panelInfo.setLayout(new BorderLayout()); 
        panelInfo.add(infoPanel, BorderLayout.CENTER);  
        panelInfo.revalidate();
        panelInfo.repaint();
        
        
        //tamaño del panel lateral
        panelInfo.setPreferredSize(new Dimension(360, 900));

        // tiempo
        lblTime.setText("");
        new javax.swing.Timer(100, e -> {
            lblTime.setText(GLoop.getTimeDay().getTimeString());
        }).start();

        // combobox
        for (String animal : animals) { 
            cboxAnimals.addItem(animal);
        }
        cboxAnimals.addActionListener(e -> {
            if (!RbtnAnimals.isSelected()) return;

            int animal = cboxAnimals.getSelectedIndex();

            switch (animal) {
                case 1 -> setTool(Tool.Lummon);
                case 2 -> setTool(Tool.Zyrox);
                default -> setTool(Tool.NONE);
            }
        });
        for (String resource : resources ){
            cboxResource.addItem(resource);
        }

        //la info empieza oculta
        panelInfo.setVisible(false);
        
        //modos
        rbtnMGenerate.setSelected(true);
        panelMode.add(panelMSpawn,"Spawn");
        panelMode.add(panelMBuilt,"Built");
        
    }
    
    
    //adapta la ventana a la pantalla
    public void SizeAdapted(){
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width ;
        int height = screen.height ;
        
        setSize(width, height);
        setLocationRelativeTo(null);
    }
    //comparativa del cbox para saber el aniamal
    public Animal makeAnimal(int tipo, int x, int y,Entitymanager emEntitymanager) {
        return switch (tipo) {
            case 1 -> new Lummon(x, y,EManager);
            case 2 -> new Zyrox(x, y,EManager);
            default -> null;
        };
    }
    //comparativa del cbox para saber el recurso
    public Resource makeResourse(int tipo, int x, int y) {
        return switch (tipo) {
            case 1 -> new Food(x, y);
            case 2 -> new Nero(x, y);
            case 3 -> new Tree(x, y);
            default -> null;
        };
    }
    //cambiar el modo de juego
    private void chansemode(String modo) {
        CardLayout cl = (CardLayout) panelMode.getLayout();
        cl.show(panelMode, modo);
    }
    
    //imprime el tool que se esta utilizando
    private void setTool(Tool tool) {
        if (mouser != null) {
            mouser.setTool(tool);
            System.out.println("Tool actual: " + tool);
        }
    }

    
  
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGSpawn = new javax.swing.ButtonGroup();
        jTextField1 = new javax.swing.JTextField();
        btnGmodes = new javax.swing.ButtonGroup();
        btnGMBuilts = new javax.swing.ButtonGroup();
        panelGame = new javax.swing.JPanel();
        menuGame = new javax.swing.JPanel();
        btnPause = new javax.swing.JButton();
        lblTime = new javax.swing.JLabel();
        btnInformationshow = new javax.swing.JButton();
        rbtnMGenerate = new javax.swing.JRadioButton();
        rbtnMbuilt = new javax.swing.JRadioButton();
        panelMode = new javax.swing.JPanel();
        panelMBuilt = new javax.swing.JPanel();
        rbtnBNone = new javax.swing.JRadioButton();
        rbtnBWater = new javax.swing.JRadioButton();
        rbtnBGlass = new javax.swing.JRadioButton();
        panelMSpawn = new javax.swing.JPanel();
        RbtnResource = new javax.swing.JRadioButton();
        cboxResource = new javax.swing.JComboBox<>();
        RbtnAnimals = new javax.swing.JRadioButton();
        cboxAnimals = new javax.swing.JComboBox<>();
        btnGenerate = new javax.swing.JToggleButton();
        RbtnGNone = new javax.swing.JRadioButton();
        btnStartTime1 = new javax.swing.JButton();
        btnTimeSpeed1 = new javax.swing.JButton();
        btnTimeSpeed10 = new javax.swing.JButton();
        btnTimeSpeed6 = new javax.swing.JButton();
        panelInfo = new javax.swing.JPanel();
        bMenuGame = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        MitemSave = new javax.swing.JMenuItem();
        MitemLoad = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        MitemGrid = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Aqualix");
        setFocusCycleRoot(false);

        panelGame.setBackground(new java.awt.Color(0, 0, 0));
        panelGame.setPreferredSize(new java.awt.Dimension(0, 320));
        panelGame.setLayout(new java.awt.BorderLayout());
        getContentPane().add(panelGame, java.awt.BorderLayout.CENTER);

        menuGame.setBackground(new java.awt.Color(51, 51, 51));
        menuGame.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 0, 0)));
        menuGame.setPreferredSize(new java.awt.Dimension(0, 137));
        menuGame.setLayout(null);

        btnPause.setBackground(new java.awt.Color(62, 62, 62));
        btnPause.setForeground(new java.awt.Color(51, 255, 0));
        btnPause.setText("⏸");
        btnPause.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnPause.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPause.addActionListener(this::btnPauseActionPerformed);
        menuGame.add(btnPause);
        btnPause.setBounds(10, 40, 50, 53);

        lblTime.setBackground(new java.awt.Color(21, 21, 21));
        lblTime.setFont(new java.awt.Font("Palatino Linotype", 1, 14)); // NOI18N
        lblTime.setForeground(new java.awt.Color(255, 255, 255));
        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTime.setText("Time");
        lblTime.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        lblTime.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        menuGame.add(lblTime);
        lblTime.setBounds(20, 10, 67, 26);

        btnInformationshow.setBackground(new java.awt.Color(0, 51, 51));
        btnInformationshow.setForeground(new java.awt.Color(255, 255, 153));
        btnInformationshow.setText("Information");
        btnInformationshow.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnInformationshow.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnInformationshow.addActionListener(this::btnInformationshowActionPerformed);
        menuGame.add(btnInformationshow);
        btnInformationshow.setBounds(210, 20, 110, 22);

        rbtnMGenerate.setBackground(new java.awt.Color(0, 102, 102));
        btnGmodes.add(rbtnMGenerate);
        rbtnMGenerate.setForeground(new java.awt.Color(255, 255, 255));
        rbtnMGenerate.setText("Generater");
        rbtnMGenerate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnMGenerate.addActionListener(this::rbtnMGenerateActionPerformed);
        menuGame.add(rbtnMGenerate);
        rbtnMGenerate.setBounds(500, 0, 110, 21);

        rbtnMbuilt.setBackground(new java.awt.Color(51, 51, 51));
        btnGmodes.add(rbtnMbuilt);
        rbtnMbuilt.setForeground(new java.awt.Color(255, 255, 255));
        rbtnMbuilt.setText("Constructor");
        rbtnMbuilt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnMbuilt.addActionListener(this::rbtnMbuiltActionPerformed);
        menuGame.add(rbtnMbuilt);
        rbtnMbuilt.setBounds(500, 20, 110, 21);

        panelMode.setLayout(new java.awt.CardLayout());

        panelMBuilt.setBackground(new java.awt.Color(43, 35, 58));

        rbtnBNone.setBackground(new java.awt.Color(47, 12, 39));
        btnGMBuilts.add(rbtnBNone);
        rbtnBNone.setForeground(new java.awt.Color(255, 255, 255));
        rbtnBNone.setText("None");
        rbtnBNone.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rbtnBNone.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnBNone.addActionListener(this::rbtnBNoneActionPerformed);
        panelMBuilt.add(rbtnBNone);

        rbtnBWater.setBackground(new java.awt.Color(47, 12, 39));
        btnGMBuilts.add(rbtnBWater);
        rbtnBWater.setForeground(new java.awt.Color(0, 102, 255));
        rbtnBWater.setText("Water");
        rbtnBWater.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rbtnBWater.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnBWater.addActionListener(this::rbtnBWaterActionPerformed);
        panelMBuilt.add(rbtnBWater);

        rbtnBGlass.setBackground(new java.awt.Color(47, 12, 39));
        btnGMBuilts.add(rbtnBGlass);
        rbtnBGlass.setForeground(new java.awt.Color(102, 255, 51));
        rbtnBGlass.setText("Glass");
        rbtnBGlass.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        rbtnBGlass.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnBGlass.addActionListener(this::rbtnBGlassActionPerformed);
        panelMBuilt.add(rbtnBGlass);

        panelMode.add(panelMBuilt, "card2");

        panelMSpawn.setBackground(new java.awt.Color(0, 102, 102));

        RbtnResource.setBackground(new java.awt.Color(0, 51, 51));
        btnGSpawn.add(RbtnResource);
        RbtnResource.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        RbtnResource.setForeground(new java.awt.Color(255, 255, 255));
        RbtnResource.setText("Resources");
        RbtnResource.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        RbtnResource.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        cboxResource.setBackground(new java.awt.Color(0, 102, 102));
        cboxResource.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cboxResource.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        RbtnAnimals.setBackground(new java.awt.Color(0, 51, 51));
        btnGSpawn.add(RbtnAnimals);
        RbtnAnimals.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        RbtnAnimals.setForeground(new java.awt.Color(255, 255, 255));
        RbtnAnimals.setText("Animals");
        RbtnAnimals.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        RbtnAnimals.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        RbtnAnimals.addActionListener(this::RbtnAnimalsActionPerformed);

        cboxAnimals.setBackground(new java.awt.Color(0, 102, 102));
        cboxAnimals.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        cboxAnimals.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnGenerate.setBackground(new java.awt.Color(0, 153, 102));
        btnGenerate.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerate.setText("Generate");
        btnGenerate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnGenerate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGenerate.addActionListener(this::btnGenerateActionPerformed);

        RbtnGNone.setBackground(new java.awt.Color(0, 51, 51));
        btnGSpawn.add(RbtnGNone);
        RbtnGNone.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        RbtnGNone.setForeground(new java.awt.Color(255, 255, 255));
        RbtnGNone.setText("None");
        RbtnGNone.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        RbtnGNone.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        RbtnGNone.addActionListener(this::RbtnGNoneActionPerformed);

        javax.swing.GroupLayout panelMSpawnLayout = new javax.swing.GroupLayout(panelMSpawn);
        panelMSpawn.setLayout(panelMSpawnLayout);
        panelMSpawnLayout.setHorizontalGroup(
            panelMSpawnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMSpawnLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMSpawnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RbtnResource, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelMSpawnLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(cboxResource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(panelMSpawnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMSpawnLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(btnGenerate, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(cboxAnimals, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(60, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMSpawnLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(RbtnGNone, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RbtnAnimals, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34))))
        );
        panelMSpawnLayout.setVerticalGroup(
            panelMSpawnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMSpawnLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(panelMSpawnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RbtnResource)
                    .addComponent(RbtnAnimals)
                    .addComponent(RbtnGNone))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelMSpawnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMSpawnLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cboxResource, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cboxAnimals, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnGenerate, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59, Short.MAX_VALUE))
        );

        panelMode.add(panelMSpawn, "card3");

        menuGame.add(panelMode);
        panelMode.setBounds(610, 0, 400, 140);

        btnStartTime1.setBackground(new java.awt.Color(62, 62, 62));
        btnStartTime1.setForeground(new java.awt.Color(102, 153, 0));
        btnStartTime1.setText("▶");
        btnStartTime1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnStartTime1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStartTime1.addActionListener(this::btnStartTime1ActionPerformed);
        menuGame.add(btnStartTime1);
        btnStartTime1.setBounds(60, 40, 50, 53);

        btnTimeSpeed1.setBackground(new java.awt.Color(62, 62, 62));
        btnTimeSpeed1.setForeground(new java.awt.Color(102, 153, 0));
        btnTimeSpeed1.setText("1");
        btnTimeSpeed1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnTimeSpeed1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTimeSpeed1.addActionListener(this::btnTimeSpeed1ActionPerformed);
        menuGame.add(btnTimeSpeed1);
        btnTimeSpeed1.setBounds(10, 90, 40, 40);

        btnTimeSpeed10.setBackground(new java.awt.Color(62, 62, 62));
        btnTimeSpeed10.setForeground(new java.awt.Color(102, 153, 0));
        btnTimeSpeed10.setText("10");
        btnTimeSpeed10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnTimeSpeed10.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTimeSpeed10.addActionListener(this::btnTimeSpeed10ActionPerformed);
        menuGame.add(btnTimeSpeed10);
        btnTimeSpeed10.setBounds(90, 90, 40, 40);

        btnTimeSpeed6.setBackground(new java.awt.Color(62, 62, 62));
        btnTimeSpeed6.setForeground(new java.awt.Color(102, 153, 0));
        btnTimeSpeed6.setText("5");
        btnTimeSpeed6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnTimeSpeed6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTimeSpeed6.addActionListener(this::btnTimeSpeed6ActionPerformed);
        menuGame.add(btnTimeSpeed6);
        btnTimeSpeed6.setBounds(50, 90, 40, 40);

        getContentPane().add(menuGame, java.awt.BorderLayout.PAGE_END);

        panelInfo.setBackground(new java.awt.Color(102, 102, 102));
        panelInfo.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 0, 0)));
        panelInfo.setLayout(new java.awt.BorderLayout());
        getContentPane().add(panelInfo, java.awt.BorderLayout.LINE_END);

        bMenuGame.setAlignmentX(1.0F);

        jMenu1.setText("File");

        MitemSave.setText("Save");
        MitemSave.addActionListener(this::MitemSaveActionPerformed);
        jMenu1.add(MitemSave);

        MitemLoad.setText("Load");
        MitemLoad.addActionListener(this::MitemLoadActionPerformed);
        jMenu1.add(MitemLoad);

        bMenuGame.add(jMenu1);

        jMenu2.setText("Edit");
        bMenuGame.add(jMenu2);

        jMenu3.setText("View");

        MitemGrid.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        MitemGrid.setText("Grid");
        MitemGrid.addActionListener(this::MitemGridActionPerformed);
        jMenu3.add(MitemGrid);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem1.setText("Information");
        jMenuItem1.addActionListener(this::jMenuItem1ActionPerformed);
        jMenu3.add(jMenuItem1);

        bMenuGame.add(jMenu3);

        setJMenuBar(bMenuGame);

        pack();
    }// </editor-fold>//GEN-END:initComponents

   //reanuda el timepo o inicia
    private void btnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPauseActionPerformed
        GLoop.pause();
        System.out.println(GLoop.getTimeDay().isPaused());
    }//GEN-LAST:event_btnPauseActionPerformed

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
        // comparar cual button del buttongrou esta seleccionado para saber que tipo de entidad es 
            int rngX = rng.nextInt(world.getColums()); //para determinar en que parte del mapa de manera aleatoria de generara
            int rngY = rng.nextInt(world.getRows());
        if (RbtnAnimals.isSelected()){      
            if (cboxAnimals.getSelectedIndex() == 0 ){
                return;
            }else{
                EManager.addAnimal(makeAnimal(cboxAnimals.getSelectedIndex(), rngX, rngY, EManager));
            }
        }
        if (RbtnResource.isSelected()){
            if (cboxResource.getSelectedIndex() == 0 ){
                return;
            }else{
                EManager.addResourse(makeResourse(cboxResource.getSelectedIndex(), rngX, rngY));
            }
        }
        repaint();
    }//GEN-LAST:event_btnGenerateActionPerformed

    private void btnInformationshowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInformationshowActionPerformed
        //intercambia la vista del grid
        panelInfo.setVisible(!panelInfo.isVisible());
        repaint();
    }//GEN-LAST:event_btnInformationshowActionPerformed

    private void MitemGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MitemGridActionPerformed
        // TODO add your handling code here:
       GP.dgrid=!GP.dgrid;
       repaint();
    }//GEN-LAST:event_MitemGridActionPerformed

    private void rbtnMGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnMGenerateActionPerformed
        // TODO add your handling code here:
        if(rbtnMGenerate.isSelected()){
            rbtnMGenerate.setBackground(new Color(0, 102, 102));
            rbtnMbuilt.setBackground(new Color(51,51,51));
            chansemode("Spawn");
            setTool(Tool.NONE);
            RbtnGNone.setSelected(true);
        }
    }//GEN-LAST:event_rbtnMGenerateActionPerformed

    private void rbtnMbuiltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnMbuiltActionPerformed
        //se cambia el modo del cardpanel
        if(rbtnMbuilt.isSelected()){
            rbtnMGenerate.setBackground(new Color(51,51,51));
            rbtnMbuilt.setBackground(new Color(43,35,58));
            chansemode("Built");
            setTool(Tool.NONE);
            rbtnBNone.setSelected(true);
        }
    }//GEN-LAST:event_rbtnMbuiltActionPerformed

    private void rbtnBWaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnBWaterActionPerformed
        setTool(Tool.WATER);
    }//GEN-LAST:event_rbtnBWaterActionPerformed

    private void rbtnBGlassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnBGlassActionPerformed
        //cambia solo la herramienta ya que en el mouser hace todo le comprobane   
        setTool(Tool.GRASS);
    }//GEN-LAST:event_rbtnBGlassActionPerformed

    private void rbtnBNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnBNoneActionPerformed
        setTool(Tool.NONE);
    }//GEN-LAST:event_rbtnBNoneActionPerformed

    private void MitemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MitemSaveActionPerformed
        // TODO add your handling code here:
    //    game.getEntityManager().saveToDatabase();
        
    }//GEN-LAST:event_MitemSaveActionPerformed

    private void MitemLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MitemLoadActionPerformed
        // TODO add your handling code here:
      //  GP.getEntityManager().loadFromDatabase();
    }//GEN-LAST:event_MitemLoadActionPerformed

    private void btnStartTime1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartTime1ActionPerformed
        // TODO add your handling code here:
        GLoop.resume();
    }//GEN-LAST:event_btnStartTime1ActionPerformed

    private void btnTimeSpeed1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimeSpeed1ActionPerformed
        // TODO add your handling code here:
        GLoop.setSpeed(1);
    }//GEN-LAST:event_btnTimeSpeed1ActionPerformed

    private void btnTimeSpeed10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimeSpeed10ActionPerformed
        // TODO add your handling code here:
        GLoop.setSpeed(10);
    }//GEN-LAST:event_btnTimeSpeed10ActionPerformed

    private void btnTimeSpeed6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimeSpeed6ActionPerformed
        // TODO add your handling code here:
        GLoop.setSpeed(5);
    }//GEN-LAST:event_btnTimeSpeed6ActionPerformed

    private void RbtnAnimalsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RbtnAnimalsActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_RbtnAnimalsActionPerformed

    private void RbtnGNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RbtnGNoneActionPerformed
        // TODO add your handling code here:
        setTool(Tool.NONE);
    }//GEN-LAST:event_RbtnGNoneActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        panelInfo.setVisible(!panelInfo.isVisible());
        repaint();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            GameLoop loop = new GameLoop();
            World world = new World();                    
            Entitymanager manager = new Entitymanager(world);
            InfoPanel infoP = new InfoPanel();
            loop.start();
            new ControlPanel(loop, world, manager,infoP).setVisible(true); 
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem MitemGrid;
    private javax.swing.JMenuItem MitemLoad;
    private javax.swing.JMenuItem MitemSave;
    private javax.swing.JRadioButton RbtnAnimals;
    private javax.swing.JRadioButton RbtnGNone;
    private javax.swing.JRadioButton RbtnResource;
    private javax.swing.JMenuBar bMenuGame;
    private javax.swing.ButtonGroup btnGMBuilts;
    private javax.swing.ButtonGroup btnGSpawn;
    private javax.swing.JToggleButton btnGenerate;
    private javax.swing.ButtonGroup btnGmodes;
    private javax.swing.JButton btnInformationshow;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnStartTime1;
    private javax.swing.JButton btnTimeSpeed1;
    private javax.swing.JButton btnTimeSpeed10;
    private javax.swing.JButton btnTimeSpeed6;
    private javax.swing.JComboBox<String> cboxAnimals;
    private javax.swing.JComboBox<String> cboxResource;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel menuGame;
    private javax.swing.JPanel panelGame;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JPanel panelMBuilt;
    private javax.swing.JPanel panelMSpawn;
    private javax.swing.JPanel panelMode;
    private javax.swing.JRadioButton rbtnBGlass;
    private javax.swing.JRadioButton rbtnBNone;
    private javax.swing.JRadioButton rbtnBWater;
    private javax.swing.JRadioButton rbtnMGenerate;
    private javax.swing.JRadioButton rbtnMbuilt;
    // End of variables declaration//GEN-END:variables
}
