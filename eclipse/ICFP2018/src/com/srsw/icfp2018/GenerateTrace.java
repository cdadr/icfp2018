package com.srsw.icfp2018;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.srsw.icfp2018.generator.TraceGenerator;
import com.srsw.icfp2018.generator.TraceGenerator.Mode;
import com.srsw.icfp2018.model.Model;
import com.srsw.icfp2018.model.Trace;

public class GenerateTrace {

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("usage: cmd -ma|md|mr MODEL_FILE [rtgtMODEL_FILE] [OUTPUT_TRACE_FILE]");
			System.exit(1);
		}
		
		String srcModelFilename = null;
		String tgtModelFilename = null;
		String traceFilename = null;
		Mode mode;
		
		int argc = 0;
		String flag = args[argc++];
		if (flag.equals("-ma")) {
			tgtModelFilename = args[argc++];
			mode = Mode.Add;
			
		} else if (flag.equals("-md")) {
			srcModelFilename = args[argc++];
			mode = Mode.Delete;
			
		} else if (flag.equals("-mr")) {
			srcModelFilename = args[argc++];
			tgtModelFilename = args[argc++];
			mode = Mode.Replace;
			
		} else {
			throw new Exception("unknown flag " + flag);
		}
		traceFilename = (args.length > argc) ? args[argc++] : null;

		generateOne(srcModelFilename, tgtModelFilename, traceFilename, mode);
	}


	public static void  generateOne(String srcModelFilename, String tgtModelFilename, String traceFilename, Mode mode)
			throws IOException 
	{
		Model srcModel = loadModel(srcModelFilename);
		Model tgtModel = loadModel(tgtModelFilename);

		TraceGenerator generator = new TraceGenerator(srcModel, tgtModel, mode);
		generator.run();
		
		System.out.println("--- generation complete; len=" + generator.trace.size() + " ---");
		generator.printTrace(System.out);
		
		if (traceFilename != null) {
			FileOutputStream traceFile = new FileOutputStream(traceFilename);
			System.out.println("Writing " + generator.trace.size() + " traces to " + traceFilename);
			try {
				Trace.writeTrace(traceFile, generator.trace);
			} finally {
				traceFile.close();
			}
		}
	}
	
	public static Model loadModel(String filename) throws IOException {
		if (filename == null) {
			return null;
		}
		FileInputStream modelFile = new FileInputStream(filename);
		Model model = new Model();
		try {
			model.load(modelFile);
		} finally {
			modelFile.close();
		}
		return model;
	}
}
