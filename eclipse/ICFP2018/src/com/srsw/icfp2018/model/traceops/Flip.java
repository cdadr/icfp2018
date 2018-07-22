package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.OutputStream;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.Harmonics;
import com.srsw.icfp2018.model.State;
import com.srsw.icfp2018.model.Trace;

public class Flip extends Trace {
	@Override
	public String toString() {
		return "Flip";
	}
	
	
	@Override
	public void execute(Bot bot) {
		State state = bot.state;
		if (state.harmonics == Harmonics.High) {
			state.harmonics = Harmonics.Low;
		} else {
			state.harmonics = Harmonics.High;
		}
	}


	@Override
	public void write(OutputStream out) throws IOException {
		out.write(0xfd);
	}
}
