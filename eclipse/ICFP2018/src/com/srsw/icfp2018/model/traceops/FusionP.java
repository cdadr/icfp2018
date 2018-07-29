package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.OutputStream;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.ModelRuntimeException;
import com.srsw.icfp2018.model.Vector3;

public class FusionP extends FusionBase {

	public FusionP(int i) {
		super(i);
	}
	
	public FusionP(Vector3 nd) {
		super(nd);
	}

	@Override
	public String toString() {
		return "FusionP " + nd;
	}
	
	@Override
	public void write(OutputStream out) throws IOException {
		int i = ((nd.x + 1) * 9) + ((nd.y + 1) * 3) + (nd.z + 1);
		out.write((i << 3) | 0x07);
	}
	
	@Override
	public void execute(Bot bot) throws ModelRuntimeException {
		executeFusion(bot, true);
	}
}
