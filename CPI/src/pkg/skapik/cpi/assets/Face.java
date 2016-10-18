package pkg.skapik.cpi.assets;

import java.util.ArrayList;

public class Face {
	
	private ArrayList<Triangle> triangles;
	private int triangle_count;
	private String shape;
	
	public Face(String sh){
		triangles = new ArrayList<>();
		this.triangle_count = 0;
		this.shape = sh;
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
	
}
