package pkg.skapik.cpi.assets;

public class Object_data {

	private String key;
	private Draw_3D_data draw_data;
	
	public Object_data(String key){
		this.key = key;
		this.draw_data = null;
	}
	
	public void add_draw_data(Draw_3D_data data){
		this.draw_data = data;
	}

	public Draw_3D_data get_draw_data() {
		return this.draw_data;
	}
	
}
