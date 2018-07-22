package com.srsw.icfp2018.model;

public class Vector3 {
	public int x, y, z;

	public static final Vector3 down = new Vector3(0, -1, 0);

	public Vector3(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(Vector3 v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	@Override
	public String toString() {
		return "<" + x + "," + y + "," + z + ">";
	}

	public void move(Vector3 d) {
		x += d.x;
		y += d.y;
		z += d.z;
	}

	public int mlen() {
		return Math.abs(x) + Math.abs(y) + Math.abs(z);
	}

	public int mdist(Vector3 v) {
		return Math.abs(x - v.x) + Math.abs(y - v.y) + Math.abs(z - v.z);
	}
}
