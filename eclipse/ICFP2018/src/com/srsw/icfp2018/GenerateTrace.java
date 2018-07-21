package com.srsw.icfp2018;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.srsw.icfp2018.generator.TraceGenerator;
import com.srsw.icfp2018.model.Model;
import com.srsw.icfp2018.model.Trace;

public class GenerateTrace {

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("usage: cmd  MODEL_FILE  [OUTPUT_TRACE_FILE]");
			System.exit(1);
		}
		
		String modelFilename = args[0];
		String traceFilename = (args.length > 1) ? args[1] : null;

		generateOne(modelFilename, traceFilename);
	}


	public static void  generateOne(String modelFilename, String traceFilename) throws IOException {
		FileInputStream modelFile = new FileInputStream(modelFilename);
		Model model = new Model();
		try {
			model.load(modelFile);
		} finally {
			modelFile.close();
		}

		TraceGenerator generator = new TraceGenerator(model);
		generator.run();
		
		System.out.println("--- generation complete; len=" + generator.trace.size() + " ---");
		generator.printTrace(System.out);
		
		if (traceFilename != null) {
			FileOutputStream traceFile = new FileOutputStream(traceFilename);
			try {
				Trace.writeTrace(traceFile, generator.trace);
			} finally {
				traceFile.close();
			}
		}
	}
}
