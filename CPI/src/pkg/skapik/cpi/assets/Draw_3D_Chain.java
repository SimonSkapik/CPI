package pkg.skapik.cpi.assets;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

public class Draw_3D_Chain extends Draw_3D_data {
	
	private IntBuffer indices;
	private int chain_length;
	private FloatBuffer normals;
	
	public Draw_3D_Chain(){
		super();
		indices = null;
		chain_length = 0;
	}
	
	public void add_chain(int[] chain){
		//this.indices = chain;
		this.chain_length = chain.length;
		this.vertices = Buffers.newDirectFloatBuffer(chain_length*3);
		this.normals = Buffers.newDirectFloatBuffer(chain_length*3);

		for(int i = 0; i < chain.length; i++){
			this.vertices.put(this.vertex_list.get(chain[i]).get_x());
			this.vertices.put(this.vertex_list.get(chain[i]).get_y());
			this.vertices.put(this.vertex_list.get(chain[i]).get_z());
			this.normals.put(0);
			this.normals.put(0);
			this.normals.put(1);
			
		}
		
		this.vertices.rewind();
		this.normals.rewind();
	}

	@Override
	public void draw(GL2 gl) {
		if(this.vertices != null){
			gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			gl.glNormalPointer(GL2.GL_FLOAT, 0, this.normals);
			gl.glVertexPointer(3, GL2.GL_FLOAT, 0, this.vertices);
			gl.glDrawArrays(GL2.GL_LINES, 0, chain_length);
			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
			
		}
	}

	@Override
	public void compile_indices() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean is_drawable() {
		if(this.vertices != null && this.indices != null){
			return true;
		}
		return false;
	}

	@Override
	public ArrayList<Vertex> get_vertex_list() {
		return this.vertex_list;
	}

	@Override
	public ArrayList<Face> get_faces() {
		return null;
	}
	
	@Override
	public int get_vertex_count() {
		return this.vertices.capacity();
	}
	
}
