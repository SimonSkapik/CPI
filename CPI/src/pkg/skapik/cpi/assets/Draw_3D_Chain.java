package pkg.skapik.cpi.assets;

import java.util.ArrayList;

public class Draw_3D_Chain extends Draw_3D_data {
	
	private int[] indices;
	
	public Draw_3D_Chain(){
		super();
	}
	
	public void add_chain(int[] chain){
		this.indices = chain;
	}

	@Override
	public void draw() {
		if(this.vertices != null){
			
			
			
		}
	}

	@Override
	public void compile_indices() {
		// TODO Auto-generated method stub
		
	}
	
}
