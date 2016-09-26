package pkg.skapik.cpi.functions;

import java.nio.FloatBuffer;

import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

public class Custom_Draw {
	
	public static int COLOR_WHITE = 0;
	public static int COLOR_BLACK = 1;
	public static int COLOR_RED = 2;
	public static int COLOR_BLUE = 3;
	public static int COLOR_GREEN = 4;
	public static int COLOR_GREY = 5;
	public static int COLOR_LIGHT = 6;
	
	public static boolean is_black(double[] pixel) {
		int counter = 0;
		for(int k = 0;k < pixel.length;k++){
			counter += pixel[k];
			
		} 
		if(counter == 0){
			return true;
		}
		return false;
	}

    public static float[] float_color(float r, float g, float b, float a){
    	float Color[] = {r, g, b, a};
    	return Color;
    }
 
    public static float[] float_color(int r, int g, int b, int a){
    	float Color[] = {(((float)r)/255), (((float)g)/255), (((float)b)/255), (((float)a)/255)};
    	return Color;
    }
    
    public static float[] float_color(double r, double g, double b, double a){
    	float Color[] = {(float)r, (float)g, (float)b, (float)a};
    	return Color;
    }

    public static float[] float_color(int color_name){
    	return float_color(color_name, 1.0f);
    }
    
    public static void drawCircle(GL2 gl, float radius){
    	//gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, Custom_Draw.float_color("black"), 0);
    	gl.glBegin(GL2.GL_LINE_LOOP);
    	gl.glColor4f(0.0f,1.0f,0.0f,1.0f); //line color
    	for (int i=0; i < 360; i+=60){
    		float degInRad = (float) ((i*Math.PI)/180.0f);
    		gl.glVertex2d(Math.cos(degInRad)*radius,Math.sin(degInRad)*radius);
    	}
     
    	gl.glEnd();
    }
    
    public static void drawQuad(GL2 gl, float size){
    	gl.glPushMatrix();
    	gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
    	gl.glDisable(GL2.GL_TEXTURE_2D);
 		gl.glEnable(GL2.GL_BLEND);
 		
 		FloatBuffer vertices;
		vertices = Buffers.newDirectFloatBuffer(8);
		vertices.put(0);
		vertices.put(0);
		vertices.put(0);
		vertices.put(1);
		vertices.put(1);
		vertices.put(1);
		vertices.put(1);
		vertices.put(0);
		vertices.rewind();
 		gl.glVertexPointer(2, GL2.GL_FLOAT, 0, vertices);
 		// draw a cube
 		gl.glScaled(size, size, 1);
 		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, Custom_Draw.float_color(COLOR_WHITE), 0);
 		gl.glDrawArrays(GL2.GL_QUADS, 0, 4);
 		
 		// deactivate vertex arrays after drawing
 		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
 		gl.glEnable(GL2.GL_TEXTURE_2D);
 		gl.glDisable(GL2.GL_BLEND);
 		gl.glPopMatrix();
    }

    public static float[] float_color(int color_name, float a){
    	float r,g,b;
    	switch(color_name){
    		case 0:
    			r = 1;
    			g = 1;
    			b = 1; 
    		break;
    		case 1:
    			r = 0;
    			g = 0;
    			b = 0;
    		break;
    		case 2:
    			r = 1;
    			g = 0;
    			b = 0;
    		break;
    		case 3:
    			r = 0;
    			g = 0;
    			b = 1; 
    		break;
    		case 4:
    			r = 0;
    			g = 1;
    			b = 0;
    		break;
    		case 5:
    			r = 0.5f;
    			g = 0.5f;
    			b = 0.5f;
    		break;
    		case 6:
    			r = 0.95f;
    			g = 0.95f;
    			b = 0.8f;
    		break;
    		default:
    			r = 0;
    			g = 0;
    			b = 0;
    	}
    	float Color[] = {r, g, b, a};
    	return Color;
    }
    

}
