package com.srsw.icfp2018.model.traceops;

import java.io.IOException;
import java.io.InputStream;

import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.TraceFileException;
import com.srsw.icfp2018.model.Vector3;

public class LMove extends Trace {

	public final Vector3 sld1;
	public final Vector3 sld2;
	
	public LMove(int opcode, InputStream in) throws TraceFileException {
		int op2;
		try {
			op2 = in.read();
			if (op2 == -1) {
				throw new TraceFileException("EOF in 2nd byte of LMove");
			}
		} catch (IOException e) {
			throw new TraceFileException("Error reading 2nd byte of LMove", e);
		}

		int sld2a = opcode >> 6;
		int sld1a = (opcode & 0x30) >> 4;
		int sld2i = op2 >> 4;
		int sld1i = op2 & 0x0f;
		
		sld1 = decodeSLD(sld1a, sld1i);
		sld2 = decodeSLD(sld2a, sld2i);
	}
	
	@Override
	public String toString() {
		return "LMove " + sld1 + " " + sld2;
	}
}
