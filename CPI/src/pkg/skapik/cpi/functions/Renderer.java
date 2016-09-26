package pkg.skapik.cpi.functions;

import java.awt.Point;
import java.awt.dnd.DragSourceMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import pkg.skapik.cpi.assets.Camera;
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
    //public int crosshair;

    /*private ArrayList<Creature> mobs;
    private ArrayList<Creature> dead_mobs;
    private ArrayList<Cloud> clouds;*/
    
	private CPI module;
	private Camera cam;
	

	public Renderer(CPI module){
		this.glu = new GLU();
		this.glut = new GLUT();
	    
		this.module = module;
		this.module.init();
		
		this.cam = module.get_camera();
	    this.width = module.width;
	    this.height = module.height;
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
        gl.glEnable( GL2.GL_LIGHT1 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT2 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT3 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT4 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT5 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT6 );         // zapni zdroj svetla
        gl.glDisable( GL2.GL_LIGHT7 );         // zapni zdroj svetla
        gl.glEnable( GL2.GL_LIGHTING );         // zapni zdroj svetla
        
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_LEVEL, 7);
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_MODULATE);     // texture application method - modulation
        
        gl.glDisable(GL2.GL_FOG);
        gl.glDisable(GL2.GL_BLEND);

        

        
        // list_start = gl.glGenLists(11);
        // list_2 = list_start+1;
        
	    /*
        gl.glNewList(list_start, GL2.GL_COMPILE );
	    
	    // activate and specify pointer to vertex array
 		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
 		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
 		gl.glEnable(GL2.GL_BLEND);
 		
 		FloatBuffer vertices;
		vertices = Buffers.newDirectFloatBuffer(8);
		vertices.put(-0.5f);
		vertices.put(-0.5f);
		vertices.put(-0.5f);
		vertices.put(0.5f);
		vertices.put(0.5f);
		vertices.put(0.5f);
		vertices.put(0.5f);
		vertices.put(-0.5f);
		vertices.rewind();
 		gl.glVertexPointer(2, GL2.GL_FLOAT, 0, vertices);
 		gl.glTexCoordPointer(2, GL2.GL_FLOAT, 0, main_coords_manager.get_texture_coords(Texture_List.HBAR_BG));
 		// draw a cube
 		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, Custom_Draw.float_color("white"), 0);
 		gl.glDrawArrays(GL2.GL_QUADS, 0, 4);
 		
 		// deactivate vertex arrays after drawing
 		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
 		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
 		gl.glDisable(GL2.GL_BLEND);
 		
	    gl.glEndList();
	    */
        
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
        glu.gluPerspective(80.0f, h, 0.01, 2000.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
	}

    private void render(GLAutoDrawable drawable) {
     	frame_count++;
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT); 	// vymazani bitovych rovin barvoveho bufferu
        gl.glLoadIdentity(); 											// vynulovani vsech predchozich transformaci
        gl.glMatrixMode(GL2.GL_MODELVIEW);         						// bude se menit matice modelview
        gl.glLoadIdentity();											// vynulovani vsech predchozich transformaci
        
        Position cam_pos = cam.get_position();
        Vector cam_dir = new Vector(cam.get_view_direction());
        cam_dir.add(cam_pos);
        glu.gluLookAt(cam_pos.getX_D(), cam_pos.getY_D(), cam_pos.getZ_D(),
        		cam_dir.getX_D(), cam_dir.getY_D(), cam_dir.getZ_D(), 0, 1, 0); // postaveni kamery

        float[] lightPosition = new float[] {1,1,1,0};
        gl.glLightModelfv( GL2.GL_LIGHT_MODEL_AMBIENT, Custom_Draw.float_color(Custom_Draw.COLOR_RED), 0); // zakladni barva ambientniho a difuzniho osvetleni
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition,0);
        
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, Custom_Draw.float_color(Custom_Draw.COLOR_LIGHT), 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, Custom_Draw.float_color(Custom_Draw.COLOR_BLACK), 0);
       	
        
        /////// !!!!!!! A KRESLIIIIMEEEE !!!!!!!! \\\\\\\\
        
        gl.glTranslatef(cam.get_position().getX_F(), cam.get_position().getY_F(), cam.get_position().getZ_F());
        
        gl.glPushMatrix();
        gl.glTranslatef(-400.0f, -400.0f, -400.0f);
    	skybox.draw(gl,1);
        gl.glPopMatrix();
        
        /*gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
 		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
 		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
*/
 		
 		
 		// DRAW SCENE
 		gl.glPushMatrix();
 		//draw_cube(gl,5);
 		gl.glTranslatef(0, 0, -4.0f);
 		Custom_Draw.drawQuad(gl, 10);
 		gl.glPopMatrix();
 		
 		gl.glPushMatrix();
 		gl.glTranslatef(0, 0, 4.0f);
 		Custom_Draw.drawQuad(gl, 10);
 		gl.glPopMatrix();
 	

 		gl.glPushMatrix();
 		gl.glTranslatef(0, 4.0f, 0);
 		Custom_Draw.drawQuad(gl, 10);
 		gl.glPopMatrix();

 		gl.glPushMatrix();
 		gl.glTranslatef(0, -4.0f, 0);
 		Custom_Draw.drawQuad(gl, 10);
 		gl.glPopMatrix();

 		gl.glPushMatrix();
 		gl.glTranslatef(4.0f, 0, 0);
 		Custom_Draw.drawQuad(gl, 10);
 		gl.glPopMatrix();

 		gl.glPushMatrix();
 		gl.glTranslatef(-4.0f,0,0);
 		Custom_Draw.drawQuad(gl, 10);
 		gl.glPopMatrix();
 		
 		/*
    	gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
 		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
 		gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
    	*/
    	cam.draw_HUD(gl, frame_count);
	    	
	    gl.glFlush();


    }
    
    private void draw_cube(GL2 gl, int scale){
    	
    }

	public int get_frame_count() {
		return frame_count;
	}



}