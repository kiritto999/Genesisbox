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
import World.World;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
    
    private GamePanel GP;  
    private GameLoop GLoop;
    private World world;
    private Entitymanager EManager;
    private InfoPanel infoPanel;
    String[] animals = {"Elegir","Lummon", "Zyrox"};
    String[] resources = {"Elegir","Food","Nero","Zenthra",};
    
    
    
    
    private JLabel lblPos;
    private JLabel lblTileType;
    private JLabel lblEntityName;
    private JLabel lblHealth;
    private JLabel lblEnergy;
    private JLabel lblResourceName;
    private JLabel lblResourceAmount;
    public ControlPanel(GameLoop loop, World world, Entitymanager manager, InfoPanel infoPanel ) {
        initComponents();
        SizeAdapted();

        this.GLoop = loop;
        this.world = world;
        this.EManager = manager;
        this.infoPanel = infoPanel; 


        GP = new GamePanel(world, EManager, this.infoPanel);

        panelGame.setLayout(new BorderLayout());
        panelGame.add(GP, BorderLayout.CENTER);
        panelGame.revalidate();

        // usar panelInfo como contenedor del InfoPanel
        panelInfo.setLayout(new BorderLayout()); // colar absoluteloyaut para la acomodacion delso LBLs
        panelInfo.removeAll(); //por si acaso
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.add(this.infoPanel, BorderLayout.CENTER);     

        //tamaño del panel lateral
        panelInfo.setPreferredSize(new Dimension(250, 0));

        // tiempo
        lblTime.setText("");
        new javax.swing.Timer(100, e -> {
            lblTime.setText(GLoop.getTimeDay().getTimeString());
        }).start();
        
        new javax.swing.Timer(16, e -> GP.repaint()).start();

        // combobox
        for (String animal : animals) { 
            cboxAnimals.addItem(animal);
        }
        for (String resource : resources ){
            cboxResource.addItem(resource);
        }

        //la info empieza oculta
        panelInfo.setVisible(true);
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
    public Animal makeAnimal(int tipo, int x, int y, Entitymanager manager) {
        switch (tipo) {
            case 1:
                return new Lummon(x, y, EManager);
            case 2:
                return new Zyrox(x, y, EManager);
            default:
                return null;
        }
    }
    //comparativa del cbox para saber el recurso
    public Resource makeResourse(int tipo, int x, int y) {
        switch (tipo) {
            case 1:
                return new Food(x, y);
            case 2:
                return new Nero(x, y);
            case 3:
                return new Tree(x, y);
            default:
                return null;
        }
    }

    
    public void Information() {

    panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));

    //POSICION -----------------------------------------
    JPanel panelPos = new JPanel();
    panelPos.setLayout(new BoxLayout(panelPos, BoxLayout.Y_AXIS));

    JLabel titlePos = new JLabel("POSICIÓN");
    lblPos = new JLabel("0,0");

    panelPos.add(titlePos);
    panelPos.add(lblPos);

    panelPos.setBorder(BorderFactory.createTitledBorder("Posición"));
    panelInfo.add(panelPos); 


    // TILE -----------------------------------------
    JPanel panelTile = new JPanel();
    panelTile.setLayout(new BoxLayout(panelTile, BoxLayout.Y_AXIS));

    JLabel titleTile = new JLabel("TILE");
    lblTileType = new JLabel("-");

    panelTile.add(titleTile);
    panelTile.add(lblTileType);

    panelTile.setBorder(BorderFactory.createTitledBorder("Tile"));
    panelInfo.add(panelTile);


    // ENTITY -----------------------------------------
    JPanel panelEntity = new JPanel();
    panelEntity.setLayout(new BoxLayout(panelEntity, BoxLayout.Y_AXIS));

    JLabel titleEntity = new JLabel("ENTITY");

    lblEntityName = new JLabel("-");
    lblHealth = new JLabel("-");
    lblEnergy = new JLabel("-");

    panelEntity.add(titleEntity);
    panelEntity.add(lblEntityName);
    panelEntity.add(lblHealth);
    panelEntity.add(lblEnergy);

    panelEntity.setBorder(BorderFactory.createTitledBorder("Entidad"));
    panelInfo.add(panelEntity);


    //RESOURCE -----------------------------------------
    JPanel panelResource = new JPanel();
    panelResource.setLayout(new BoxLayout(panelResource, BoxLayout.Y_AXIS));

    JLabel titleRes = new JLabel("RESOURCE");

    lblResourceName = new JLabel("-");
    lblResourceAmount = new JLabel("-");

    panelResource.add(titleRes);
    panelResource.add(lblResourceName);
    panelResource.add(lblResourceAmount);

    panelResource.setBorder(BorderFactory.createTitledBorder("Recurso"));
    panelInfo.add(panelResource);
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGSpawn = new javax.swing.ButtonGroup();
        jTextField1 = new javax.swing.JTextField();
        panelGame = new javax.swing.JPanel();
        menuGame = new javax.swing.JPanel();
        btnStartTime = new javax.swing.JButton();
        btnPause = new javax.swing.JButton();
        lblTime = new javax.swing.JLabel();
        cboxAnimals = new javax.swing.JComboBox<>();
        btnGenerate = new javax.swing.JToggleButton();
        RbtnAnimals = new javax.swing.JRadioButton();
        cboxResource = new javax.swing.JComboBox<>();
        RbtnResource = new javax.swing.JRadioButton();
        btnGrid = new javax.swing.JButton();
        panelInfo = new javax.swing.JPanel();
        bMenuGame = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        jTextField1.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Aqualix");
        setFocusCycleRoot(false);

        panelGame.setBackground(new java.awt.Color(0, 0, 0));
        panelGame.setPreferredSize(new java.awt.Dimension(0, 320));
        panelGame.setLayout(new java.awt.BorderLayout());
        getContentPane().add(panelGame, java.awt.BorderLayout.CENTER);

        menuGame.setBackground(new java.awt.Color(102, 102, 102));
        menuGame.setPreferredSize(new java.awt.Dimension(0, 137));
        menuGame.setLayout(null);

        btnStartTime.setText("▶");
        btnStartTime.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnStartTime.addActionListener(this::btnStartTimeActionPerformed);
        menuGame.add(btnStartTime);
        btnStartTime.setBounds(110, 0, 50, 53);

        btnPause.setText("⏸");
        btnPause.addActionListener(this::btnPauseActionPerformed);
        menuGame.add(btnPause);
        btnPause.setBounds(50, 0, 50, 53);

        lblTime.setText("Time");
        menuGame.add(lblTime);
        lblTime.setBounds(0, 0, 67, 26);

        menuGame.add(cboxAnimals);
        cboxAnimals.setBounds(540, 30, 76, 26);

        btnGenerate.setText("Generate");
        btnGenerate.addActionListener(this::btnGenerateActionPerformed);
        menuGame.add(btnGenerate);
        btnGenerate.setBounds(435, 13, 81, 27);

        RbtnAnimals.setBackground(new java.awt.Color(102, 102, 102));
        btnGSpawn.add(RbtnAnimals);
        RbtnAnimals.setText("Animals");
        menuGame.add(RbtnAnimals);
        RbtnAnimals.setBounds(530, 10, 110, 21);

        menuGame.add(cboxResource);
        cboxResource.setBounds(690, 30, 76, 26);

        RbtnResource.setBackground(new java.awt.Color(102, 102, 102));
        btnGSpawn.add(RbtnResource);
        RbtnResource.setText("Resources");
        menuGame.add(RbtnResource);
        RbtnResource.setBounds(670, 10, 110, 21);

        btnGrid.setText("Grid");
        btnGrid.addActionListener(this::btnGridActionPerformed);
        menuGame.add(btnGrid);
        btnGrid.setBounds(0, 60, 79, 27);

        getContentPane().add(menuGame, java.awt.BorderLayout.PAGE_END);

        panelInfo.setBackground(new java.awt.Color(102, 102, 102));
        panelInfo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(panelInfo, java.awt.BorderLayout.LINE_END);

        bMenuGame.setAlignmentX(1.0F);

        jMenu1.setText("File");
        bMenuGame.add(jMenu1);

        jMenu2.setText("Edit");
        bMenuGame.add(jMenu2);

        setJMenuBar(bMenuGame);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    //pausa el timepo
    private void btnStartTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartTimeActionPerformed
        GLoop.getTimeDay().setPaused(false);
        System.out.println(GLoop.getTimeDay().isPaused());
    }//GEN-LAST:event_btnStartTimeActionPerformed
    //reanuda el timepo o inicia
    private void btnPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPauseActionPerformed
        GLoop.getTimeDay().setPaused(true);
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
                EManager.getAnimals().add(makeAnimal(cboxAnimals.getSelectedIndex(), rngX, rngY,EManager));
            }
        }
        if (RbtnResource.isSelected()){
            if (cboxResource.getSelectedIndex() == 0 ){
                return;
            }else{

                EManager.getResources().add(makeResourse(cboxResource.getSelectedIndex(), rngX, rngY));
            }
        }
        repaint();
    }//GEN-LAST:event_btnGenerateActionPerformed

    private void btnGridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGridActionPerformed
        //intercambia la vista del grid
        GP.dgrid=!GP.dgrid;
        panelInfo.setVisible(GP.dgrid);
        repaint();
    }//GEN-LAST:event_btnGridActionPerformed

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
            loop.setEntitymanager(manager);
            loop.setWorld(world);
            loop.start();
            new ControlPanel(loop, world, manager,infoP).setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton RbtnAnimals;
    private javax.swing.JRadioButton RbtnResource;
    private javax.swing.JMenuBar bMenuGame;
    private javax.swing.ButtonGroup btnGSpawn;
    private javax.swing.JToggleButton btnGenerate;
    private javax.swing.JButton btnGrid;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnStartTime;
    private javax.swing.JComboBox<String> cboxAnimals;
    private javax.swing.JComboBox<String> cboxResource;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel menuGame;
    private javax.swing.JPanel panelGame;
    private javax.swing.JPanel panelInfo;
    // End of variables declaration//GEN-END:variables
}
