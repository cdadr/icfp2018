package com.srsw.icfp2018.model;

public class Region {
	public final Vector3 cornerL;
	public final Vector3 cornerH;
	
	public Region(Vector3 v0, Vector3 v1) {
		final int minX, maxX, minY, maxY, minZ, maxZ;
		if (v0.x > v1.x) {
			minX = v1.x;
			maxX = v0.x;
		} else {
			minX = v0.x;
			maxX = v1.x;
		}
		if (v0.y > v1.y) {
			minY = v1.y;
			maxY = v0.y;
		} else {
			minY = v0.y;
			maxY = v1.y;
		}
		if (v0.z > v1.z) {
			minZ = v1.z;
			maxZ = v0.z;
		} else {
			minZ = v0.z;
			maxZ = v1.z;
		}
		
		cornerL = new Vector3(minX, minY, minZ);
		cornerH = new Vector3(maxX, maxY, maxZ);
	}
	
	@Override
	public String toString() {
		return "{Region:" + cornerL + "::" + cornerH + "}";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Region) {
			Region r = (Region) obj;
			return cornerL.equals(r.cornerL) && cornerH.equals(cornerH);
		} else {
			return false;
		}
	}
	
}
