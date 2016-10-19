package pkg.skapik.cpi.assets;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.media.opengl.GL2;

import com.jogamp.common.nio.Buffers;

import pkg.skapik.cpi.functions.Normal_calculator;
import pkg.skapik.cpi.functions.Vector;

public class Face {
	
	private ArrayList<Triangle> triangles;
	private int triangle_count;
	private String shape;
	private IntBuffer indices;
	private DoubleBuffer normals;
	
	public Face(String sh){
		this.triangles = new ArrayList<>();
		this.triangle_count = 0;
		this.shape = sh;
		this.indices = null;
	}
	
	public void add_triangle(Triangle t){
		triangles.add(t);
		triangle_count++;
	}

	public int get_triangle_count() {
		return this.triangle_count;
	}

	public ArrayList<Triangle> get_triangles() {
		return triangles;
	}

	public void get_normals(DoubleBuffer vertices, ArrayList<Vector>[] normal_group_list) {
		Vector normal = null;
		int p1x_ind, p2x_ind, p3x_ind;
		
		for(Triangle T : this.triangles){
			p1x_ind = (T.get_i1())*3;
			p2x_ind = (T.get_i2())*3;
			p3x_ind = (T.get_i3())*3;
			normal = Normal_calculator.triangle_normal(vertices.get(p1x_ind), vertices.get(p1x_ind+1), vertices.get(p1x_ind+2),
														vertices.get(p2x_ind), vertices.get(p2x_ind+1), vertices.get(p2x_ind+2),
														vertices.get(p3x_ind), vertices.get(p3x_ind+1), vertices.get(p3x_ind+2));
			normal.normalize();
			normal_group_list[T.get_i1()].add(new Vector(normal));
			normal_group_list[T.get_i2()].add(new Vector(normal));
			normal_group_list[T.get_i3()].add(new Vector(normal));
		}
		
	}

	public void draw(GL2 gl, DoubleBuffer vertices) {
		if(vertices != null && indices != null){
			gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
			gl.glNormalPointer(GL2.GL_DOUBLE, 0, this.normals);
			gl.glVertexPointer(3, GL2.GL_DOUBLE, 0, vertices);
			gl.glDrawElements(GL2.GL_TRIANGLES, triangle_count*3, GL2.GL_UNSIGNED_INT, indices);
			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
		}
		
	}

	public void compile_indices(DoubleBuffer vertices) {
		this.indices = Buffers.newDirectIntBuffer(triangle_count*3);
		Vector normal;
		int vertex_count = vertices.capacity()/3;
		this.normals = Buffers.newDirectDoubleBuffer(vertex_count*3);
		this.indices = Buffers.newDirectIntBuffer(triangle_count*3);
		ArrayList<Vector>[] normal_group_list = new ArrayList[vertex_count];
		for(int i = 0; i < vertex_count; i++){
			normal_group_list[i] = new ArrayList<>();
		}
		
		get_normals(vertices, normal_group_list);
		for(int i = 0; i < normal_group_list.length; i++){
			normal = Normal_calculator.average_vector(normal_group_list[i]);
			normal.normalize();
			normals.put(normal.getX_D());
			normals.put(normal.getY_D());
			normals.put(normal.getZ_D());
		}
		
		for(Triangle T : this.triangles){
			this.indices.put(T.get_i1());
			this.indices.put(T.get_i2());
			this.indices.put(T.get_i3());
		}
		this.indices.rewind();
		this.normals.rewind();
	}
	
}
