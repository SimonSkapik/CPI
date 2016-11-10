package pkg.skapik.cpi.functions;

import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;

import pkg.skapik.cpi.assets.Camera;
import pkg.skapik.cpi.assets.Container;
import pkg.skapik.cpi.assets.Draw_3D_Solid;
import pkg.skapik.cpi.assets.Face;
import pkg.skapik.cpi.assets.Materials;
import pkg.skapik.cpi.assets.Object_3D;
import pkg.skapik.cpi.assets.Object_data;
import pkg.skapik.cpi.assets.Skybox;
import pkg.skapik.cpi.assets.Triangle;
import pkg.skapik.cpi.assets.Vertex;
import pkg.skapik.cpi.main.CPI;
import pkg.skapik.cpi.functions.Vector;


public class Renderer implements GLEventListener  {
	
	private GL2 gl;
	private GLU glu;
    private GLUT glut;
    
    public int width;
    public int height;
    private int frame_count;
    private Skybox skybox;
    public int list_grid;

	private CPI module;
	private Camera cam;
	private Position cam_pos;
	private Vector cam_dir;
	private Materials materials;
	private Container object_tree;
	
	private Container test;
	
	private boolean use_aa;
	private boolean wireframe;
	
	private final Vector y_axis = new Vector(0,1,0);
    private final Vector z_axis = new Vector(0,0,1);

	public Renderer(CPI module){
		
		this.glu = new GLU();
		this.glut = new GLUT();
	    
		this.module = module;
		this.module.init(this);
		
		this.cam = module.get_camera();
	    this.width = module.width;
	    this.height = module.height;
	    this.materials = new Materials();
	    this.object_tree = null;
	    this.frame_count = 0;
	    
	    this.use_aa = true;
	    this.wireframe = false;
	    
	    Object_data data = new Object_data("testiiiing");
	    Draw_3D_Solid data3d = new Draw_3D_Solid();
	    ArrayList<Vertex> vert = new ArrayList<>();
	    vert.add(new Vertex(0, 0, 4, 2));
	    vert.add(new Vertex(1, 2, 4, 2));
	    vert.add(new Vertex(2, 4, 6, 8));
	    vert.add(new Vertex(3, 4, 4, 2));
	    vert.add(new Vertex(4, 6, 6, 8));
	    vert.add(new Vertex(5, 4, 1, 2));
	    vert.add(new Vertex(6, 6, 3, 8));
	    vert.add(new Vertex(7, 6, -1, 8));
	    data3d.set_vertices(vert);
	    Face f = new Face("Planar");
	    f.add_triangle(new Triangle(0, 1, 2));
	    f.add_triangle(new Triangle(1, 4, 2));
	    f.add_triangle(new Triangle(1, 3, 4));
	    f.add_triangle(new Triangle(3, 6, 4));
	    f.add_triangle(new Triangle(3, 5, 6));
	    f.add_triangle(new Triangle(5, 7, 6));
	    data3d.add_face(f);
	    data3d.compile_indices();
	    data.add_draw_data(data3d);
	    this.test = new Container(5000, "Test Object", new Object_data("Testing"));
	    this.test.add_object(new Object_3D(5001, "Test 3D", 700, data));
	    
	}

