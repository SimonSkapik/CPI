package pkg.skapik.cpi.assets;

import java.util.ArrayList;

public class Face {
	
	private ArrayList<Triangle> triangles;
	
	public Face(){
		triangles = new ArrayList<>();
	}
	
	public void add_triangle(Triangle t){
		triangles.add(t);
	}
	
}
