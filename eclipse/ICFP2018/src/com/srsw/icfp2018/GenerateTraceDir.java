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
		
		doAddTasks(modelDir, traceDir);
		doDeleteTasks(modelDir, traceDir);
		doReplaceTasks(modelDir, traceDir);
		
	}

	private static void doAddTasks(File modelDir, File traceDir) throws IOException {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("FA") && name.endsWith("_tgt.mdl");
			}
		};
		File[] tgtModelFiles = modelDir.listFiles(filter);
		Arrays.sort(tgtModelFiles);
		System.out.println("#files=" + tgtModelFiles.length + ":");
		for (File tgtModelFile : tgtModelFiles) {
			String traceFilename = tgtModelFile.getName().replaceFirst("_tgt\\.mdl$", ".nbt");
			File traceFile = new File(traceDir, traceFilename);
			System.out.println("  " + tgtModelFile + " -> " + traceFile);
			
			GenerateTrace.generateOne(null, tgtModelFile.getPath(), traceFile.getPath(), Mode.Add);
		}
	}

	private static void doDeleteTasks(File modelDir, File traceDir) throws IOException {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("FD") && name.endsWith("_src.mdl");
			}
		};
		File[] srcModelFiles = modelDir.listFiles(filter);
		Arrays.sort(srcModelFiles);
		System.out.println("#files=" + srcModelFiles.length + ":");
		for (File srcModelFile : srcModelFiles) {
			String traceFilename = srcModelFile.getName().replaceFirst("_src\\.mdl$", ".nbt");
			File traceFile = new File(traceDir, traceFilename);
			System.out.println("  " + srcModelFile + " -> " + traceFile);
			
			GenerateTrace.generateOne(srcModelFile.getPath(), null, traceFile.getPath(), Mode.Delete);
		}
	}

	private static void doReplaceTasks(File modelDir, File traceDir) throws IOException {
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith("FR") && name.endsWith("_src.mdl");
			}
		};
		File[] srcModelFiles = modelDir.listFiles(filter);
		Arrays.sort(srcModelFiles);
		System.out.println("#files=" + srcModelFiles.length + ":");
		for (File srcModelFile : srcModelFiles) {
			String tgtModelFilename = srcModelFile.getName().replaceFirst("_src", "_tgt");
			File tgtModelFile = new File(modelDir, tgtModelFilename);
			
			String traceFilename = srcModelFile.getName().replaceFirst("_src\\.mdl$", ".nbt");
			File traceFile = new File(traceDir, traceFilename);
			System.out.println("  " + srcModelFile + " + " + tgtModelFile + " -> " + traceFile);
			
			GenerateTrace.generateOne(srcModelFile.getPath(), tgtModelFile.getPath(), traceFile.getPath(), Mode.Replace);
		}
	}
}
