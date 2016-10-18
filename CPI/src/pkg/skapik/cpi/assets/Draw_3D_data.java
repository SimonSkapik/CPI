package pkg.skapik.cpi.assets;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

public abstract class Draw_3D_data {
	
	public enum Type {CHAIN, POLYGON, SOLID};
	protected Type type;
	protected DoubleBuffer vertices;
	protected boolean compiled;
	
	public abstract void compile_indices();
	public abstract void draw(GL2 gl); 
	public abstract boolean is_drawable();
	
	
	public Draw_3D_data(){
		this.vertices = null; 
		this.compiled = false;
	}
	
	public void set_type(Type t){
		this.type = t;
	}
	
	public void set_vertices(ArrayList<Vertex> vert){
		Collections.sort(vert, new Comparator<Vertex>() {
		    @Override
		    public int compare(Vertex v1, Vertex v2) {
		        return v1.get_id() - v2.get_id();
		    }
		});
		this.vertices = Buffers.newDirectDoubleBuffer(vert.size()*3);
		//this.normals = Buffers.newDirectFloatBuffer(faces_to_draw*12);

		for (Vertex v : vert){
			this.vertices.put(v.get_x());
			this.vertices.put(v.get_y());
			this.vertices.put(v.get_z());
			//this.normals.put(norm[i*3+(j%3)]);
		}
		
		this.vertices.rewind();
		//this.normals.rewind();
	}


}
