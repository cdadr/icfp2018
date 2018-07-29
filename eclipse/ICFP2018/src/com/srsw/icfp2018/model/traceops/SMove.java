package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.srsw.icfp2018.model.Bot;
import com.srsw.icfp2018.model.ModelRuntimeException;
import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.TraceFileException;
import com.srsw.icfp2018.model.Vector3;

public class SMove extends Trace {
	
	public final Vector3 lld;

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
	
	public SMove(Vector3 v) {
		lld = v;
	}

	@Override
	public String toString() {
		return "SMove " + lld;
	}
	
	@Override
	public void execute(Bot bot) throws ModelRuntimeException {
		bot.pos.move(lld);
		bot.pos.validate(bot.state.r);
		bot.state.energy += 2 * lld.mlen();
	}
	
	@Override
	public void write(OutputStream out) throws IOException {
		int a, i0;
		if ((lld.x == 0) && (lld.y == 0) && (lld.z == 0)) {
			throw new IOException("SMove zero vector? " + this);
		}
		if (lld.x != 0) {
			if ((lld.y != 0) || (lld.z != 0)) {
				throw new IOException("SMove non-linear vector " + this);
			}
			a = 1;
			i0 = lld.x;
		} else if (lld.y != 0) {
			if (lld.z != 0) {
				throw new IOException("SMove non-linear vector " + this);
			}
			a = 2;
			i0 = lld.y;
		} else {
			a = 3;
			i0 = lld.z;
		}
		
		if ((i0 > 15) || (i0 < -15)) {
			throw new IOException("SMove out of range " + this);
		}
		int i = i0 + 15;
		
		int b0 = (a << 4) | 0x04;
		int b1 = i;
		
		out.write(b0);
		out.write(b1);
	}
}
