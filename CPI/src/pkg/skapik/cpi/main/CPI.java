package pkg.skapik.cpi.main;


import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
// JAVAAAAAA
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
// OpenGL 
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

// OUR OWN OP CODE!
//import pkg.skapik.icp.assets.Player;
import pkg.skapik.cpi.functions.*;
import pkg.skapik.cpi.assets.*;


public class CPI{

	// OpenGL 
	private static FPSAnimator animator;
    public int width;
    public int height;
    private static Point center;
    private static Point center_on_screen;
    public static Rectangle screenSize;
    public static JFrame frame;
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
    
	public CPI(int w, int h, JFrame frame) {
		width = w;
	    height = h;
		module_frame = frame;
		dragging = false;
		xml_reader = new XML_Reader();

		cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	    blankCursor = Toolkit.getDefaultToolkit().createCustomCursor((Image)cursorImg, new Point(0, 0), "blank cursor");
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
	    //width = 1280;// screenSize.width; //(int) screenSize.getWidth();
	    //height = 762;// screenSize.height; //(int) screenSize.getHeight();
	    
	    // Nove okno
	    GLCapabilities caps = new GLCapabilities(GLProfile.get(GLProfile.GL2));
	    caps.setSampleBuffers(true);
	    caps.setNumSamples(4);
	    canvas = new GLCanvas(caps);
	    frame = new JFrame("CPI Module");
	    frame.setAlwaysOnTop(false);
	    frame.setSize(screenSize.width, screenSize.height);
	    //frame.getContentPane().setCursor(blankCursor); // vlastni kurzor
	    frame.setUndecorated(false); // borderless
	    frame.add(canvas);
	    
	    // Pridat platno a vykreslovaci smycku
	    animator = new FPSAnimator(60);
	    animator.add(canvas);
	    animator.start();
	    
	    // AAAAAAND OPEEEEEN!!!
	    frame.setVisible(true);
	    
	    // stred obrazovky jen pro inicializaci na hodnotach nezalezi
	    center = new Point((screenSize.width/2),(screenSize.height/2));
	    center_on_screen = new Point((canvas.getLocationOnScreen().x + screenSize.width/2),(canvas.getLocationOnScreen().y + screenSize.height/2));
	    
	    // navazani listeneru
	    CPI module = new CPI(screenSize.width, screenSize.height, frame);
	    Renderer renderer = new Renderer(module);
	    canvas.addGLEventListener(renderer);
	    canvas.addKeyListener((java.awt.event.KeyListener) new Key_Listener(module));
	    canvas.addMouseListener(new Mouse_Listener(module));
	    canvas.addMouseWheelListener(new Mouse_Listener(module));
	    canvas.addMouseMotionListener(new Mouse_Listener(module));
	    canvas.requestFocus();
	    frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	            System.exit(0);
	        }
	    });
	}

	public void key_pressed(int code, int id) {
		if(code == 67){
			
			xml_reader.Load_XML("./xml/PrikladFormaruRIB-Zapsal-iTWOcivil2016.cpixml");
			renderer.set_materials(xml_reader.get_materials());
			renderer.set_objects(xml_reader.get_object_tree());

		}else if(code == 86){
			renderer.toggle_wireframe();
		}else if(code == 27){
			String[] buttons = { "Ano", "Ne" };
			int dialogResult = JOptionPane.showOptionDialog(null, "Ukončit aplikaci?", null, JOptionPane.YES_NO_OPTION,
			        JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[1]);
			
			if(dialogResult == JOptionPane.YES_OPTION){
				System.exit(0);
			}
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
	}

	public void mouse_dragged(int button, int x, int y) {
		if(!dragging){
			center.setLocation(x, y);
			camera.init_view_coords(center.x,center.y);
		    center_on_screen.setLocation((canvas.getLocationOnScreen().x + center.x),(canvas.getLocationOnScreen().y + center.y));
			frame.getContentPane().setCursor(blankCursor);
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
					camera.Move(x,y);
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
		frame.getContentPane().setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
	}
	
	public void mouse_wheel_moved(MouseWheelEvent mwe) {
		this.camera.Scroll(-mwe.getWheelRotation());
	}

}