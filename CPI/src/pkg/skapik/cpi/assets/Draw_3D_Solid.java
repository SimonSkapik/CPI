package pkg.skapik.cpi.assets;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

import pkg.skapik.cpi.functions.Normal_calculator;
import pkg.skapik.cpi.functions.Vector;

public class Draw_3D_Solid extends Draw_3D_data {
	
	private ArrayList<Face> faces;
	private int triangle_count;
	private FloatBuffer normals;
	
	public Draw_3D_Solid(){
		super();
		faces = new ArrayList<>();
		triangle_count = 0;
	}
	
	public void add_face(Face f){
		this.faces.add(f);
		triangle_count += f.get_triangle_count();
	}
	
	@Override
	public void draw(GL2 gl){
		if(this.vertices != null && this.compiled){
			gl.glVertexPointer(3, GL2.GL_FLOAT, 0, this.vertices);
			gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			gl.glNormalPointer(GL2.GL_FLOAT, 0, this.normals);
			gl.glDrawArrays(GL2.GL_TRIANGLES, 0, triangle_count*3);
			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
		}
	}

	@Override
	public void compile_indices() {
		Vertex v1 = null;
		Vertex v2 = null;
		Vertex v3 = null;
		Vector normal = null;
		this.vertices = Buffers.newDirectFloatBuffer(this.triangle_count*9);
		this.normals = Buffers.newDirectFloatBuffer(this.triangle_count*9);
		for(Face F : this.faces){
			for(Triangle T : F.get_triangles()){
				v1 = this.vertex_list.get(T.get_i1());
				v2 = this.vertex_list.get(T.get_i2());
				v3 = this.vertex_list.get(T.get_i3());
				normal = Normal_calculator.triangle_normal(v1.get_x(), v1.get_y(), v1.get_z(), v2.get_x(), v2.get_y(), v2.get_z(), v3.get_x(), v3.get_y(), v3.get_z());
				normal.normalize();
				this.vertices.put( v1.get_x() );
				this.vertices.put( v1.get_y() );
				this.vertices.put( v1.get_z() );
				
				this.vertices.put( v2.get_x() );
				this.vertices.put( v2.get_y() );
				this.vertices.put( v2.get_z() );
				
				this.vertices.put( v3.get_x() );
				this.vertices.put( v3.get_y() );
				this.vertices.put( v3.get_z() );
				
				this.normals.put( normal.getX_F() );
				this.normals.put( normal.getY_F() );
				this.normals.put( normal.getZ_F() );
				
				this.normals.put( normal.getX_F() );
				this.normals.put( normal.getY_F() );
				this.normals.put( normal.getZ_F() );
				
				this.normals.put( normal.getX_F() );
				this.normals.put( normal.getY_F() );
				this.normals.put( normal.getZ_F() );
			}
		}
		
		this.vertices.rewind();
		this.normals.rewind();

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
