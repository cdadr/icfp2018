package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.InputStream;

import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.TraceFileException;
import com.srsw.icfp2018.model.Vector3;

public class Fission extends Trace {

	public Vector3 nd;
	public int m;

	public Fission(int i, InputStream in) throws TraceFileException {
		nd = decodeND(i);
		try {
			m = in.read();
			if (m == -1) {
				throw new TraceFileException("EOF in 2nd byte of Fission");
			}
		} catch (IOException e) {
			throw new TraceFileException("Error reading 2nd byte of Fission", e);
		}
	}
	
	@Override
	public String toString() {
		return "Fission " + nd + " " + m;
	}
}
