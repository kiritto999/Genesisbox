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
        mouser = new Mouser(camera, GP, world,EManager);
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

        // combobox
        for (String animal : animals) { 
            cboxAnimals.addItem(animal);
        }
        cboxAnimals.addActionListener(e -> {
            if (!RbtnAnimals.isSelected()) return;

            int animal = cboxAnimals.getSelectedIndex();

            switch (animal) {
                case 1:
                    setTool(Tool.Lummon);
                    break;
                case 2:
                    setTool(Tool.Zyrox);
                    break;
                default:
                    setTool(Tool.NONE);
                    break;
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
        panelMSpawn = new javax.swing.JPanel();
        RbtnResource = new javax.swing.JRadioButton();
        cboxResource = new javax.swing.JComboBox<>();
        RbtnAnimals = new javax.swing.JRadioButton();
        cboxAnimals = new javax.swing.JComboBox<>();
        btnGenerate = new javax.swing.JToggleButton();
        RbtnGNone = new javax.swing.JRadioButton();
        panelMBuilt = new javax.swing.JPanel();
        rbtnBNone = new javax.swing.JRadioButton();
        rbtnBWater = new javax.swing.JRadioButton();
        rbtnBGlass = new javax.swing.JRadioButton();
        btnStartTime1 = new javax.swing.JButton();
        SliderSpeed = new javax.swing.JSlider();
        lblSpeed = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblDYear = new javax.swing.JLabel();
        lblDDays = new javax.swing.JLabel();
        lblDAnimals = new javax.swing.JLabel();
        lblDResources = new javax.swing.JLabel();
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
        rbtnMbuilt.setBounds(500, 20, 110, 20);

        panelMode.setLayout(new java.awt.CardLayout());

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

    int rngX;
    int rngY;

    do{
        rngX = rng.nextInt(world.getColums());
        rngY = rng.nextInt(world.getRows());

    }while(world.getTile(rngY, rngX).getType() == Tile.WATER);

    
    if (RbtnAnimals.isSelected()){      

        if (cboxAnimals.getSelectedIndex() == 0 ){
            return;

        }else{
            EManager.addAnimal(
                makeAnimal(
                    cboxAnimals.getSelectedIndex(),
                    rngX,
                    rngY,
                    EManager
                )
            );
        }
    }

    
    if (RbtnResource.isSelected()){

        if (cboxResource.getSelectedIndex() == 0 ){
            return;

        }else{
            EManager.addResourse(
                makeResourse(
                    cboxResource.getSelectedIndex(),
                    rngX,
                    rngY
                )
            );
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
    private javax.swing.JRadioButton RbtnAnimals;
    private javax.swing.JRadioButton RbtnGNone;
    private javax.swing.JRadioButton RbtnResource;
    private javax.swing.JSlider SliderSpeed;
    private javax.swing.JMenuBar bMenuGame;
    private javax.swing.ButtonGroup btnGMBuilts;
    private javax.swing.ButtonGroup btnGSpawn;
    private javax.swing.JToggleButton btnGenerate;
    private javax.swing.ButtonGroup btnGmodes;
    private javax.swing.JButton btnInformationshow;
    private javax.swing.JButton btnPause;
    private javax.swing.JButton btnStartTime1;
    private javax.swing.JComboBox<String> cboxAnimals;
    private javax.swing.JComboBox<String> cboxResource;
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
    private javax.swing.JPanel panelMSpawn;
    private javax.swing.JPanel panelMode;
    private javax.swing.JRadioButton rbtnBGlass;
    private javax.swing.JRadioButton rbtnBNone;
    private javax.swing.JRadioButton rbtnBWater;
    private javax.swing.JRadioButton rbtnMGenerate;
    private javax.swing.JRadioButton rbtnMbuilt;
    // End of variables declaration//GEN-END:variables
}
