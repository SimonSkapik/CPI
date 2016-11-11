package pkg.skapik.cpi.assets;

import java.util.ArrayList;

import javax.media.opengl.GL2;

public class Container {

	private String name;
	private int id;
	
	private ArrayList<Container> children;
	private ArrayList<Object_3D> objects;
	private ArrayList<Container> composites;
	private Object_3D composite_object;
	private Object_data data;
	private int[] vTrans;
	private boolean is_composite;
	private boolean has_composite;
	
	public Container(int id, String name, Object_data data){
		this.id = id;
		this.name = name;
		this.vTrans = null;
		this.is_composite = false;
		this.has_composite = false;
		this.children = new ArrayList<>();
		this.objects = new ArrayList<>();
		this.composites = new ArrayList<>();
		this.data = data;
	}
	
	public void set_vTrans(int[] params){
		vTrans = params;
	}
	
	public void set_composite(boolean state){
		is_composite = state;
	}

	public void add_container(Container container) {
		this.children.add(container);
	}

	public void add_object(Object_3D object_3d) {
		this.objects.add(object_3d);
	}

	public void draw(GL2 gl, Materials materials) {
		for(Container C : this.children){
			C.draw(gl, materials);
		}
		for(Object_3D O : this.objects){
			O.draw(gl, materials);
		}
		if(has_composite && this.composite_object != null){
			this.composite_object.draw(gl, materials);
		}
	}

	public int count_drawable() {
		int count = 0;
		for(Container C : this.children){
			count += C.count_drawable();
		}
		for(Object_3D O : this.objects){
			count += O.count_drawable();
		}
		return count;
	}

	public void add_composite(Container con) {
		this.has_composite = true;
		con.set_composite(true);
		this.composites.add(con);
	}

	public boolean has_composites() {
		return this.has_composite;
	}

	public ArrayList<Container> get_containers() {
		return this.children;
	}

	public ArrayList<Container> get_composites() {
		return this.composites;
	}
 
	public ArrayList<Object_3D> get_objects() {
		return this.objects;
	}

	public void add_composite_object(Object_3D comp_obj) {
		this.composite_object = comp_obj;
	}
}
