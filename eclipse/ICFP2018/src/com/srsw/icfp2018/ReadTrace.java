package com.srsw.icfp2018;

import java.io.FileInputStream;

import com.srsw.icfp2018.model.State;

public class ReadTrace {
	
	public static void main(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("no args provided");
			System.exit(1);
		}
		String filename = args[0];
		
		State state = new State(250);
		FileInputStream in = new FileInputStream(filename);
		try {
			state.readTrace(in);
		} finally {
			in.close();
		}
		state.printTrace(System.out);
	}

}
