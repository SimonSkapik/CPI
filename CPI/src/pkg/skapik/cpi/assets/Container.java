package pkg.skapik.cpi.assets;

import java.util.ArrayList;

public class Container {

	private String name;
	private int id;
	
	private ArrayList<Container> children;
	private ArrayList<Object_3D> objects;
	private int[] vTrans;
	private boolean composite;
	
	public Container(int id, String name){
		this.id = id;
		this.name = name;
		this.vTrans = null;
		this.composite = false;
		this.children = new ArrayList<>();
		this.objects = new ArrayList<>();
	}
	
	public void set_vTrans(int[] params){
		vTrans = params;
	}
	
	public void set_composite(boolean state){
		composite = state;
	}

	public void add_container(Container container) {
		this.children.add(container);
	}

	public void add_object(Object_3D object_3d) {
		this.objects.add(object_3d);
	}
	
}
