/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package UI;

import Database.SaveManager;
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
import Utils.*;
import java.awt.Color;
import Game.Game;
import World.Tile;
import java.awt.Image;
import java.util.Hashtable;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
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
    private TimeDay time;
    private Entitymanager EManager;
    private InfoPanel infoPanel;
    private Mouser mouser;
    private Camera camera;
    private Mode currentMode = Mode.SPAWN; 
    private Tool currentBuildTool = Tool.NONE;
    private SaveManager SV;
    private int selectedGroundVariant = 0;
    
    String[] animals = {"Elegir","Lummon", "Zyrox"};
    String[] resources = {"Elegir","Food","Nero","Zenthra",};
    
    public ControlPanel(GameLoop loop, World world, Entitymanager manager, InfoPanel infoPanel,TimeDay time,SaveManager sv ) { 
        initComponents();
        SizeAdapted();
        SetTextSlider();
        this.GLoop = loop;
        this.world = world;
        this.EManager = manager;
        this.infoPanel = infoPanel; 
        this.time = time;
        this.SV = sv;
        
        camera = new Camera();
        GP = new GamePanel(world, EManager, this.infoPanel, camera,time);
        mouser = new Mouser(camera, GP, world,EManager,this);
        infoPanel.setGamePanel(GP);
        //se agreg el redibujado 
        GLoop.setGamePanel(GP);      
        
        Image icon = new ImageIcon(
                getClass().getResource("/resources/Gifs/Aqualix.gif")
        ).getImage();
        
        // ── Botón rayo ──
        javax.swing.JButton btnRayo = new javax.swing.JButton("⚡ Rayo");
        btnRayo.setBackground(new Color(20, 10, 60));
        btnRayo.setForeground(new Color(180, 100, 255));
        btnRayo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRayo.setBounds(340, 40, 100, 30);
        btnRayo.addActionListener(e -> setTool(Tool.RAYO));
        menuGame.add(btnRayo);
        
        // ── Label cooldown ──
        javax.swing.JLabel lblCooldown = new javax.swing.JLabel("Listo");
        lblCooldown.setForeground(new Color(100, 255, 100));
        lblCooldown.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        lblCooldown.setBounds(340, 75, 100, 20);
        menuGame.add(lblCooldown);

        // ── Timer que actualiza el label ──
        new javax.swing.Timer(200, e -> {
            LightningEffect ray = GP.getLightning();
            if (ray.puedeUsarse()) {
                lblCooldown.setForeground(new Color(100, 255, 100));
                lblCooldown.setText("✔ Listo");
                btnRayo.setEnabled(true);
            } else {
                int seg = ray.getCooldownRestante();
                lblCooldown.setForeground(new Color(255, 80, 80));
                lblCooldown.setText("⏳ " + seg + "s");
                btnRayo.setEnabled(false);
            }
        }).start();

        setIconImage(icon);
        
        panelGame.setLayout(new BorderLayout());
        panelGame.add(GP, BorderLayout.CENTER);
        GP.setOpaque(false);
        panelGame.revalidate();

        // usar panelInfo como contenedor del InfoPanel y crearlo con scroll
        panelInfo.removeAll();
        panelInfo.setLayout(new BorderLayout());
        
        JScrollPane infoScroll = new JScrollPane(infoPanel); //crear el scroll
        infoScroll.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS   //se activa el scroll en vertical
        );
        infoScroll.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER   // desactiva scroll horizontal
        );

        infoScroll.getVerticalScrollBar().setPreferredSize(
            new Dimension(0, 0)         // hace invisible la barra vertical
        );

        infoScroll.getViewport().setBackground(
            new Color(28, 30, 34)
        );
        infoScroll.getVerticalScrollBar().setUnitIncrement(24);     // aumenta la velocidad/sensibilidad del scroll

        infoScroll.setBorder(null);
        infoScroll.getVerticalScrollBar().setBackground(
            new Color(40,40,40)
        );
        panelInfo.add(infoScroll, BorderLayout.CENTER);        // agregamos el scroll al panel lateral
        panelInfo.revalidate();
        panelInfo.repaint();

        
        //tamaño del panel lateral
        panelInfo.setPreferredSize(new Dimension(340, 900));

        // tiempo
        lblTime.setText("");
        new javax.swing.Timer(100, e -> {
            lblTime.setText(GLoop.getTimeDay().getTimeString());
            updateLblD();
        }).start();

        
       

        //la info empieza oculta
        panelInfo.setVisible(false);
        
        //modos
        rbtnMCreatures.setSelected(true);
        panelMode.add(panelMCreature,"Spawn");
        panelMode.add(panelMBuilt,"Built");
        panelMode.add(panelMResources,"Resources");
        
        //controla la velocidad del timepo
        
        SliderSpeed.addChangeListener(e -> {
            int value = SliderSpeed.getValue();
            double speed = 1;
            switch(value){
                case 0:
                    speed = 0.5;
                    break;
                case 1:
                    speed = 1;
                    break;
                case 2:
                    speed = 5;
                    break;
                case 3:
                    speed = 20;
                    break;
                case 4:
                    speed = 100;
                    break;
                case 5:
                    speed = 1000;
                    break;
            }
            GLoop.setSpeed(speed);
            lblSpeed.setText("Speed: x" + speed);
        });
        
    }

    public GamePanel getGamePanel() {
        return GP;
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
                return new Blupys(x, y);
            case 2:
                return new Nero(x, y);
            case 3:
                return new Zenthra(x, y);
            default:
                return null;
        }
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
    
    private void SetTextSlider(){
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(0, new JLabel("0.5x"));
        labels.put(1, new JLabel("1x"));
        labels.put(2, new JLabel("5x"));
        labels.put(3, new JLabel("20x"));
        labels.put(4, new JLabel("100x"));
        labels.put(5, new JLabel("1000x"));
        for (JLabel label : labels.values()) {
            label.setForeground(Color.WHITE);
        }
        SliderSpeed.setLabelTable(labels);
        SliderSpeed.setPaintLabels(true);
    }
    
    private void updateLblD(){

        lblDDays.setText("Day: " + time.getDay());
        lblDYear.setText("Year: " + time.getYear());

        lblDAnimals.setText("Creatures: " + EManager.getAnimals().size());

        lblDResources.setText("Recursos: " + EManager.getResources().size());
    }
    
    private void resetBorders() {

    // CREATURES
    TbtnCZyrox.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );
    TbtnCLummon.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    // RESOURCES
    TbtnRZen.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnRZen2.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnRZen3.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnRZen4.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnRNero.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );
    // BUILTS
    TbtnBGrass.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnBGrass1.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnBGrass2.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnBGrass3.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnBGrass4.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnBGrass5.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnBGrass6.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnBGrass7.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnBGrass8.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnBGrass9.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );

    TbtnBWater.setBorder(
        new javax.swing.border.LineBorder(java.awt.Color.BLACK, 3)
    );
}

    
  
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField1 = new javax.swing.JTextField();
        btnGmodes = new javax.swing.ButtonGroup();
        btnGMBuilts = new javax.swing.ButtonGroup();
        btnGCreatures = new javax.swing.ButtonGroup();
        btnGResources = new javax.swing.ButtonGroup();
        panelGame = new javax.swing.JPanel();
        menuGame = new javax.swing.JPanel();
        btnPause = new javax.swing.JButton();
        lblTime = new javax.swing.JLabel();
        btnInformationshow = new javax.swing.JButton();
        rbtnMResources = new javax.swing.JRadioButton();
        rbtnMbuilt = new javax.swing.JRadioButton();
        panelMode = new javax.swing.JPanel();
        panelMCreature = new javax.swing.JPanel();
        TbtnCZyrox = new javax.swing.JToggleButton();
        TbtnCLummon = new javax.swing.JToggleButton();
        panelMResources = new javax.swing.JPanel();
        TbtnRZen = new javax.swing.JToggleButton();
        TbtnRZen2 = new javax.swing.JToggleButton();
        TbtnRZen3 = new javax.swing.JToggleButton();
        TbtnRZen4 = new javax.swing.JToggleButton();
        TbtnRNero = new javax.swing.JToggleButton();
        TbtnRBlupys = new javax.swing.JToggleButton();
        panelMBuilt = new javax.swing.JPanel();
        TbtnBGrass = new javax.swing.JToggleButton();
        TbtnBGrass1 = new javax.swing.JToggleButton();
        TbtnBGrass2 = new javax.swing.JToggleButton();
        TbtnBGrass3 = new javax.swing.JToggleButton();
        TbtnBGrass4 = new javax.swing.JToggleButton();
        TbtnBGrass5 = new javax.swing.JToggleButton();
        TbtnBGrass6 = new javax.swing.JToggleButton();
        TbtnBGrass7 = new javax.swing.JToggleButton();
        TbtnBGrass8 = new javax.swing.JToggleButton();
        TbtnBGrass9 = new javax.swing.JToggleButton();
        TbtnBWater = new javax.swing.JToggleButton();
        btnStartTime1 = new javax.swing.JButton();
        SliderSpeed = new javax.swing.JSlider();
        lblSpeed = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblDYear = new javax.swing.JLabel();
        lblDDays = new javax.swing.JLabel();
        lblDAnimals = new javax.swing.JLabel();
        lblDResources = new javax.swing.JLabel();
        btnGenerate = new javax.swing.JToggleButton();
        rbtnMCreatures = new javax.swing.JRadioButton();
        rbtnMNone = new javax.swing.JRadioButton();
        panelInfo = new javax.swing.JPanel();
        bMenuGame = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        MitemSave = new javax.swing.JMenuItem();
        MitemLoad = new javax.swing.JMenuItem();
        MitemExitManu = new javax.swing.JMenuItem();
        MitemExit = new javax.swing.JMenuItem();
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
        btnPause.setBounds(10, 40, 50, 30);

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
        btnInformationshow.setBounds(220, 10, 110, 22);

        rbtnMResources.setBackground(new java.awt.Color(51, 51, 51));
        btnGmodes.add(rbtnMResources);
        rbtnMResources.setForeground(new java.awt.Color(255, 255, 255));
        rbtnMResources.setText("Resources");
        rbtnMResources.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnMResources.addActionListener(this::rbtnMResourcesActionPerformed);
        menuGame.add(rbtnMResources);
        rbtnMResources.setBounds(500, 40, 110, 21);

        rbtnMbuilt.setBackground(new java.awt.Color(51, 51, 51));
        btnGmodes.add(rbtnMbuilt);
        rbtnMbuilt.setForeground(new java.awt.Color(255, 255, 255));
        rbtnMbuilt.setText("Constructor");
        rbtnMbuilt.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnMbuilt.addActionListener(this::rbtnMbuiltActionPerformed);
        menuGame.add(rbtnMbuilt);
        rbtnMbuilt.setBounds(500, 60, 110, 20);

        panelMode.setLayout(new java.awt.CardLayout());

        panelMCreature.setBackground(new java.awt.Color(0, 102, 102));

        btnGCreatures.add(TbtnCZyrox);
        TbtnCZyrox.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Mzyrox_idle.png"))); // NOI18N
        TbtnCZyrox.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnCZyrox.setOpaque(true);
        TbtnCZyrox.addActionListener(this::TbtnCZyroxActionPerformed);

        btnGCreatures.add(TbtnCLummon);
        TbtnCLummon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Mlummon_idle.png"))); // NOI18N
        TbtnCLummon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnCLummon.setOpaque(true);
        TbtnCLummon.addActionListener(this::TbtnCLummonActionPerformed);

        javax.swing.GroupLayout panelMCreatureLayout = new javax.swing.GroupLayout(panelMCreature);
        panelMCreature.setLayout(panelMCreatureLayout);
        panelMCreatureLayout.setHorizontalGroup(
            panelMCreatureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMCreatureLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(TbtnCZyrox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TbtnCLummon, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(217, Short.MAX_VALUE))
        );
        panelMCreatureLayout.setVerticalGroup(
            panelMCreatureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMCreatureLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelMCreatureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TbtnCLummon)
                    .addComponent(TbtnCZyrox))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        panelMode.add(panelMCreature, "card3");

        panelMResources.setBackground(new java.awt.Color(51, 51, 0));

        btnGResources.add(TbtnRZen);
        TbtnRZen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/MINI_zenthra_sapling.png"))); // NOI18N
        TbtnRZen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnRZen.setOpaque(true);
        TbtnRZen.addActionListener(this::TbtnRZenActionPerformed);

        btnGResources.add(TbtnRZen2);
        TbtnRZen2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/MINI_zenthra_joven.png"))); // NOI18N
        TbtnRZen2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnRZen2.setEnabled(false);
        TbtnRZen2.setOpaque(true);
        TbtnRZen2.addActionListener(this::TbtnRZen2ActionPerformed);

        btnGResources.add(TbtnRZen3);
        TbtnRZen3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/MINI_zenthra_maduro.png"))); // NOI18N
        TbtnRZen3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnRZen3.setEnabled(false);
        TbtnRZen3.setOpaque(true);
        TbtnRZen3.addActionListener(this::TbtnRZen3ActionPerformed);

        btnGResources.add(TbtnRZen4);
        TbtnRZen4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/MINI_zenthra_old.png"))); // NOI18N
        TbtnRZen4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnRZen4.setEnabled(false);
        TbtnRZen4.setOpaque(true);
        TbtnRZen4.addActionListener(this::TbtnRZen4ActionPerformed);

        btnGResources.add(TbtnRNero);
        TbtnRNero.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/MINI_Nero_sprite.png"))); // NOI18N
        TbtnRNero.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnRNero.setOpaque(true);
        TbtnRNero.addActionListener(this::TbtnRNeroActionPerformed);

        btnGResources.add(TbtnRBlupys);
        TbtnRBlupys.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/MINI_blupys_young.png"))); // NOI18N
        TbtnRBlupys.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnRBlupys.setOpaque(true);
        TbtnRBlupys.addActionListener(this::TbtnRBlupysActionPerformed);

        javax.swing.GroupLayout panelMResourcesLayout = new javax.swing.GroupLayout(panelMResources);
        panelMResources.setLayout(panelMResourcesLayout);
        panelMResourcesLayout.setHorizontalGroup(
            panelMResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMResourcesLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelMResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelMResourcesLayout.createSequentialGroup()
                        .addComponent(TbtnRBlupys)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelMResourcesLayout.createSequentialGroup()
                        .addComponent(TbtnRZen)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TbtnRZen2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TbtnRZen3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TbtnRZen4)
                        .addGap(61, 61, 61)
                        .addComponent(TbtnRNero)
                        .addGap(15, 15, 15))))
        );
        panelMResourcesLayout.setVerticalGroup(
            panelMResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMResourcesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMResourcesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TbtnRZen4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TbtnRZen2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TbtnRZen3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TbtnRZen, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(TbtnRNero, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(TbtnRBlupys, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelMode.add(panelMResources, "card4");

        panelMBuilt.setBackground(new java.awt.Color(43, 35, 58));

        btnGMBuilts.add(TbtnBGrass);
        TbtnBGrass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Dirt1.png"))); // NOI18N
        TbtnBGrass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnBGrass.setOpaque(true);
        TbtnBGrass.addActionListener(this::TbtnBGrassActionPerformed);
        panelMBuilt.add(TbtnBGrass);

        btnGMBuilts.add(TbtnBGrass1);
        TbtnBGrass1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Dirt2.png"))); // NOI18N
        TbtnBGrass1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnBGrass1.setOpaque(true);
        TbtnBGrass1.addActionListener(this::TbtnBGrass1ActionPerformed);
        panelMBuilt.add(TbtnBGrass1);

        btnGMBuilts.add(TbtnBGrass2);
        TbtnBGrass2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Dirt3.png"))); // NOI18N
        TbtnBGrass2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnBGrass2.setOpaque(true);
        TbtnBGrass2.addActionListener(this::TbtnBGrass2ActionPerformed);
        panelMBuilt.add(TbtnBGrass2);

        btnGMBuilts.add(TbtnBGrass3);
        TbtnBGrass3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Dirt4.png"))); // NOI18N
        TbtnBGrass3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnBGrass3.setOpaque(true);
        TbtnBGrass3.addActionListener(this::TbtnBGrass3ActionPerformed);
        panelMBuilt.add(TbtnBGrass3);

        btnGMBuilts.add(TbtnBGrass4);
        TbtnBGrass4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Plain5.png"))); // NOI18N
        TbtnBGrass4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnBGrass4.setOpaque(true);
        TbtnBGrass4.addActionListener(this::TbtnBGrass4ActionPerformed);
        panelMBuilt.add(TbtnBGrass4);

        btnGMBuilts.add(TbtnBGrass5);
        TbtnBGrass5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Plain6.png"))); // NOI18N
        TbtnBGrass5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnBGrass5.setOpaque(true);
        TbtnBGrass5.addActionListener(this::TbtnBGrass5ActionPerformed);
        panelMBuilt.add(TbtnBGrass5);

        btnGMBuilts.add(TbtnBGrass6);
        TbtnBGrass6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Plain7.png"))); // NOI18N
        TbtnBGrass6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnBGrass6.setOpaque(true);
        TbtnBGrass6.addActionListener(this::TbtnBGrass6ActionPerformed);
        panelMBuilt.add(TbtnBGrass6);

        btnGMBuilts.add(TbtnBGrass7);
        TbtnBGrass7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Plain8.png"))); // NOI18N
        TbtnBGrass7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnBGrass7.setOpaque(true);
        TbtnBGrass7.addActionListener(this::TbtnBGrass7ActionPerformed);
        panelMBuilt.add(TbtnBGrass7);

        btnGMBuilts.add(TbtnBGrass8);
        TbtnBGrass8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Rock8.png"))); // NOI18N
        TbtnBGrass8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnBGrass8.setOpaque(true);
        TbtnBGrass8.addActionListener(this::TbtnBGrass8ActionPerformed);
        panelMBuilt.add(TbtnBGrass8);

        btnGMBuilts.add(TbtnBGrass9);
        TbtnBGrass9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sprites/Rock9.png"))); // NOI18N
        TbtnBGrass9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnBGrass9.setOpaque(true);
        TbtnBGrass9.addActionListener(this::TbtnBGrass9ActionPerformed);
        panelMBuilt.add(TbtnBGrass9);

        btnGMBuilts.add(TbtnBWater);
        TbtnBWater.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/Gifs/WaterV1.png"))); // NOI18N
        TbtnBWater.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        TbtnBWater.setOpaque(true);
        TbtnBWater.addActionListener(this::TbtnBWaterActionPerformed);
        panelMBuilt.add(TbtnBWater);

        panelMode.add(panelMBuilt, "card2");

        menuGame.add(panelMode);
        panelMode.setBounds(610, 0, 400, 140);

        btnStartTime1.setBackground(new java.awt.Color(62, 62, 62));
        btnStartTime1.setForeground(new java.awt.Color(102, 153, 0));
        btnStartTime1.setText("▶");
        btnStartTime1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnStartTime1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStartTime1.addActionListener(this::btnStartTime1ActionPerformed);
        menuGame.add(btnStartTime1);
        btnStartTime1.setBounds(60, 40, 50, 30);

        SliderSpeed.setBackground(new java.awt.Color(51, 51, 51));
        SliderSpeed.setForeground(new java.awt.Color(255, 255, 255));
        SliderSpeed.setMajorTickSpacing(1);
        SliderSpeed.setMaximum(5);
        SliderSpeed.setPaintLabels(true);
        SliderSpeed.setPaintTicks(true);
        SliderSpeed.setPaintTrack(false);
        SliderSpeed.setSnapToTicks(true);
        SliderSpeed.setToolTipText("");
        SliderSpeed.setValue(1);
        SliderSpeed.setName(""); // NOI18N
        menuGame.add(SliderSpeed);
        SliderSpeed.setBounds(10, 80, 190, 50);

        lblSpeed.setForeground(new java.awt.Color(255, 255, 255));
        lblSpeed.setText("Speed");
        menuGame.add(lblSpeed);
        lblSpeed.setBounds(90, 0, 140, 40);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 153)), "Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        lblDYear.setForeground(new java.awt.Color(255, 255, 255));
        lblDYear.setText("Year:");

        lblDDays.setForeground(new java.awt.Color(255, 255, 255));
        lblDDays.setText("Dya:");

        lblDAnimals.setForeground(new java.awt.Color(255, 255, 255));
        lblDAnimals.setText("Creatures");

        lblDResources.setForeground(new java.awt.Color(255, 255, 255));
        lblDResources.setText("Resources:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblDYear)
                        .addGap(46, 46, 46)
                        .addComponent(lblDDays))
                    .addComponent(lblDResources)
                    .addComponent(lblDAnimals))
                .addContainerGap(239, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDYear)
                    .addComponent(lblDDays))
                .addGap(18, 18, 18)
                .addComponent(lblDAnimals)
                .addGap(18, 18, 18)
                .addComponent(lblDResources)
                .addGap(0, 13, Short.MAX_VALUE))
        );

        menuGame.add(jPanel1);
        jPanel1.setBounds(1290, 10, 350, 120);

        btnGenerate.setBackground(new java.awt.Color(0, 153, 102));
        btnGenerate.setForeground(new java.awt.Color(255, 255, 255));
        btnGenerate.setText("Generate");
        btnGenerate.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnGenerate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnGenerate.addActionListener(this::btnGenerateActionPerformed);
        menuGame.add(btnGenerate);
        btnGenerate.setBounds(510, 90, 84, 38);

        rbtnMCreatures.setBackground(new java.awt.Color(0, 102, 102));
        btnGmodes.add(rbtnMCreatures);
        rbtnMCreatures.setForeground(new java.awt.Color(255, 255, 255));
        rbtnMCreatures.setText("Creatures");
        rbtnMCreatures.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnMCreatures.addActionListener(this::rbtnMCreaturesActionPerformed);
        menuGame.add(rbtnMCreatures);
        rbtnMCreatures.setBounds(500, 20, 110, 21);

        rbtnMNone.setBackground(new java.awt.Color(51, 51, 51));
        btnGmodes.add(rbtnMNone);
        rbtnMNone.setForeground(new java.awt.Color(255, 255, 255));
        rbtnMNone.setText("None");
        rbtnMNone.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rbtnMNone.addActionListener(this::rbtnMNoneActionPerformed);
        menuGame.add(rbtnMNone);
        rbtnMNone.setBounds(500, 0, 110, 21);

        getContentPane().add(menuGame, java.awt.BorderLayout.PAGE_END);

        panelInfo.setBackground(new java.awt.Color(102, 102, 102));
        panelInfo.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 0, 0)));
        panelInfo.setLayout(new java.awt.BorderLayout());
        getContentPane().add(panelInfo, java.awt.BorderLayout.LINE_END);

        bMenuGame.setToolTipText("");
        bMenuGame.setAlignmentX(1.0F);

        jMenu1.setText("File");

        MitemSave.setText("Save");
        MitemSave.addActionListener(this::MitemSaveActionPerformed);
        jMenu1.add(MitemSave);

        MitemLoad.setText("Load");
        MitemLoad.addActionListener(this::MitemLoadActionPerformed);
        jMenu1.add(MitemLoad);

        MitemExitManu.setText("Exit menu");
        MitemExitManu.addActionListener(this::MitemExitManuActionPerformed);
        jMenu1.add(MitemExitManu);

        MitemExit.setText("Exit");
        MitemExit.addActionListener(this::MitemExitActionPerformed);
        jMenu1.add(MitemExit);

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

        Random random = new Random();

        Tool tool = mouser.getCurrentTool();

        for (int i = 0; i < 50; i++) {

            int x = random.nextInt(world.getColums()* GP.getUNIT_SIZE());
            int y = random.nextInt(world.getRows() * GP.getUNIT_SIZE());

            switch (tool) {

                // ===== CREATURES =====

                case Zyrox:
                    EManager.addAnimal(
                        new Zyrox(x, y, EManager)
                    );
                    break;

                case Lummon:
                    EManager.addAnimal(
                        new Lummon(x, y, EManager)
                    );
                    break;

                // ===== RESOURCES =====

                case Nero:
                    EManager.addResourse(
                        new Nero(x, y)
                    );
                    break;

                case Zethar:
                    EManager.addResourse(
                        new Zenthra(x, y)
                    );
                    break;

                case Blupys:
                    EManager.addResourse(
                        new Blupys(x, y)
                    );
                    break;

                default:
                    System.out.println("No hay entidad seleccionada");
                    break;
            }
        }
        GP.repaint();
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

    private void rbtnMResourcesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnMResourcesActionPerformed
        // TODO add your handling code here:
        if(rbtnMResources.isSelected()){
            panelMode.setOpaque(false);
            rbtnMCreatures.setBackground(new Color(51,51,51));
            rbtnMResources.setBackground(new Color(51,51,0));
            rbtnMbuilt.setBackground(new Color(51,51,51));
            rbtnMNone.setBackground(new Color(51,51,51));
            chansemode("Resources");
            setTool(Tool.NONE);
        }


    }//GEN-LAST:event_rbtnMResourcesActionPerformed

    private void rbtnMbuiltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnMbuiltActionPerformed
        //se cambia el modo del cardpanel
        if(rbtnMbuilt.isSelected()){
            rbtnMCreatures.setBackground(new Color(51,51,51));
            rbtnMResources.setBackground(new Color(51,51,51));
            rbtnMbuilt.setBackground(new Color(43,35,58));
            rbtnMNone.setBackground(new Color(51,51,51));
            chansemode("Built");
            setTool(Tool.NONE);
        }
    }//GEN-LAST:event_rbtnMbuiltActionPerformed

    private void MitemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MitemSaveActionPerformed
        // TODO add your handling code here:
        SV.saveGame(world,time,EManager);
        
    }//GEN-LAST:event_MitemSaveActionPerformed

    private void MitemLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MitemLoadActionPerformed
        // TODO add your handling code here:
        SV.loadGame(world,time,EManager);
    }//GEN-LAST:event_MitemLoadActionPerformed

    private void btnStartTime1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartTime1ActionPerformed
        // TODO add your handling code here:
        GLoop.resume();
    }//GEN-LAST:event_btnStartTime1ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        panelInfo.setVisible(!panelInfo.isVisible());
        repaint();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void MitemExitManuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MitemExitManuActionPerformed
        // TODO add your handling code here:
        java.awt.Window window =
        SwingUtilities.getWindowAncestor(this);

        if(window != null){
            window.dispose();
        }

        new MainMenu().setVisible(true);
    }//GEN-LAST:event_MitemExitManuActionPerformed

    private void MitemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MitemExitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_MitemExitActionPerformed

    private void rbtnMCreaturesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnMCreaturesActionPerformed
        // TODO add your handling code here:
        if(rbtnMCreatures.isSelected()){
            panelMode.setOpaque(false);
            rbtnMCreatures.setBackground(new Color(0,102,102));
            rbtnMResources.setBackground(new Color(51,51,51));
            rbtnMbuilt.setBackground(new Color(51,51,51));
            rbtnMNone.setBackground(new Color(51,51,51));
            chansemode("Spawn");
            setTool(Tool.NONE);
        }
    }//GEN-LAST:event_rbtnMCreaturesActionPerformed

    private void TbtnCZyroxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnCZyroxActionPerformed
        // TODO add your handling code here:
            resetBorders();

        if (TbtnCZyrox.isSelected()) {

            TbtnCZyrox.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );

            setTool(Tool.Zyrox);
        }
    }//GEN-LAST:event_TbtnCZyroxActionPerformed

    private void TbtnCLummonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnCLummonActionPerformed
        // TODO add your handling code here:
            resetBorders();

        if (TbtnCLummon.isSelected()) {

            TbtnCLummon.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );

            setTool(Tool.Lummon);
        }
    }//GEN-LAST:event_TbtnCLummonActionPerformed

    private void TbtnRZenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnRZenActionPerformed
        // TODO add your handling code here:
        resetBorders();

        if (TbtnRZen.isSelected()) {

            TbtnRZen.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );

            setTool(Tool.Zethar);
        }
    }//GEN-LAST:event_TbtnRZenActionPerformed

    private void TbtnRNeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnRNeroActionPerformed
        // TODO add your handling code here:
        resetBorders();

        if (TbtnRNero.isSelected()) {

            TbtnRNero.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );

            setTool(Tool.Nero);
        }
    }//GEN-LAST:event_TbtnRNeroActionPerformed

    private void TbtnRZen2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnRZen2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TbtnRZen2ActionPerformed

    private void TbtnRZen3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnRZen3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TbtnRZen3ActionPerformed

    private void TbtnRZen4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnRZen4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TbtnRZen4ActionPerformed

    private void rbtnMNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnMNoneActionPerformed
        // TODO add your handling code here:
        if(rbtnMNone.isSelected()){
            panelMode.setOpaque(true);
            rbtnMCreatures.setBackground(new Color(51,51,51));
            rbtnMResources.setBackground(new Color(51,51,51));
            rbtnMbuilt.setBackground(new Color(51,51,51));
            rbtnMNone.setBackground(new Color(120,0,153));
            setTool(Tool.NONE);
        }
        
    }//GEN-LAST:event_rbtnMNoneActionPerformed

    private void TbtnBGrassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnBGrassActionPerformed
        // TODO add your handling code here:
        resetBorders();

        if (TbtnBGrass.isSelected()) {

            TbtnBGrass.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );
            
            selectedGroundVariant = Tile.DIRT_1;
            setTool(Tool.GRASS);
        }
    }//GEN-LAST:event_TbtnBGrassActionPerformed

    private void TbtnBGrass1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnBGrass1ActionPerformed
        // TODO add your handling code here:
        resetBorders();

        if (TbtnBGrass1.isSelected()) {

            TbtnBGrass1.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );
            
            selectedGroundVariant = Tile.DIRT_2;
            setTool(Tool.GRASS);
        }
    }//GEN-LAST:event_TbtnBGrass1ActionPerformed

    private void TbtnBGrass2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnBGrass2ActionPerformed
        // TODO add your handling code here:
        resetBorders();

        if (TbtnBGrass2.isSelected()) {

            TbtnBGrass2.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );
            
            selectedGroundVariant = Tile.DIRT_3;
            setTool(Tool.GRASS);
        }
    }//GEN-LAST:event_TbtnBGrass2ActionPerformed

    private void TbtnBGrass3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnBGrass3ActionPerformed
        // TODO add your handling code here:
        resetBorders();

        if (TbtnBGrass3.isSelected()) {

            TbtnBGrass3.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );
            
            selectedGroundVariant = Tile.DIRT_4;
            setTool(Tool.GRASS);
        }
    }//GEN-LAST:event_TbtnBGrass3ActionPerformed

    private void TbtnBGrass4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnBGrass4ActionPerformed
        // TODO add your handling code here:
        resetBorders();

        if (TbtnBGrass4.isSelected()) {

            TbtnBGrass4.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );
            
            selectedGroundVariant = Tile.PLAIN_5;
            setTool(Tool.GRASS);
        }
    }//GEN-LAST:event_TbtnBGrass4ActionPerformed

    private void TbtnBGrass5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnBGrass5ActionPerformed
        // TODO add your handling code here:resetBorders();

        if (TbtnBGrass5.isSelected()) {

            TbtnBGrass5.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );
            
            selectedGroundVariant = Tile.PLAIN_6;
            setTool(Tool.GRASS);
        }
    }//GEN-LAST:event_TbtnBGrass5ActionPerformed

    private void TbtnBGrass6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnBGrass6ActionPerformed
        // TODO add your handling code here:resetBorders();

        if (TbtnBGrass6.isSelected()) {

            TbtnBGrass6.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );
            
            selectedGroundVariant = Tile.PLAIN_7;
            setTool(Tool.GRASS);
        }
    }//GEN-LAST:event_TbtnBGrass6ActionPerformed

    private void TbtnBGrass7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnBGrass7ActionPerformed
        // TODO add your handling code here:resetBorders();

        if (TbtnBGrass7.isSelected()) {

            TbtnBGrass7.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );
            
            selectedGroundVariant = Tile.PLAIN_8;
            setTool(Tool.GRASS);
        }
    }//GEN-LAST:event_TbtnBGrass7ActionPerformed

    private void TbtnBGrass8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnBGrass8ActionPerformed
        // TODO add your handling code here:
        resetBorders();

        if (TbtnBGrass8.isSelected()) {

            TbtnBGrass8.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );
            
            selectedGroundVariant = Tile.ROCK_8;
            setTool(Tool.GRASS);
        }
    }//GEN-LAST:event_TbtnBGrass8ActionPerformed

    private void TbtnBGrass9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnBGrass9ActionPerformed
        // TODO add your handling code here:
        resetBorders();

        if (TbtnBGrass9.isSelected()) {

            TbtnBGrass9.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );
            
            selectedGroundVariant = Tile.ROCK_9;
            setTool(Tool.GRASS);
        }
    }//GEN-LAST:event_TbtnBGrass9ActionPerformed

    private void TbtnBWaterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnBWaterActionPerformed
        // TODO add your handling code here:
        resetBorders();

        if (TbtnBWater.isSelected()) {

            TbtnBWater.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );

            setTool(Tool.WATER);
        }
    }//GEN-LAST:event_TbtnBWaterActionPerformed

    private void TbtnRBlupysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TbtnRBlupysActionPerformed
        // TODO add your handling code here:
        resetBorders();

        if (TbtnRBlupys.isSelected()) {

            TbtnRBlupys.setBorder(
                new javax.swing.border.LineBorder(java.awt.Color.GREEN, 3)
            );

            setTool(Tool.Blupys);
        }
    }//GEN-LAST:event_TbtnRBlupysActionPerformed

    public int getSelectedGroundVariant() {
        return selectedGroundVariant;
    }
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
            new MainMenu().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem MitemExit;
    private javax.swing.JMenuItem MitemExitManu;
    private javax.swing.JMenuItem MitemGrid;
    private javax.swing.JMenuItem MitemLoad;
    private javax.swing.JMenuItem MitemSave;
    private javax.swing.JSlider SliderSpeed;
    private javax.swing.JToggleButton TbtnBGrass;
    private javax.swing.JToggleButton TbtnBGrass1;
    private javax.swing.JToggleButton TbtnBGrass2;
    private javax.swing.JToggleButton TbtnBGrass3;
    private javax.swing.JToggleButton TbtnBGrass4;
    private javax.swing.JToggleButton TbtnBGrass5;
    private javax.swing.JToggleButton TbtnBGrass6;
    private javax.swing.JToggleButton TbtnBGrass7;
    private javax.swing.JToggleButton TbtnBGrass8;
    private javax.swing.JToggleButton TbtnBGrass9;
    private javax.swing.JToggleButton TbtnBWater;
    private javax.swing.JToggleButton TbtnCLummon;
    private javax.swing.JToggleButton TbtnCZyrox;
    private javax.swing.JToggleButton TbtnRBlupys;
    private javax.swing.JToggleButton TbtnRNero;
    private javax.swing.JToggleButton TbtnRZen;
    private javax.swing.JToggleButton TbtnRZen2;
    private javax.swing.JToggleButton TbtnRZen3;
    private javax.swing.JToggleButton TbtnRZen4;
    private javax.swing.JMenuBar bMenuGame;
    private javax.swing.ButtonGroup btnGCreatures;
    private javax.swing.ButtonGroup btnGMBuilts;
    private javax.swing.ButtonGroup btnGResources;
    private javax.swing.JToggleButton btnGenerate;
    private javax.swing.ButtonGroup btnGmodes;
    private javax.swing.JButton btnInformationshow;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnStartTime1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblDAnimals;
    private javax.swing.JLabel lblDDays;
    private javax.swing.JLabel lblDResources;
    private javax.swing.JLabel lblDYear;
    private javax.swing.JLabel lblSpeed;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel menuGame;
    private javax.swing.JPanel panelGame;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JPanel panelMBuilt;
    private javax.swing.JPanel panelMCreature;
    private javax.swing.JPanel panelMResources;
    private javax.swing.JPanel panelMode;
    private javax.swing.JRadioButton rbtnMCreatures;
    private javax.swing.JRadioButton rbtnMNone;
    private javax.swing.JRadioButton rbtnMResources;
    private javax.swing.JRadioButton rbtnMbuilt;
    // End of variables declaration//GEN-END:variables
}
