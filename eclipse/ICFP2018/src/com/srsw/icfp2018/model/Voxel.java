package com.srsw.icfp2018.model;

public class Voxel {

	public enum GroundState { Unknown, Grounded, Floating }
	
	public final Vector3 pos;
	public GroundState groundState;
	
	public Voxel(int x, int y, int z) {
		pos = new Vector3(x, y, z);
		groundState = (y == 0) ? GroundState.Grounded : GroundState.Unknown;
	}
	
	public Voxel(Vector3 v1, Vector3 v2) {
		this(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
}
