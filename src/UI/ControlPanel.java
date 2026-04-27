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
    
    GamePanel GP;  
    private GameLoop GLoop;
    private World world;
    private Entitymanager EManager;
    String[] animals = {"Elegir","Lummon", "Zyrox"};
    String[] resources = {"Elegir","Food","Nero","Zenthra",};
    
    public ControlPanel(GameLoop loop, World world, Entitymanager manager) {
        initComponents();
        SizeAdapted();
        //para que todo sea un solo create
        this.GLoop = loop;
        this.world = world;
        this.EManager = manager;

        GP = new GamePanel(world,EManager);

        //agrega el panel visible con el redujo
        panelGame.setLayout(new BorderLayout());
        panelGame.add(GP, BorderLayout.CENTER);
        panelGame.revalidate();
        
        //va modificando el tiempo 
        lblTime.setText("");
        new javax.swing.Timer(100, new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                lblTime.setText(GLoop.getTimeDay().getTimeString());
            }
        }).start();
        
        for (String animal : animals) {
            cboxAnimals.addItem(animal);
        }
        
        for (String resource : resources ){
            cboxResource.addItem(resource);
        }
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
    public Animal makeAnimal(int tipo, int x, int y) {
        switch (tipo) {
            case 1:
                return new Lummon(x, y);
            case 2:
                return new Zyrox(x, y);
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

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGSpawn = new javax.swing.ButtonGroup();
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
        bMenuGame = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Aqualix");

        panelGame.setBackground(new java.awt.Color(0, 0, 0));
        panelGame.setPreferredSize(new java.awt.Dimension(0, 320));
        panelGame.setLayout(null);
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

        getContentPane().add(menuGame, java.awt.BorderLayout.PAGE_END);

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
        if (RbtnAnimals.isSelected()){      
            if (cboxAnimals.getSelectedIndex() == 0 ){
                return;
            }else{
                EManager.getAnimals().add(makeAnimal(cboxAnimals.getSelectedIndex(), 5, 5));
            }
        }
        if (RbtnResource.isSelected()){
            if (cboxResource.getSelectedIndex() == 0 ){
                return;
            }else{
                int rngX = rng.nextInt(world.getColums());
                int rngY = rng.nextInt(world.getRows());
                EManager.getResources().add(makeResourse(cboxResource.getSelectedIndex(), rngX, rngY));
                System.out.println( cboxResource.getSelectedIndex() +"generada");
            }
        }
        repaint();
    }//GEN-LAST:event_btnGenerateActionPerformed

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
            loop.start();
            new ControlPanel(loop, world, manager).setVisible(true); 
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton RbtnAnimals;
    private javax.swing.JRadioButton RbtnResource;
    private javax.swing.JMenuBar bMenuGame;
    private javax.swing.ButtonGroup btnGSpawn;
    private javax.swing.JToggleButton btnGenerate;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnStartTime;
    private javax.swing.JComboBox<String> cboxAnimals;
    private javax.swing.JComboBox<String> cboxResource;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel menuGame;
    private javax.swing.JPanel panelGame;
    // End of variables declaration//GEN-END:variables
}
