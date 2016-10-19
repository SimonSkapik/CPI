package pkg.skapik.cpi.functions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Normal_calculator {
	
	public static Vector triangle_normal(double p1x, double p1y, double p1z, double p2x, double p2y, double p2z, double p3x, double p3y, double p3z){
		return cross_product(new Vector(p2x-p1x, p2y-p1y, p2z-p1z), new Vector(p3x-p1x, p3y-p1y, p3z-p1z));
	}
	
	public static Vector cross_product(Vector u, Vector v){
		return new Vector(u.getY_D() * v.getZ_D() - v.getY_D() * u.getZ_D(),
							u.getX_D() * v.getZ_D() - v.getX_D() * u.getZ_D(),
							u.getX_D() * v.getY_D() - v.getX_D() * u.getY_D());
	}

	public static Vector average_vector(ArrayList<Vector> vector_list) {
		double count = vector_list.size();
		if(count == 0){
			return new Vector(0,0,0);
		}
		
		ArrayList<Vector> filtered = new ArrayList<>();
		for(Vector V : vector_list){
			if(!contains_vector(filtered, V))
				filtered.add(V);
		}
		if(vector_list.size() == filtered.size()){
			System.out.print("same :-/");
		}else if(vector_list.size() > filtered.size()){
			System.err.print("reduced");
		}else{
			System.out.print("INCRESED???? WTF?????");
		}
		
		double x = 0;
		double y = 0;
		double z = 0;

		for(Vector V : filtered){
			x += V.getX_D();
			y += V.getY_D();
			z += V.getZ_D();
		}
		return new Vector(x/count, y/count, z/count);
	}
	
	private static boolean contains_vector(ArrayList<Vector> list, Vector vector){
		for(Vector V : list){
			if(Math.abs(V.getX_D()-vector.getX_D()) < 0.00001 && Math.abs(V.getY_D()-vector.getY_D()) < 0.00001 && Math.abs(V.getZ_D()-vector.getZ_D()) < 0.00001)
				return true;
		}
		return false;
	}
	
}
