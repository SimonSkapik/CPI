package pkg.skapik.cpi.assets;

import javax.media.opengl.GL2;

public class Material {
	
	private float[] diffuse;
	private float[] ambient;
	private float[] specular;
	private float transparent;
	private int id;
	private String name;
	
	public Material(int mat_id, String name, int dr, int dg, int db, float da, int ar, int ag, int ab, float aa, int sr, int sg, int sb, float sa, float trans){
		this.id = mat_id;
		this.name = name;
		transparent = trans;
		trans = 1-trans;
		diffuse = new float[]{dr/255.0f,dg/255.0f,db/255.0f,da*trans};
		ambient = new float[]{ar/255.0f,ag/255.0f,ab/255.0f,aa*trans};
		specular = new float[]{sr/255.0f,sg/255.0f,sb/255.0f,sa*trans};
	}
	
	public Material(int mat_id, String name, float[] diff, float[] amb, float[] spec, float trans){
		this.id = mat_id;
		this.name = name;
		transparent = trans;
		trans = 1-trans;
		diffuse = diff.clone();
		ambient = amb.clone();
		specular = spec.clone();
		diffuse[3] *= trans;
		ambient[3] *= trans;
		specular[3] *= trans;
	}

	public void set_material(GL2 gl) {
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT, ambient, 0);
		gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, specular, 0);
	}
	
	public void set_transparency(float trans){
		this.transparent = trans;
		diffuse[3] = 1-trans;
		ambient[3] = 1-trans;
		specular[3] = 1-trans;
	}
	
	public boolean is_transparent(){
		if(transparent == 0)
			return false;
		return true;
			
	}

	public int get_id() {
		return this.id;
	}

}
