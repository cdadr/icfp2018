package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.OutputStream;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.Harmonics;
import com.srsw.icfp2018.model.ModelRuntimeException;
import com.srsw.icfp2018.model.State;
import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.Vector3;

public class Halt extends Trace {
	@Override
	public String toString() {
		return "Halt";
	}
	
	@Override
	public void execute(Bot bot) throws ModelRuntimeException {
		State state = bot.state;
		if (!bot.pos.equals(Vector3.zero)) {
			throw new ModelRuntimeException("Halt: not at origin: pos=" + bot.pos);
		}
		if (state.bots.size() > 1) {
			throw new ModelRuntimeException("Halt: other active bots");
		}
		if (state.harmonics != Harmonics.Low) {
			throw new ModelRuntimeException("Halt: harmonics = " + state.harmonics);
		}
		state.bots.remove(bot);
	}


	@Override
	public void write(OutputStream out) throws IOException {
		out.write(0xff);
	}
}
