package pkg.skapik.cpi.assets;

public class Vertex {

	private int id;
	private double x,y,z;
	
	public Vertex(int id, double x, double y, double z){
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public double get_x(){
		return this.x;
	}

	public double get_y(){
		return this.y;
	}

	public double get_z(){
		return this.z;
	}

	public int get_id() {
		return this.id;
	}
	
}
