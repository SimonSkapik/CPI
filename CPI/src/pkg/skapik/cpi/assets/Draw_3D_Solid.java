package pkg.skapik.cpi.assets;

import java.util.ArrayList;

public class Draw_3D_Solid extends Draw_3D_data {
	
	private ArrayList<Face> faces;
	
	public Draw_3D_Solid(){
		super();
		faces = new ArrayList<>();
	}
	
	public void add_face(Face f){
		this.faces.add(f);
	}
	
	@Override
	public void draw(){
		if(this.vertices != null && this.compiled){
			
			
			
		}
	}

	@Override
	public void compile_indices() {
		
	}
	
}
