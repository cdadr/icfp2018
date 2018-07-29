package com.srsw.icfp2018.model.traceops;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.Trace;

public class Nop extends Trace {

	private final String msg;

	public Nop(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return "NOP (was " + msg + ")";
	}
	
	@Override
	public void execute(Bot bot) {
		// nop
	}
}
