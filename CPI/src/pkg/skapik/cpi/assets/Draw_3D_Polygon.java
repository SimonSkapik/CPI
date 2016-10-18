package pkg.skapik.cpi.assets;

import java.nio.IntBuffer;

import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

public class Draw_3D_Polygon extends Draw_3D_data {
	
	private Face face;
	private int triangle_count;
	private IntBuffer indices;
	
	public Draw_3D_Polygon(){
		super();
		face = null;
		triangle_count = 0;
		indices = null;
	}
	
	public void add_face(Face f){
		this.face = f;
		triangle_count += f.get_triangle_count();
	}

	@Override
	public void draw(GL2 gl) {
		if(this.vertices != null && this.compiled){
			
			gl.glVertexPointer(3, GL2.GL_DOUBLE, 0, this.vertices);
			gl.glDrawElements(GL2.GL_TRIANGLES, triangle_count*3, GL2.GL_UNSIGNED_INT, indices);
			
		}
	}

	@Override
	public void compile_indices() {
		this.indices = Buffers.newDirectIntBuffer(triangle_count*3);
		for(Triangle T : face.get_triangles()){
			this.indices.put(T.get_i1());
			this.indices.put(T.get_i2());
			this.indices.put(T.get_i3());
		}
		this.indices.rewind();
		this.compiled = true;
	}

	@Override
	public boolean is_drawable() {
		if(this.vertices != null && this.compiled){
			return true;
		}
		return false;
	}
	
}
