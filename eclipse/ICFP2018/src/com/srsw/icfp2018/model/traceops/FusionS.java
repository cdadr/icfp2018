package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.OutputStream;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.ModelRuntimeException;
import com.srsw.icfp2018.model.Vector3;

public class FusionS extends FusionBase {

	public FusionS(int i) {
		super(i);
	}
	
	public FusionS(Vector3 nd) {
		super(nd);
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
	
	@Override
	public void execute(Bot bot) throws ModelRuntimeException {
		executeFusion(bot, false);
	}
}
