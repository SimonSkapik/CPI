package pkg.skapik.cpi.assets;

public class Triangle {
	
	private int ind1,ind2,ind3;

	public Triangle(int i1, int i2, int i3){
		this.ind1 = i1;
		this.ind2 = i2;
		this.ind3 = i3;
	}
	
	public int get_i1(){
		return ind1;
	}

	public int get_i2(){
		return ind2;
	}
	
	public int get_i3(){
		return ind3;
	}
}
