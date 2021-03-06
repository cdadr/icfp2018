package com.srsw.icfp2018.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class State {
	public final int r;
	
	public long energy = 0;
	public Harmonics harmonics = Harmonics.Low;
	public Matrix<Voxel> matrix = new Matrix<>();
	public Set<Bot> bots = new TreeSet<Bot>();
//	public List<Bot> bots = new ArrayList<>();
	public List<Trace> trace = null;
	
	public State(int r) {
		this.r = r;
		matrix.dimensions = new Vector3(r, r, r);
		
		Bot initBot = new Bot(this);
		bots.add(initBot);
	}

	public boolean isWellFormed() {
		// TODO
		/*
		 * if harmonics = Low, then all Full voxels are grounded
		 * eacn bot has unique id
		 * each bot has distinct pos, and is in a Void voxel
		 * seeds of each active bot are disjoint
		 * seeds of each active bot does not include any active bot's seed
		 */
		return true;
	}
	
	public void readTrace(InputStream in) throws IOException, TraceFileException {
		trace = new ArrayList<>();
		while (true) {
			int i = in.read();
			if (i == -1) {
				break;
			}
			Trace.appendFromStream(trace, i, in);
		}
	}

	public void printTrace(PrintStream out) {
		Trace.printTrace(out, trace);
	}

	public void run() throws ModelRuntimeException {
		while (!trace.isEmpty()) {
			if (trace.size() < bots.size()) {
				throw new ModelRuntimeException("Not enough traces for " + bots.size() + " bots");
			}
			if (bots.isEmpty()) {
				throw new ModelRuntimeException("no active bots");
			}
			for (Bot bot : bots) {
				Trace op = trace.remove(0);		// XXX we aren't supposed to remove the op until after execution
				bot.nextOp = op;
			}
			
			// TODO ensure pre-conditions, etc.
			
			// global energy update
			if (harmonics == Harmonics.High) {
				energy += 30 * r * r * r;
			} else {
				energy += 3 * r * r * r;
			}
			energy += 20 * bots.size();

			// create a copy so iterator doesn't blow up on fission/fusion
			List<Bot> tmpList = new ArrayList<>(bots);	
			for (Bot bot : tmpList) {
				// In theory, these could execute in parallel. In theory.
				bot.execute();
			}

			printStateQuick(System.out);
		}
		System.out.println("---- END RUN ----");
	}

	public void printState(PrintStream out) {
		out.println("energy: " + energy);
		out.println("harmonics: " + harmonics);
		out.println("matrix:");
		matrix.print(out);
		out.println("#bots: " + bots.size());
		for (Bot bot : bots) {
			out.println("  " + bot);
		}
		printTrace(out);
	}


	public void printStateQuick(PrintStream out) {
		out.print("energy=" + energy + ", harmonics=" + harmonics + "; #commands=" + trace.size() + ", #bots=" + bots.size() + ": [");
		for (Bot bot : bots) {
			out.print(" " + bot.bid + ":" + bot.pos);
		}
		out.println(" ]");
	}
	
	
	public void validateModel(Model model) throws ModelRuntimeException {
		boolean error = false;
		for (int x = 0; x < r; x++) {
			for (int y = 0; y < r; y++) {
				for (int z = 0; z < r; z++) {
					boolean isFull = matrix.get(x, y, z) != null;
					boolean modelIsFull = model.get(x, y, z);
					if (isFull != modelIsFull) {
						System.err.println("Model/State mismatch <" + x + "," + y + "," + z + ">: state=" + 
								isFull + "; model=" + modelIsFull);
						error = true;
					}
				}
			}
		}
		if (error) {
			throw new ModelRuntimeException("state did not match model");
		}
	}

	public void validateClear() throws ModelRuntimeException {
		boolean error = false;
		for (int x = 0; x < r; x++) {
			for (int y = 0; y < r; y++) {
				for (int z = 0; z < r; z++) {
					boolean isFull = matrix.get(x, y, z) != null;
					if (isFull) {
						System.err.println("Full voxel <" + x + "," + y + "," + z + ">");
						error = true;
					}
				}
			}
		}
		if (error) {
			throw new ModelRuntimeException("state did not match model");
		}
	}

	public void initModel(Model model) {
		for (int x = 0; x < r; x++) {
			for (int y = 0; y < r; y++) {
				for (int z = 0; z < r; z++) {
					if (model.get(x, y, z)) {
						Voxel voxel = new Voxel(x,  y, z);
						matrix.put(x, y, z, voxel);
					}
				}
			}
		}
	}
}
