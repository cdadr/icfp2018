package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.InputStream;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.TraceFileException;
import com.srsw.icfp2018.model.Vector3;

public class SMove extends Trace {
	
	public Vector3 lld;

	public SMove(int opcode, InputStream in) throws TraceFileException {
		if ((opcode & 0xc8) != 0) {
			throw new TraceFileException(String.format("Bad opcode for SMove: 0x%02x", opcode));
		}
		int op2;
		try {
			op2 = in.read();
			if (op2 == -1) {
				throw new TraceFileException("EOF in 2nd byte of SMove");
			}
		} catch (IOException e) {
			throw new TraceFileException("Error reading 2nd byte of SMove", e);
		}
		
		if ((op2 & 0xe0) != 0) {
			throw new TraceFileException(String.format("Bad operand for SMove: 0x%02x", op2));
		}
		
		lld = decodeLLD(opcode >> 4, op2);
	}
	
	@Override
	public String toString() {
		return "SMove " + lld;
	}
	
	@Override
	public void execute(Bot bot) {
		bot.pos.move(lld);
		bot.state.energy += 2 * lld.mlen();
	}
}
