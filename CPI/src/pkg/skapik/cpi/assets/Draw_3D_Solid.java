package pkg.skapik.cpi.assets;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

import pkg.skapik.cpi.functions.Normal_calculator;
import pkg.skapik.cpi.functions.Vector;

public class Draw_3D_Solid extends Draw_3D_data {
	
	private ArrayList<Face> faces;
	private int triangle_count;
	private IntBuffer indices;
	private DoubleBuffer normals;
	
	public Draw_3D_Solid(){
		super();
		faces = new ArrayList<>();
		triangle_count = 0;
		indices = null;
	}
	
	public void add_face(Face f){
		this.faces.add(f);
		triangle_count += f.get_triangle_count();
	}
	
	@Override
	public void draw(GL2 gl){
		if(this.vertices != null && this.compiled){
			for(Face F : this.faces){
				F.draw(gl, this.vertices);
			}
		}
		/*
			gl.glVertexPointer(3, GL2.GL_DOUBLE, 0, this.vertices);
			gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			gl.glNormalPointer(GL2.GL_DOUBLE, 0, this.normals);
			gl.glDrawElements(GL2.GL_TRIANGLES, triangle_count*3, GL2.GL_UNSIGNED_INT, indices);
			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
		*/
	}

	@Override
	public void compile_indices() {
		
		for(Face F : this.faces){
			F.compile_indices(this.vertices);
		}
		
		/*
		Vector normal;
		int vertex_count = this.vertices.capacity()/3;
		this.normals = Buffers.newDirectDoubleBuffer(vertex_count*3);
		this.indices = Buffers.newDirectIntBuffer(triangle_count*3);
		ArrayList<Vector>[] normal_group_list = new ArrayList[vertex_count];
		for(int i = 0; i < vertex_count; i++){
			normal_group_list[i] = new ArrayList<>();
		}
		
		for(Face F : this.faces){
			F.get_normals(this.vertices, normal_group_list);
			for(Triangle T : F.get_triangles()){
				this.indices.put(T.get_i1());
				this.indices.put(T.get_i2());
				this.indices.put(T.get_i3());
			}
		}
		
		for(int i = 0; i < normal_group_list.length; i++){
			normal = Normal_calculator.average_vector(normal_group_list[i]);
			normal.normalize();
			normals.put(normal.getX_D());
			normals.put(normal.getY_D());
			normals.put(normal.getZ_D());
		}
		
		this.normals.rewind();
		this.indices.rewind();*/
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
