package pkg.skapik.cpi.assets;

import java.awt.event.ContainerEvent;
import java.util.ArrayList;

import javax.media.opengl.GL2;

public class Container {

	private String name;
	private int id;
	
	private ArrayList<Container> children;
	private ArrayList<Object_3D> objects;
	private Object_data data;
	private int[] vTrans;
	private boolean composite;
	
	public Container(int id, String name, Object_data data){
		this.id = id;
		this.name = name;
		this.vTrans = null;
		this.composite = false;
		this.children = new ArrayList<>();
		this.objects = new ArrayList<>();
		this.data = data;
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

	public void draw(GL2 gl, Materials materials) {
		for(Container C : this.children){
			C.draw(gl, materials);
		}
		for(Object_3D O : this.objects){
			O.draw(gl, materials);
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
}
