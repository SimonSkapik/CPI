package pkg.skapik.cpi.assets;

import java.nio.IntBuffer;

import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

public class Draw_3D_Chain extends Draw_3D_data {
	
	private IntBuffer indices;
	private int chain_length;
	
	public Draw_3D_Chain(){
		super();
		indices = null;
		chain_length = 0;
	}
	
	public void add_chain(int[] chain){
		//this.indices = chain;
		this.chain_length = chain.length;
		this.indices = Buffers.newDirectIntBuffer(chain_length);
		//this.normals = Buffers.newDirectFloatBuffer(faces_to_draw*12);

		for(int i = 0; i < chain.length; i++){
			this.indices.put(chain[i]);
		}
		
		this.indices.rewind();
		//this.normals.rewind();
	}

	@Override
	public void draw(GL2 gl) {
		if(this.vertices != null && this.indices != null){
			
			gl.glVertexPointer(3, GL2.GL_DOUBLE, 0, this.vertices);
			//gl.glDrawElements(GL2.GL_LINE, chain_length, GL2.GL_UNSIGNED_INT, indices);
			gl.glDrawArrays(GL2.GL_LINES, 0, chain_length);
			
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
	
}
