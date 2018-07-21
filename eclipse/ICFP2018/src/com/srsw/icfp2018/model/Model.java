package com.srsw.icfp2018.model;

import java.io.FileInputStream;
import java.io.IOException;

public class Model {

	public int r;
	byte[] rawBits;
	boolean[][][] bits;
	
	public void load(FileInputStream in) throws IOException {
		r = in.read();
		if ((r <= 0) || (r > 250)) {
			throw new IOException("Bad model size: " + r);
		}
		
		int nBytes = ((r * r * r) + 7) / 8;
		rawBits = new byte[nBytes];
		
		System.out.println("r=" + r + "; reading " + nBytes + " bytes");
		
		int pos = 0;
		while (pos < nBytes) {
			int bytesRead = in.read(rawBits, pos, nBytes - pos);
			if (bytesRead == -1) {
				throw new IOException("EOF reading model, pos=" + pos);
			}
			pos += bytesRead;
		}
		
		if (in.read() != -1) {
			throw new IOException("Extra garbage in file: pos=" + pos);
		}
		
		bits = new boolean[r][][];
		for (int z = 0; z < r; z++) {
			bits[z] = new boolean[r][];
			for (int y = 0; y < r; y++) {
				bits[z][y] = new boolean[r];
				for (int x = 0; x < r; x++) {
					bits[z][y][x] = get0(x, y, z);
				}
			}
		}
	}

	private boolean get0(int x, int y, int z) {
		int bitpos = (((x * r) + y) * r) + z;
		int index = bitpos / 8;
		int offset = bitpos % 8;
		
		byte b = rawBits[index];
		return (b & (1 << offset)) != 0;
	}

	public boolean get(int x, int y, int z) {
		return bits[z][y][x];
	}

	public boolean getPlaneBBox(int y, Vector3 bboxL, Vector3 bboxH) {
		boolean anyFound = false;
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxZ = Integer.MIN_VALUE;
		
		for (int x = 0; x < r; x++) {
			for (int z = 0; z < r; z++) {
				if (get(x, y, z)) {
					minX = Math.min(minX, x);
					maxX = Math.max(maxX, x);
					minZ = Math.min(minZ, z);
					maxZ = Math.max(maxZ, z);
					anyFound = true;
				}
			}
		}
		
		if (anyFound) {
			bboxL.x = minX;
			bboxL.y = y;
			bboxL.z = minZ;
			
			bboxH.x = maxX;
			bboxH.y = y;
			bboxH.z = maxZ;
		}
		return anyFound;
	}
	

	public boolean getLineBBox(int y, int z, Vector3 bboxL, Vector3 bboxH) {
		boolean anyFound = false;
		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		
		for (int x = 0; x < r; x++) {
			if (get(x, y, z)) {
				minX = Math.min(minX, x);
				maxX = Math.max(maxX, x);
				anyFound = true;
			}
		}
		
		if (anyFound) {
			bboxL.x = minX;
			bboxL.y = y;
			bboxL.z = z;
			
			bboxH.x = maxX;
			bboxH.y = y;
			bboxH.z = z;
		}
		return anyFound;
	}
}
