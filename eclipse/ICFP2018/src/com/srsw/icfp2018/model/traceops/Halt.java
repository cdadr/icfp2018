package com.srsw.icfp2018.model.traceops;

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
}
