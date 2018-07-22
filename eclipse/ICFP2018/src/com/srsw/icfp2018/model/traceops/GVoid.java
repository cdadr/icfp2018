package com.srsw.icfp2018.model.traceops;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.TraceFileException;
import com.srsw.icfp2018.model.Vector3;

public class GVoid  extends Trace {

	public final Vector3 nd;
	public final Vector3 fd;
	
	public GVoid(int nd, InputStream in) throws TraceFileException {
		this.nd = decodeND(nd);
		int x;
		try {
			x = in.read();
			int y = in.read();
			int z = in.read();
			if ((x == -1) || (y == -1) || (z == -1)) {
				throw new EOFException();
			}
			fd = new Vector3(x, y, z);
		} catch (IOException e) {
			throw new TraceFileException("Error reading fd bytes of GVoid", e);
		}
	}
	
	public GVoid(Vector3 nd, Vector3 fd) {
		assertND(nd);
		this.nd = nd;
		this.fd = fd;
	}


	@Override
	public String toString() {
		return "GVoid " + nd + " " + fd;
	}

}
