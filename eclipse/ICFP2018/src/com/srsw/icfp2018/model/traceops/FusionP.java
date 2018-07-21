package com.srsw.icfp2018.model.traceops;

import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.Vector3;

public class FusionP extends Trace {

	public Vector3 nd;
	
	public FusionP(int i) {
		nd = decodeND(i);
	}
	
	@Override
	public String toString() {
		return "FusionP " + nd;
	}
}
