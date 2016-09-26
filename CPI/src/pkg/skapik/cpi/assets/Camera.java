package pkg.skapik.cpi.assets;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

import pkg.skapik.cpi.functions.Position;
import pkg.skapik.cpi.functions.Vector;


public class Camera {
	
	private GLUT glut;
	
	private Position position;
	private Vector direction;
	private int old_x,old_y;
	private float speed;
	
	public Camera(){
		this.speed = 0.25f;
		this.old_x = -1;
		this.old_y = -1;
		this.glut = new GLUT();
		init();
	}
	
	private void init(){
		this.position = new Position(1,50,1);
		this.direction = new Vector(0,0,-1);
	}
	
	public void Controll(int key_code, int action) {

	}
	
	public void Look(int new_x, int new_y) {
		int d_x = (new_x - this.old_x);
		int d_y = (new_y - this.old_y);

		float angle_x  = 0.0f;				
		float angle_y  = 0.0f;							
		angle_x = (float)( (d_x) ) / 500.0f;
		angle_y = -(float)( (d_y) ) / 500.0f;

		Vector direction_normal = new Vector(-this.direction.getZ_F(),0,this.direction.getX_F());

		float tmp_y = this.direction.getY_F();
		this.direction.setY(0);
		this.direction.rotate(Vector.Y_AXIS, angle_x);
		this.direction.setY(tmp_y);
		this.direction.normalize();
		if(angle_y < 0){
			if(direction.getY_D() > -0.99){
				this.direction.rotate(direction_normal , angle_y);
			}
		}else{
			if(direction.getY_D() < 0.99){
				this.direction.rotate(direction_normal , angle_y);
			}
		}
	}
	
	public void Move(int new_x, int new_y) {
		
	}
	

	public Position get_position(){
		return this.position;
	}

	public Vector get_view_direction(){
		return this.direction;
	}
	
	public void Scroll(int wheelRotation) {
	}
	
	public void init_view_coords(int x, int y) {
		this.old_x = x;
		this.old_y = y;
	}


	public void draw_HUD(GL2 gl, int frame_count) {
		
	}
}
