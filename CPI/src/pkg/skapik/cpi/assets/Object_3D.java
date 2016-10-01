package pkg.skapik.cpi.assets;

public class Object_3D {

	private String name;
	private int id;
	private int mat_id;
	private Draw_3D_data draw_data;
	
	public Object_3D(int id, String name, int mat_id){
		this.id = id;
		this.name = name;
		this.mat_id = mat_id;
		this.draw_data = null;
	}
	
	public void set_draw_data(Draw_3D_data data){
		this.draw_data = data;
	}
	
}
