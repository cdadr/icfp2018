package com.srsw.icfp2018.model;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import com.srsw.icfp2018.model.traceops.Fill;
import com.srsw.icfp2018.model.traceops.Fission;
import com.srsw.icfp2018.model.traceops.Flip;
import com.srsw.icfp2018.model.traceops.FusionP;
import com.srsw.icfp2018.model.traceops.FusionS;
import com.srsw.icfp2018.model.traceops.GFill;
import com.srsw.icfp2018.model.traceops.GVoid;
import com.srsw.icfp2018.model.traceops.Halt;
import com.srsw.icfp2018.model.traceops.LMove;
import com.srsw.icfp2018.model.traceops.SMove;
import com.srsw.icfp2018.model.traceops.Wait;
import com.srsw.icfp2018.model.traceops.Void;

public abstract class Trace {
	
	// singletons (no args)
	public static final Halt halt = new Halt();
	public static final Wait wait = new Wait();
	public static final Flip flip = new Flip();
	
	// frequently used
	public static final Fill fillDown = new Fill(Vector3.down);
	public static final Fill fillPosX = new Fill(Vector3.right);
	public static final Fill fillNegX = new Fill(Vector3.left);
	public static final Void voidDown = new Void(Vector3.down);
	public static final Void voidPosX = new Void(Vector3.right);
	public static final Void voidNegX = new Void(Vector3.left);


	public static void appendFromStream(List<Trace> list, int opcode, InputStream in) throws TraceFileException {
		int low3 = opcode & 0x07;
		int high5 = (opcode & 0xf8) >> 3;
		switch (low3) {
		case 0x07:
			if (high5 == 0x1f) {
				// halt is singleton
				list.add(halt);
			} else {
				// fusionp
				list.add(new FusionP(high5));
			}
			break;
			
		case 0x06:
			if (high5 == 0x1f) {
				//wait
				list.add(wait);
			} else {
				// fusions
				list.add(new FusionS(high5));
			}
			break;
			
		case 0x05:
			if (high5 == 0x1f) {
				// flip
				list.add(flip);
			} else {
				// fission
				list.add(new Fission(high5, in));
			}
			break;
		
		case 0x04:
			if ((opcode & 0x08) == 0) {
				// smove
				list.add(new SMove(opcode, in));
			} else {
				// lmove
				list.add(new LMove(opcode, in));
			}
			break;
			
		case 0x03:
			// fill
			list.add(new Fill(high5));
			break;
			
		case 0x02:
			// void
			list.add(new Void(high5));
			break;
			
			
		case 0x01:
			// gfill
			list.add(new GFill(high5, in));
			break;
			
			
		case 0x00:
			// gvoid
			list.add(new GVoid(high5, in));
			break;
			
			
		default:
			throw new TraceFileException(String.format("Bad opcode 0x%02x", opcode));
		}
	}
	

	protected Vector3 decodeND(int i) {
		int z = (i % 3) - 1;
		int y = ((i / 3) % 3) - 1;
		int x = (i / 9) - 1;
		return new Vector3(x, y, z);
	}
	
	
	protected Vector3 decodeLLD(int a, int i) throws TraceFileException {
		switch (a) {
		case 1:
			return new Vector3(i - 15, 0, 0);
		case 2:
			return new Vector3(0, i - 15, 0);
		case 3:
			return new Vector3(0, 0, i - 15);
		default:
			throw new TraceFileException(String.format("Bad value for a: 0x%02x", a));
		}
	}

	protected void assertND(Vector3 nd) {
		if ((nd.x != 0) && (nd.y != 0) && (nd.z != 0)) {
			throw new RuntimeException("At least one axis must be zero: " + this);
		}
		if ((Math.abs(nd.x) > 1) || (Math.abs(nd.y) > 1) || (Math.abs(nd.z) > 1)) {
			throw new RuntimeException("nd out of range: " + this);
		}
	}

	protected void assertFD(Vector3 nd) {
		if ((nd.x == 0) && (nd.y == 0) && (nd.z == 0)) {
			throw new RuntimeException("At least one axis must be non-zero: " + this);
		}
		if ((Math.abs(nd.x) > 30) || (Math.abs(nd.y) > 30) || (Math.abs(nd.z) > 30)) {
			throw new RuntimeException("fd out of range: " + this);
		}
	}

	
	protected Vector3 decodeSLD(int a, int i) {
		throw new RuntimeException("not implemented: " + this);
	}


//	public abstract void execute(Bot bot);
	public void execute(Bot bot) throws ModelRuntimeException {
		throw new RuntimeException("not implemented: " + this);
	}

//	public abstract void write(OutputStream out) throws IOException;
	public void write(OutputStream out) throws IOException {
		throw new RuntimeException("not implemented: " + this);
	}


	public static void writeTrace(OutputStream out, List<Trace> trace) throws IOException {
		for (Trace op : trace) {
			op.write(out);
		}
	}


	public static void printTrace(PrintStream out, List<Trace> trace) {
		out.println("#Commands: " + trace.size());
		out.println("Commands:");
		for (Trace op : trace) {
			out.println("    " + op);
		}
	}
}
