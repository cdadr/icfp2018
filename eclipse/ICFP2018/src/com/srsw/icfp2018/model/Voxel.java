package com.srsw.icfp2018.model;

public class Voxel {

	public Vector3 pos;
	
	public Voxel(Vector3 v1, Vector3 v2) {
		pos = new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}

	public Voxel(int x, int y, int z) {
		pos = new Vector3(x, y, z);
	}
}
