package pkg.skapik.cpi.functions;

public class Position {

	private float x,y,z;

	public Position(int i, int j, int k) {
		this.x = (float)i;
		this.y = (float)j;
		this.z = (float)k;
	}
	
	public Position(float i, float j, float k) {
		this.x = i;
		this.y = j;
		this.z = k;

	}
	
	public Position(double i, double j, double k) {
		this.x = (float)i;
		this.y = (float)j;
		this.z = (float)k;
	}
	
	public Position(Position position) {
		this(position.getX_D(),position.getY_D(),position.getZ_D());
	}

	public double getX_D(){
		return (double)this.x;
	}

	public double getY_D(){
		return (double)this.y;
	}

	public double getZ_D(){
		return (double)this.z;
	}

	public int getX_I(){
		return (int)this.x;
	}

	public int getY_I(){
		return (int)this.y;
	}

	public int getZ_I(){
		return (int)this.z;
	}

	public float getX_F(){
		return this.x;
	}

	public float getY_F(){
		return this.y;
	}

	public float getZ_F(){
		return this.z;
	}

	public void move_by(Vector move_direction, float speed) {
		this.x += move_direction.getX_F()*speed;
		this.y += move_direction.getY_F()*speed;
		this.z += move_direction.getZ_F()*speed;
	}
	
	public void setX(double d) {
		this.x = (float) d;
	}

	public void setY(double d) {
		this.y = (float) d;
	}

	public void setZ(double d) {
		this.z = (float) d;
		
	}

	public void addX(double add) {
		this.x += add;
	}

	public void addY(double add) {
		this.y += add;
	}

	public void addZ(double add) {
		this.z += add;
	}

	public void set(double x2, double y2, double z2) {
		this.x = (float) x2;
		this.y = (float) y2;
		this.z = (float) z2;
		
	}

	public float distance_to_3d(Position pos) {
		float dx = this.x-pos.x;
		float dy = this.y-pos.y;
		float dz = this.z-pos.z;
		return (float)Math.sqrt(dx*dx+dy*dy+dz*dz);
	}

	public float distance_to_2d(Position pos) {
		float dx = this.x-pos.x;
		float dz = this.z-pos.z;
		return (float)Math.sqrt(dx*dx+dz*dz);
	}
	
	public void rotate(Vector axis, float angle) {
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		double[][] matrix = new double[3][3];
		double ax = axis.getX_D();
		double ay = axis.getY_D();
		double az = axis.getZ_D();
		matrix[0][0] = cos + ax*ax*(1 - cos);
		matrix[0][1] = ax*ay*(1 - cos) - az*sin;
		matrix[0][2] = ax*az*(1 - cos) + ay*sin;
		matrix[1][0] = ay*ax*(1 - cos) + az*sin;
		matrix[1][1] = cos + ay*ay*(1 - cos);
		matrix[1][2] = ay*az*(1 - cos) - ax*sin;
		matrix[2][0] = az*ax*(1 - cos) - ay*sin;
		matrix[2][1] = az*ay*(1 - cos) + ax*sin;
		matrix[2][2] = cos + az*az*(1 - cos);
		this.multiply_from_left(matrix);
	}
	
	private void multiply_from_left(double[][] M) {
		// new_vecotr =  M * this_vector
		double new_x = 0;
		double new_y = 0;
		double new_z = 0;
		new_x = M[0][0]*x + M[0][1]*y + M[0][2]*z;
		new_y = M[1][0]*x + M[1][1]*y + M[1][2]*z;
		new_z = M[2][0]*x + M[2][1]*y + M[2][2]*z;
		x = (float)new_x;
		y = (float)new_y;
		z = (float)new_z;
	}

	@Override
	public String toString(){
		return "["+this.x+", "+this.y+", "+this.z+"]";
	}

}
