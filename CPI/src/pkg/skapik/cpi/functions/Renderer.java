package pkg.skapik.cpi.functions;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.gl2.GLUT;

import pkg.skapik.cpi.assets.Camera;
import pkg.skapik.cpi.assets.Container;
import pkg.skapik.cpi.assets.Materials;
import pkg.skapik.cpi.assets.Skybox;
import pkg.skapik.cpi.main.CPI;


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
        //gl.glLightModelfv( GL2.GL_LIGHT_MODEL_AMBIENT, Custom_Draw.float_color("light"), 0); // zakladni barva ambientniho a difuzniho osvetleni
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition,0);	//Position The Light
        //gl.glLighti(GL2.GL_LIGHT1, GL2.GL_LIGHT, arg2);
        gl.glShadeModel(GL2.GL_SMOOTH);     // smooooooth prechody mezi vertex barvama
        gl.glEnable( GL2.GL_LIGHT0 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT1 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT2 );         // zapni zdroj svetla
        
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, Custom_Draw.float_color(Custom_Draw.COLOR_LIGHT), 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, Custom_Draw.float_color(Custom_Draw.COLOR_LIGHT), 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, Custom_Draw.float_color(Custom_Draw.COLOR_LIGHT), 0);
        
        gl.glDisable( GL2.GL_LIGHT3 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT4 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT5 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT6 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT7 );         // zapni zdroj svetla
        gl.glEnable( GL2.GL_LIGHTING );         // zapni osvetleni
        
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_LEVEL, 7);
        gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);     // texture application method - modulation
        gl.glDisable(GL2.GL_TEXTURE_2D);
        //gl.glEnable(GL2.GL_COLOR_MATERIAL);
        
        gl.glDisable(GL2.GL_FOG);
        gl.glDisable(GL2.GL_BLEND);

        list_grid = gl.glGenLists(1);
        predraw_grid(800, 15);
        
        this.skybox = new Skybox(800,gl);
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
        glu.gluPerspective(80.0f, h, 0.01, 800.0);
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

        float[] lightPosition = new float[] {1,0.9f,0.8f,0};
        //gl.glLightModelfv( GL2.GL_LIGHT_MODEL_AMBIENT, Custom_Draw.float_color(Custom_Draw.COLOR_LIGHT), 0); // zakladni barva ambientniho a difuzniho osvetleni
        
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition,0);
        
        //gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, Custom_Draw.float_color(Custom_Draw.COLOR_LIGHT), 0);
        //gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, Custom_Draw.float_color(Custom_Draw.COLOR_BLACK), 0);
       	
        
        /////// !!!!!!! A KRESLIIIIMEEEE !!!!!!!! \\\\\\\\
        
        /*
        gl.glPushMatrix();
        gl.glTranslatef(-400.0f, -400.0f, -400.0f);
    	skybox.draw(gl,1);
        gl.glPopMatrix();
        */
        
        
        /*gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
 		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
 		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
         */
        
        
        
        gl.glPushMatrix();
        
        gl.glRotated(-90, 1, 0, 0);
        
        if(this.object_tree == null){
        	glut.glutSolidCube(5);
 		}else{
 			gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
 			this.object_tree.draw(gl,materials);
 			gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
 		}
 		gl.glPopMatrix();
 		
 		gl.glCallList(list_grid);
 		
 		/*
    	gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
 		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
 		gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
    	*/
 		
    	cam.draw_HUD(gl, frame_count);
	    	
	    gl.glFlush();

    }
    
    private void predraw_grid(int size, int spacing){
    	float half = (size/2.0f);
    	float m_half = (-1)*(size/2.0f);
    	
    	gl.glNewList(list_grid, GL2.GL_COMPILE );
    	
    	gl.glLineWidth(0.05f);
    	gl.glColor3f(0.0f, 0.0f, 0.0f);
    	gl.glEnable(GL2.GL_BLEND);
    	gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, Custom_Draw.float_color(Custom_Draw.COLOR_GREY, 0.8f), 0);
    	gl.glBegin(GL2.GL_LINES);
    	
    	for(int i = (int)(m_half); i <= half;i += spacing){
    		
    		gl.glVertex3f(i, 0, m_half);
    		gl.glVertex3f(i, 0, half);
    		
    		gl.glVertex3f(m_half, 0, i);
    		gl.glVertex3f(half, 0, i);
    		
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



}