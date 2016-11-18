package pkg.skapik.cpi.main;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
// JAVAAAAAA
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
// OpenGL 
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

// OUR OWN OP CODE!
//import pkg.skapik.icp.assets.Player;
import pkg.skapik.cpi.functions.*;
import pkg.skapik.cpi.assets.*;


public class CPI implements ActionListener{

	// OpenGL 
	private static FPSAnimator animator;
    public int width;
    public int height;
    private static Point center;
    private static Point center_on_screen;
    public static Rectangle screenSize;
    public static GLCanvas canvas;

	private BufferedImage cursorImg;
	private Cursor blankCursor;
	
	// Module
	private Camera camera;
	private Robot robot_controller;
    private JFrame module_frame;
    private Renderer renderer;
	private boolean dragging;
	private XML_Reader xml_reader;
	private boolean shift_key;
    
	public CPI(int w, int h) {
		width = w;
	    height = h;
		dragging = false;
		xml_reader = new XML_Reader();
		cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	    blankCursor = Toolkit.getDefaultToolkit().createCustomCursor((Image)cursorImg, new Point(0, 0), "blank cursor");
	    
	    // Nove okno
	    GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
	    caps.setSampleBuffers(true);
	    caps.setNumSamples(4);
	    canvas = new GLCanvas(caps);
	    module_frame = new JFrame("CPI Module");
	    module_frame.setAlwaysOnTop(false);
	    module_frame.setSize(screenSize.width, screenSize.height);
	    module_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JMenuBar jmb = new JMenuBar();

	    JMenu jmApp = new JMenu("App");
	    JMenu jmFile = new JMenu("File");
	    JMenuItem jmiOpen = new JMenuItem("Open");
	    JMenuItem jmiExit = new JMenuItem("Exit");
	    jmFile.add(jmiOpen);
	    jmApp.add(jmiExit);
	    jmb.add(jmFile);

	    jmiOpen.addActionListener(this);
	    jmiExit.addActionListener(this);
	    module_frame.setJMenuBar(jmb);

	    module_frame.setUndecorated(false); // borderless
	    
	    GridBagLayout gbl = new GridBagLayout();
	    module_frame.setLayout(gbl);
	    gbl.columnWidths = new int[] {300, screenSize.width-300};
        gbl.rowHeights = new int[] {screenSize.height};
        gbl.columnWeights = new double[] {1, 1};
        gbl.rowWeights = new double[] {1};
        
	    GridBagConstraints gbc = new GridBagConstraints();
	    
	    gbc.fill = GridBagConstraints.BOTH;
	    gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.insets = new Insets(0, 0, 0, 0);
	    
		
		JPanel pane = new JPanel();
		pane.setBackground(Color.green);
		gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
	    gbc.gridwidth = 1;
	    gbl.setConstraints(pane, gbc);
	    module_frame.getContentPane().add(pane);
	    
        JPanel pane2 = new JPanel();
        pane2.setBackground(Color.red);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
	    gbc.gridwidth = 2;
	    gbl.setConstraints(canvas, gbc);
		
		//canvas.setSize((int)(screenSize.width*0.7), screenSize.height+10);
	    //pane2.add(canvas, gbc);
	    module_frame.getContentPane().add(canvas);
/*
        layout.fill = GridBagConstraints.BOTH;
        layout.gridx = 1;
        layout.gridy = 0;
        //canvas.setSize(500, 400);
        layout.weightx = layout.weighty = 1.0;*/
        //module_frame.add(canvas, BorderLayout.EAST);
        
        /*JButton button = new JButton("Button 1");
	    if (true) {
	                       layout.weightx = 0.5;
	    }
	    layout.fill = GridBagConstraints.BOTH;
	    layout.gridx = 0;
	    layout.gridy = 0;
	    pane.add(button, layout);
	    //module_frame.add(canvas);

	    /*JPanel pane = new JPanel(new GridBagLayout());
	    JButton button;
	    pane.setLayout(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
	    if (true) {
	                    //natural height, maximum width
	                    c.fill = GridBagConstraints.HORIZONTAL;
	    }

	    
	    button = new JButton("Button 1");
	    if (true) {
	                       c.weightx = 0.5;
	    }
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.gridx = 0;
	    c.gridy = 0;
	    pane.add(button, c);

	    button = new JButton("Button 2");
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 0.5;
	    c.gridx = 1;
	    c.gridy = 0;
	    pane.add(button, c);

	    button = new JButton("Button 3");
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 0.5;
	    c.gridx = 2;
	    c.gridy = 0;
	    pane.add(button, c);

	    button = new JButton("Long-Named Button 4");
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.ipady = 40;      //make this component tall
	    c.weightx = 0.0;
	    c.gridwidth = 3;
	    c.gridx = 0;
	    c.gridy = 1;
	    pane.add(button, c);

	    button = new JButton("5");
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.ipady = 0;       //reset to default
	    c.weighty = 1.0;   //request any extra vertical space
	    c.anchor = GridBagConstraints.PAGE_END; //bottom of space
	    c.insets = new Insets(10,0,0,0);  //top padding
	    c.gridx = 1;       //aligned with button 2
	    c.gridwidth = 2;   //2 columns wide
	    c.gridy = 2;       //third row
	    pane.add(button, c);
	    */
	    //module_frame.add(pane);
	    
	    
	    Renderer renderer = new Renderer(this);
	    canvas.addGLEventListener(renderer);
	    canvas.addKeyListener((java.awt.event.KeyListener) new Key_Listener(this));
	    canvas.addMouseListener(new Mouse_Listener(this));
	    canvas.addMouseWheelListener(new Mouse_Listener(this));
	    canvas.addMouseMotionListener(new Mouse_Listener(this));
	    canvas.requestFocus();
	    module_frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	            System.exit(0);
	        }
	    });
	    
	    // Pridat platno a vykreslovaci smycku
	    animator = new FPSAnimator(60);
	    animator.add(canvas);
	    animator.start();
	    
	    // AAAAAAND OPEEEEEN!!!
	    module_frame.setVisible(true);
	    
	    // stred obrazovky jen pro inicializaci na hodnotach nezalezi
	    center = new Point((canvas.getWidth()/2),(canvas.getHeight()/2));
	    center_on_screen = new Point((canvas.getLocationOnScreen().x + canvas.getWidth()/2),(canvas.getLocationOnScreen().y + canvas.getHeight()/2));
	}

	public void init(Renderer rndr){ // Initialization - called when you initially want to initialize the initial values. :-P
		this.camera = new Camera();
		this.renderer = rndr;
		try {
			this.robot_controller = new Robot(); // Robot pro systemove rizeni mysi
			//camera.init_view_coords(center.x,center.y);
		} catch (AWTException e) {
			e.printStackTrace(); // OOOOPS
		}
	}

	///////// Main Method //////////
		
	public static void main(String[] args) {
	    // Jak velky okno?
	    //screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	    screenSize = new Rectangle(1000, 600);
	    new CPI(screenSize.width, screenSize.height);
	}

	public void key_pressed(int code, int id) {
		if(code == 86){
			renderer.toggle_wireframe();
		}else if(code == 27){
			String[] buttons = { "Ano", "Ne" };
			int dialogResult = JOptionPane.showOptionDialog(null, "Ukončit aplikaci?", null, JOptionPane.YES_NO_OPTION,
			        JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[1]);
			if(dialogResult == JOptionPane.YES_OPTION){
				System.exit(0);
			}
		}else if(code == 16){
			this.shift_key = true;
		}else if(code == 17){
			Position pos =  this.camera.get_position();
			Vector dir = this.camera.get_view_direction();
			System.out.println("pos: ["+pos.getX_F()+", "+pos.getY_F()+", "+pos.getZ_F()+"]  dir: ["+dir.getX_F()+", "+dir.getY_F()+", "+dir.getZ_F()+"]");
		}else{
			System.out.println("Key: "+code);
		}
		/*if(code == 87 || code == 83 || code == 65 || code == 68 || code == 32 || code == 17){
			player.Controll(code, id);
		}else if(code == 16){
			player.set_sneak(true);
		}else if(code == 27){
			String[] buttons = { "Yeah... :-|", "Psych! JK :D" };
			int dialogResult = JOptionPane.showOptionDialog(null, "Already leaving? :-(", "Srsly??", JOptionPane.YES_NO_OPTION,
			        JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[1]);
			
			if(dialogResult == JOptionPane.YES_OPTION){
				if(player.is_in_inventory())
					player.open_inventory();
				System.exit(0);
			}
		}else if(code >= 49 && code <= 57){
			player.hbar_select(code - 49);
		}else if(code == 69){
			if(player.is_in_inventory()){
				robot_controller.mouseMove(center_on_screen.x,center_on_screen.y);
			}
			player.open_inventory();
		}else if(code == 84){
			player.get_renderer().add_time(-100);
		}else if(code == 90){
			player.get_renderer().add_time(100);
		}else if(code == 67){
			player.toggle_cam();
		}else{
			System.out.println("Key: "+code);
		}*/
	}
	
	public void key_released(int code, int id) { // Kocka slezla z klavesnice
		/*if(code == 87 || code == 83 || code == 65 || code == 68 || code == 32 || code == 17){
			player.Controll(code, id);
		}else if(code == 16){
			player.set_sneak(false);
		}*/
		 if(code == 16){
			this.shift_key = false;
		}
	}

	public void mouse_dragged(int button, int x, int y) {
		if(!dragging){
			center.setLocation(x, y);
			camera.init_view_coords(center.x,center.y);
		    center_on_screen.setLocation((canvas.getLocationOnScreen().x + center.x),(canvas.getLocationOnScreen().y + center.y));
			module_frame.getContentPane().setCursor(blankCursor);
			dragging = true;
		}else{
			if(button == 1){
				if(x != center.x || y != center.y){
					camera.Look(x,y);
				}
				robot_controller.mouseMove(center_on_screen.x,center_on_screen.y);
			}else if(button == 2){
				if(x != center.x || y != center.y){
					camera.Rotate(x,y);
				}
				robot_controller.mouseMove(center_on_screen.x,center_on_screen.y);
			}else if(button == 3){
				if(x != center.x || y != center.y){
					camera.Move(x,y,this.shift_key);
				}
				robot_controller.mouseMove(center_on_screen.x,center_on_screen.y);
			}
		}
	}

	public void mouse_moved(int x, int y) {

	}
	
	public Camera get_camera() {
		return this.camera;
	}

	public void mouse_clicked(MouseEvent m) {
		//this.camera.Click(m);
	}
	
	public void mouse_released(MouseEvent m) {
		dragging = false;
		module_frame.getContentPane().setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
	}
	
	public void mouse_wheel_moved(MouseWheelEvent mwe) {
		this.camera.Scroll(-mwe.getWheelRotation(), this.shift_key);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		switch(ae.getID()){
			case 1001:{
				this.load_file();
			}break;
		}
	    
	}

	private void load_file() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(module_frame);
		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = fileChooser.getSelectedFile();
		    xml_reader.Load_XML(selectedFile.getAbsolutePath());
			renderer.set_materials(xml_reader.get_materials());
			renderer.set_objects(xml_reader.get_object_tree());
		}
	}

}