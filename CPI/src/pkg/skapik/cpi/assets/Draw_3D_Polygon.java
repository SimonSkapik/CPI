package pkg.skapik.cpi.assets;

public class Draw_3D_Polygon extends Draw_3D_data {
	
	private Face face;
	
	public Draw_3D_Polygon(){
		super();
	}
	
	public void add_face(Face f){
		this.face = f;
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
