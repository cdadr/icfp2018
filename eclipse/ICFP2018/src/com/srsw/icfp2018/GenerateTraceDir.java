package com.srsw.icfp2018;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;

import com.srsw.icfp2018.generator.TraceGenerator.Mode;

public class GenerateTraceDir {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("usage: cmd  MODEL_DIR  OUTPUT_TRACE_DIR");
			System.exit(1);
		}
		
		File modelDir = new File(args[0]);
		File traceDir = new File(args[1]);

		if (traceDir.exists()) {
			throw new IOException(traceDir + " already exists");
		}
		if (!modelDir.exists() || !modelDir.isDirectory()) {
			throw new IOException(modelDir + " doesn't exist or isn't dir");
		}
		if (!traceDir.mkdirs()) {
			throw new IOException("could not create " + traceDir);
		}
		
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".mdl");
			}
		};
		File[] modelFiles = modelDir.listFiles(filter);
		Arrays.sort(modelFiles);
		System.out.println("#files=" + modelFiles.length + ":");
		for (File modelFile : modelFiles) {
			String traceFilename = modelFile.getName().replaceFirst("_tgt\\.mdl$", ".nbt");
			File traceFile = new File(traceDir, traceFilename);
			System.out.println("  " + modelFile + " -> " + traceFile);
			
			GenerateTrace.generateOne(null, modelFile.getPath(), traceFile.getPath(), Mode.Add);
			break;
		}
	}
}
