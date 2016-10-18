package pkg.skapik.cpi.assets;

import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GL2;

public class Materials {

	private Material default_material;
	
	private Map<Integer, Material> materials;
	
	public Materials(){
		this.materials = new HashMap<Integer, Material>();
		this.default_material = new Material(0, "Default" ,255, 0, 0, 1, 255, 0, 255, 1, 230, 230, 230, 1, 0);
	}
	
	public void add_material(Material mat){
		this.materials.put(mat.get_id(), mat);
	}
	
	public void set_material(GL2 gl, int mat_id){
		if(materials.containsKey(mat_id)){
			this.materials.get(mat_id).set_material(gl);
		}else{
			default_material.set_material(gl);
		}
	}
	
}
