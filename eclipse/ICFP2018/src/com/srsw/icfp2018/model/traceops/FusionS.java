package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.OutputStream;

import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.Vector3;

public class FusionS extends Trace {

	public Vector3 nd;

	public FusionS(int i) {
		nd = decodeND(i);
	}
	
	public FusionS(Vector3 nd) {
		assertND(nd);
		this.nd = nd;
	}

	@Override
	public String toString() {
		return "FusionS " + nd;
	}
	
	@Override
	public void write(OutputStream out) throws IOException {
		int i = ((nd.x + 1) * 9) + ((nd.y + 1) * 3) + (nd.z + 1);
		out.write((i << 3) | 0x06);
	}
}