	@Override
	public void display(GLAutoDrawable drawable) {
        render(drawable);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glEnable( GL2.GL_DEPTH_TEST ); 				//testovani hloubky
        gl.glEnable( GL2.GL_LINE_SMOOTH );    			//antialiasing car
        
        gl.glPolygonMode( GL2.GL_FRONT, GL2.GL_FILL );  // nastaveni rezimu vykresleni modelu
        gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);   // zpusob ulozeni bytu v texture
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);  // Pokus se to pls vykreslit hezky. Kdyz to teda pujde
        gl.glLineWidth( 0.0f ); //sirka cary
        gl.glBlendFunc( GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA );
        gl.glEnable( GL2.GL_CULL_FACE );               // zadne hrany ani steny se nebudou odstranovat - zpomaluje, ale vykresli vzdy a vsechno

        float[] lightPosition = new float[] {1,1,1,0};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition,0);	//Position The Light
        gl.glShadeModel(GL2.GL_SMOOTH);     // smooooooth prechody mezi vertex barvama
        
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, Custom_Draw.float_color(Custom_Draw.COLOR_WHITE), 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, Custom_Draw.float_color(Custom_Draw.COLOR_LIGHT), 0);
        gl.glLightModelfv( GL2.GL_LIGHT_MODEL_AMBIENT, Custom_Draw.float_color(0.5,0.5,0.5,1), 0); // zakladni barva ambientniho a difuzniho osvetleni
        
        gl.glEnable( GL2.GL_LIGHT0 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT1 );         // vypni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT2 );         // vypni zdroj svetla
        
        
        gl.glDisable( GL2.GL_LIGHT3 );         // vypni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT4 );         // vypni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT5 );         // vypni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT6 );         // vypni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT7 );         // vypni zdroj svetla
        gl.glEnable( GL2.GL_LIGHTING );         // vypni osvetleni
        
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_LEVEL, 7);
        gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);     // texture application method - modulation
        gl.glDisable(GL2.GL_TEXTURE_2D);
        
        gl.glFogi(GL2.GL_FOG_MODE, GL2.GL_LINEAR);
        gl.glFogfv(GL2.GL_FOG_COLOR, new float[]{0.7f, 0.8f, 0.9f,1.0f}, 0); 
        gl.glHint(GL2.GL_FOG_HINT, GL2.GL_DONT_CARE);
        gl.glFogf(GL2.GL_FOG_START, 25.0f); // Fog Start Depth 
        gl.glFogf(GL2.GL_FOG_END, 30.0f); // Fog End Depth
        gl.glEnable(GL2.GL_FOG);
        gl.glDisable(GL2.GL_BLEND);
        
        list_grid = gl.glGenLists(1);
        predraw_grid(30, 0.5);
        
        this.skybox = new Skybox(150,gl);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println("reshape() called: x = "+x+", y = "+y+", width = "+width+", height = "+height);
 
        if (height <= 0) // avoid a divide by zero error!
        {
            height = 1;
        }
 
        final float h = (float) width / (float) height; // aspect ratio
 
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(80.0f, h, 0.01, 350.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
	}

    private void render(GLAutoDrawable drawable) {
     	frame_count++;
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT); 	// vymazani bitovych rovin barvoveho bufferu
        gl.glLoadIdentity(); 											// vynulovani vsech predchozich transformaci
        gl.glMatrixMode(GL2.GL_MODELVIEW);         						// bude se menit matice modelview
        gl.glLoadIdentity();											// vynulovani vsech predchozich transformaci
        
        cam_pos = cam.get_position();
        cam_dir = new Vector(cam.get_view_direction());
        cam_dir.add(cam_pos);
        glu.gluLookAt(cam_pos.getX_D(), cam_pos.getY_D(), cam_pos.getZ_D(),
        		cam_dir.getX_D(), cam_dir.getY_D(), cam_dir.getZ_D(), 0, 1, 0); // postaveni kamery

        Vector sun_rot = new Vector(cam.get_view_direction());
        sun_rot.setY(0);
        sun_rot.rotate(y_axis, -sun_rot.get_angle_to(z_axis));
        sun_rot.normalize();
        sun_rot.setY(1);
        sun_rot.normalize();
        float[] lightPosition = new float[] {sun_rot.getX_F(),sun_rot.getY_F(),sun_rot.getZ_F(),0};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition,0);
        
        /////// !!!!!!! A KRESLIIIIMEEEE !!!!!!!! \\\\\\\\
        
        gl.glPushMatrix();
        gl.glTranslatef(cam_pos.getX_F(), cam_pos.getY_F(), cam_pos.getZ_F());
        gl.glTranslatef(-75.0f, -75.0f, -75.0f);
    	skybox.draw(gl,1);
        gl.glPopMatrix();

        
        /*gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
 		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
         */
        

        gl.glPushMatrix();
        
        gl.glRotated(-90, 1, 0, 0);
        
        if(this.object_tree == null){
        	glut.glutSolidCube(3);
 		}else{
 			
 			gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
 			
 			if(use_aa){
 				gl.glEnable(GL2.GL_MULTISAMPLE);
 			}
 			
 			if(wireframe){
 				gl.glPolygonMode( GL2.GL_FRONT, GL2.GL_LINE );
 	 			gl.glLineWidth(0.1f);
 			}
 			
 			
 			this.object_tree.draw(gl,materials);

 			gl.glPolygonMode( GL2.GL_FRONT, GL2.GL_FILL );
 			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
 			gl.glDisable(GL2.GL_MULTISAMPLE);
 			
 		}

        gl.glPopMatrix();
 		
 		gl.glCallList(list_grid);
 		
    	//cam.draw_HUD(gl, frame_count);
	    	
	    gl.glFlush();

    }
    
    private void predraw_grid(int size, double grid_density){
    	double half = (size/2.0);
    	double m_half = (-1)*(size/2.0);
    	
    	gl.glNewList(list_grid, GL2.GL_COMPILE );
    	
    	gl.glLineWidth(0.05f);
    	gl.glColor3f(0.0f, 0.0f, 0.0f);
    	gl.glEnable(GL2.GL_BLEND);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, Custom_Draw.float_color(Custom_Draw.COLOR_GREY, 0.8f), 0);
    	gl.glBegin(GL2.GL_LINES);
    	
    	for(double i = (m_half); i <= half;i += grid_density){
    		
    		gl.glVertex3d(i, 0, m_half);
    		gl.glVertex3d(i, 0, half);
    		
    		gl.glVertex3d(m_half, 0, i);
    		gl.glVertex3d(half, 0, i);
    		
    	}
    	
    	gl.glEnd(); 
    	gl.glDisable(GL2.GL_BLEND);
		
    	gl.glEndList();
		
    }
    
    public void set_materials(Materials mat){
    	this.materials = mat;
    }

	public int get_frame_count() {
		return frame_count;
	}

	public void print_materials() {
		System.out.print(this.materials.toString());
	}

	public void set_objects(Container get_object_tree) {
		this.object_tree = get_object_tree;
	}

	public int count_drawable() {
		return this.object_tree.count_drawable();
	}
	
	public void test_print(){
		int buf[] = new int[1];
	    int sbuf[] = new int[1];
		gl.glGetIntegerv(GL2.GL_SAMPLE_BUFFERS, buf, 0);
	    System.out.println("number of sample buffers is " + buf[0]);
	    gl.glGetIntegerv(GL2.GL_SAMPLES, sbuf, 0);
	    System.out.println("number of samples is " + sbuf[0]);
	}

	public void toggle_aa() {
		this.use_aa = !this.use_aa;
	}

	public void toggle_wireframe() {
		this.wireframe = !this.wireframe;
	}



}