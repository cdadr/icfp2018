package com.srsw.icfp2018.model;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Matrix<T> {

	public Map<Integer, T> matrix = new HashMap<>();
	
	public Vector3 dimensions;
	
	
	private Integer index(int x, int y, int z) {
		if ((x > dimensions.x) || (x < 0) ||
			(y > dimensions.y) || (y < 0) ||
			(z > dimensions.z) || (z < 0))
		{
			throw new RuntimeException("Bad coordinates: <" + x + "," + y + "," + z + "> out of range " + dimensions);
		}
		return (((z * dimensions.y) + y) * dimensions.x) + x;
	}
	
	public void put(int x, int y, int z, T item) {
		matrix.put(index(x, y, z), item);
	}
	
	public void put(Vector3 v, T item) {
		put(v.x, v.y, v.z, item);
	}

	public T get(int x, int y, int z) {
		Integer index = index(x, y, z);
		if (matrix.containsKey(index)) {
			return matrix.get(index);
		} else {
			return null;
		}
	}
	
	public T get(Vector3 v) {
		return get(v.x, v.y, v.z);
	}
	
	public void clear(int x, int y, int z) {
		Integer index = index(x, y, z);
		matrix.remove(index);
	}

	public void clear(Vector3 v) {
		clear(v.x, v.y, v.z);
	}
	
	public void print(PrintStream out) {
		for (int y = 0; y < dimensions.y; y++) {
			out.println("y: " + y);
			
			for (int z = dimensions.z - 1; z >= 0; z--) {
				out.print("  ");
				for (int x = 0; x < dimensions.x; x++) {
					if (get(x, y, z) == null) {
						out.print(".");
					} else {
						out.print("*");
					}
				}
				out.println();
			}
		}
	}
}
