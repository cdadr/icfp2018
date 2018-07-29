package com.srsw.icfp2018.model;

public class Vector3 {

	public int x, y, z;

	public static final Vector3 zero = new Vector3(0,0,0);
	public static final Vector3 down = new Vector3(0, -1, 0);
	public static final Vector3 left = new Vector3(-1, 0, 0);
	public static final Vector3 right = new Vector3( 1, 0, 0);

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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector3) {
			Vector3 v = (Vector3) obj;
			return (x == v.x) && (y == v.y) && (z == v.z);
		} else {
			return false;
		}
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

	public void validate(int r) throws ModelRuntimeException {
		if ((x < 0) || (x >= r) || (y < 0) || (y >= r) || (z < 0) || (z >= r)) {
			throw new ModelRuntimeException("Position out of range: " + this);
		}
	}
}
