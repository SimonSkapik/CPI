package pkg.skapik.cpi.assets;

import javax.media.opengl.GL2;

public class Object_3D {

	private String name;
	private int id;
	private int mat_id;
	private Object_data data;
	
	public Object_3D(int id, String name, int mat_id, Object_data data){
		this.id = id;
		this.name = name;
		this.mat_id = mat_id;
		this.data = data;
	}

	public void draw(GL2 gl, Materials materials) {
		
		if(this.data.get_draw_data() != null){
			materials.set_material(gl, mat_id);
			this.data.get_draw_data().draw(gl);
		}
		
	}

	public int count_drawable() {
		if(this.data.get_draw_data().is_drawable()){
			return 1;
		}
		return 0;
	}
	
}
