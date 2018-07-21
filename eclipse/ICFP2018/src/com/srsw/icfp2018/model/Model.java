package com.srsw.icfp2018.model;

import java.io.FileInputStream;
import java.io.IOException;

public class Model {

	public int r;
	byte[] bits;
	
	public void load(FileInputStream in) throws IOException {
		r = in.read();
		if ((r <= 0) || (r > 250)) {
			throw new IOException("Bad model size: " + r);
		}
		
		int nBytes = ((r * r * r) + 7) / 8;
		bits = new byte[nBytes];
		
		System.out.println("r=" + r + "; reading " + nBytes + " bytes");
		
		int pos = 0;
		while (pos < nBytes) {
			int bytesRead = in.read(bits, pos, nBytes - pos);
			if (bytesRead == -1) {
				throw new IOException("EOF reading model, pos=" + pos);
			}
			pos += bytesRead;
		}
		
		if (in.read() != -1) {
			throw new IOException("Extra garbage in file: pos=" + pos);
		}
	}

	public boolean get(int x, int y, int z) {
		int bitpos = (((x * r) + y) * r) + z;
		int index = bitpos / 8;
		int offset = bitpos % 8;
		
		byte b = bits[index];
		return (b & (1 << offset)) != 0;
	}

}
