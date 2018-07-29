package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.ModelRuntimeException;
import com.srsw.icfp2018.model.State;
import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.TraceFileException;
import com.srsw.icfp2018.model.Vector3;

public class Fission extends Trace {

	public final Vector3 nd;
	public final int m;

	public Fission(int i, InputStream in) throws TraceFileException {
		nd = decodeND(i);
		try {
			m = in.read();
			if (m == -1) {
				throw new TraceFileException("EOF in 2nd byte of Fission");
			}
		} catch (IOException e) {
			throw new TraceFileException("Error reading 2nd byte of Fission", e);
		}
	}
	
	public Fission(Vector3 nd, int m) {
		assertND(nd);
		if ((m < 0) || (m > 31)) {
			throw new RuntimeException("Bad value for m: " + m);
		}
		this.nd = nd;
		this.m = m;
	}

	@Override
	public String toString() {
		return "Fission " + nd + " " + m;
	}
	
	@Override
	public void execute(Bot bot) throws ModelRuntimeException {
		State state = bot.state;
		Bot newbot = new Bot(bot, nd, m);
		state.bots.add(newbot);
		
		// note constructor doesn't modify parent bot
		bot.seeds = bot.seeds.subList(m + 1, bot.seeds.size());
		
		for (Bot i : state.bots) {
			System.out.println("  " + i);
		}
		
		state.energy += 24;
	}
	
	@Override
	public void write(OutputStream out) throws IOException {
		int i = ((nd.x + 1) * 9) + ((nd.y + 1) * 3) + (nd.z + 1);
		out.write((i << 3) | 0x05);
		out.write(m);
	}
}
