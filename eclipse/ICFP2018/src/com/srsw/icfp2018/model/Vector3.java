package com.srsw.icfp2018.model;

public class Vector3 {
	public int x, y, z;
	
	public Vector3(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
}
