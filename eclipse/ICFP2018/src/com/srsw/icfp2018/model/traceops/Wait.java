package com.srsw.icfp2018.model.traceops;

import java.io.FileOutputStream;
import java.io.IOException;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.Trace;

public class Wait extends Trace {
	@Override
	public String toString() {
		return "Wait";
	}

	@Override
	public void execute(Bot bot) {
		// Wait does nothing
	}

	@Override
	public void write(FileOutputStream out) throws IOException {
		out.write(0xfe);
	}
}
