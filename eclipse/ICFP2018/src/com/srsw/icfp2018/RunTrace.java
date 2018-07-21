package com.srsw.icfp2018;

import java.io.FileInputStream;

import com.srsw.icfp2018.model.Model;
import com.srsw.icfp2018.model.State;

public class RunTrace {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("need 2 args");
			System.exit(1);
		}
		String traceFilename = args[0];
		String modelFilename = args[1];
		
		FileInputStream modelFile = new FileInputStream(modelFilename);
		Model model = new Model();
		try {
			model.load(modelFile);
		} finally {
			modelFile.close();
		}
		
		State state = new State(model.r);
		FileInputStream traceFile = new FileInputStream(traceFilename);
		try {
			state.readTrace(traceFile);
		} finally {
			traceFile.close();
		}
		state.run();
		state.printState(System.out);
		
		state.validateModel(model);
		System.out.format("Model validated OK; final energy: %,d\n", state.energy);
	}
}
