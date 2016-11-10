package pkg.skapik.cpi.assets;

import java.nio.*;
import com.jogamp.common.nio.Buffers;
import javax.media.opengl.GL2;
import pkg.skapik.cpi.functions.Custom_Draw;

public class Skybox{
	
	private int skybox;
	private float size;
	
	public Skybox(float size, GL2 gl) {
		this.size = size;
		this.pre_draw(gl, Custom_Draw.float_color(0.7f, 0.8f, 0.9f,1.0f));
	}
	
	public void pre_draw(GL2 gl, float[] color) {

		float vert[] = {0,0,0, 0,0,size, 0,size,0, 0,size,size, size,0,0, size,0,size, size,size,0, size,size,size,  // back, front
						0,0,0, 0,0,size, 0,size,0, 0,size,size, size,0,0, size,0,size, size,size,0, size,size,size,  // left, right
						0,0,0, 0,0,size, 0,size,0, 0,size,size, size,0,0, size,0,size, size,size,0, size,size,size}; // top, bot       // 8x3 of vertex coords
		int ind[] = {0,4,6,2,1,3,7,5,   		 // back, front    // 6x4 of vertex indices
					 8,10,11,9,12,13,15,14,  	 // left, right
					 18,22,23,19,16,17,21,20};   // top, bot
		FloatBuffer vertices = Buffers.newDirectFloatBuffer(vert.length);
		for (int i = 0; i < vert.length; i++){
			vertices.put(vert[i]);
		}
		IntBuffer indices = Buffers.newDirectIntBuffer(vert.length);
		for (int i = 0; i < ind.length; i++){
			indices.put(ind[i]);
		}
		
		vertices.rewind();
		indices.rewind();
		
		skybox = gl.glGenLists(1);
	    gl.glNewList( skybox, GL2.GL_COMPILE );
	    // activate and specify pointer to vertex array
 		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
 	    //
 		gl.glVertexPointer(3, GL2.GL_FLOAT, 0, vertices);
 		// draw a cube
 		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, color, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, color, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, new float[]{100}, 0);
 		gl.glDrawElements(GL2.GL_QUADS, 24, GL2.GL_UNSIGNED_INT, indices);
 		
 		// deactivate vertex arrays after drawing
 		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
	    gl.glEndList();
	}

	public void draw(GL2 gl, float detail){
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, Custom_Draw.float_color(0.7f, 0.8f, 0.9f,1.0f), 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, Custom_Draw.float_color(0.7f, 0.8f, 0.9f,1.0f), 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SHININESS, new float[]{100}, 0);
		gl.glCallList( skybox );
	}

}
