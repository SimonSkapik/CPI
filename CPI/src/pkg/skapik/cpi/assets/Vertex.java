package pkg.skapik.cpi.assets;

public class Vertex {

	private int id;
	private float x,y,z;
	
	public Vertex(int id, float x, float y, float z){
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float get_x(){
		return this.x;
	}

	public float get_y(){
		return this.y;
	}

	public float get_z(){
		return this.z;
	}

	public int get_id() {
		return this.id;
	}
	
}
