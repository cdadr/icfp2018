package com.srsw.icfp2018;

import java.io.FileInputStream;
import java.io.IOException;

import com.srsw.icfp2018.model.Model;
import com.srsw.icfp2018.model.State;

public class RunTrace {

	public static void main(String[] args) throws Exception {
		if (args.length < 3) {
			System.err.println("usage: app TRACEFILE -r RES");
			System.err.println("       app TRACEFILE -m[a|d|r] MODELFILE [MODELFILE_R_TGT]");
			System.exit(1);
		}
		String traceFilename = args[0];
		String flag = args[1];
		
		final int res;
		Model srcModel = null;
		Model tgtModel = null;
		
		if (flag.equals("-r")) {
			res = Integer.parseInt(args[2]);
			
		} else if (flag.startsWith("-m")) {
			String runType = flag.substring(2);
			if (runType.equals("a")) {
				String tgtModelFilename = args[2];
				tgtModel = loadModel(tgtModelFilename);
				res = tgtModel.r;
			} else if (runType.equals("d")) {
				String srcModelFilename = args[2];
				srcModel = loadModel(srcModelFilename);
				res = srcModel.r;
			} else if (runType.equals("r")) {
				String srcModelFilename = args[2];
				String tgtModelFilename = args[3];
				srcModel = loadModel(srcModelFilename);
				tgtModel = loadModel(tgtModelFilename);
				res = tgtModel.r;
				if (res != srcModel.r) {
					throw new Exception("source/target resolution mismatch: " + srcModel.r + " != " + tgtModel.r);
				}
			} else {
				throw new Exception("unknown model flag " + flag);
			}

		} else {
			throw new Exception("unknown flag " + flag);
		}
		
		State state = new State(res);
		FileInputStream traceFile = new FileInputStream(traceFilename);
		try {
			state.readTrace(traceFile);
		} finally {
			traceFile.close();
		}
		
		if (srcModel != null) {
			state.initModel(srcModel);
		}
		state.run();
		state.printState(System.out);
		if (tgtModel != null) {
			state.validateModel(tgtModel);
		} else if (!flag.equals("-r")) {
			state.validateClear();
		}
		System.out.format("Model validated OK; final energy: %,d\n", state.energy);
	}

	private static Model loadModel(String modelFilename) throws IOException {
		FileInputStream modelFile = new FileInputStream(modelFilename);
		Model model = new Model();
		try {
			model.load(modelFile);
		} finally {
			modelFile.close();
		}
		return model;
	}
}
