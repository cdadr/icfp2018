package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.OutputStream;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.State;
import com.srsw.icfp2018.model.Trace;

public class Halt extends Trace {
	@Override
	public String toString() {
		return "Halt";
	}
	
	@Override
	public void execute(Bot bot) {
		// TODO validate conditions
		State state = bot.state;
		state.bots.remove(bot);
	}


	@Override
	public void write(OutputStream out) throws IOException {
		out.write(0xff);
	}
}
