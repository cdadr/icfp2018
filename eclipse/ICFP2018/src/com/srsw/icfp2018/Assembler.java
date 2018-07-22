package com.srsw.icfp2018;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.srsw.icfp2018.assembler.Token;
import com.srsw.icfp2018.assembler.Token.TokenNumber;
import com.srsw.icfp2018.assembler.Token.TokenString;
import com.srsw.icfp2018.assembler.Token.TokenVector;
import com.srsw.icfp2018.assembler.TokenStream;
import com.srsw.icfp2018.model.Trace;
import com.srsw.icfp2018.model.traceops.Fill;
import com.srsw.icfp2018.model.traceops.Fission;
import com.srsw.icfp2018.model.traceops.FusionP;
import com.srsw.icfp2018.model.traceops.FusionS;
import com.srsw.icfp2018.model.traceops.SMove;

public class Assembler {
	
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("missing args");
			System.exit(1);
		}
		
		String sourceFilename = args[0];
		String traceFilename = args[1];
		
		List<Trace> trace;
		if (sourceFilename.equals("--")) {
			trace = readTrace(new InputStreamReader(System.in));
		} else {
			FileReader in = new FileReader(sourceFilename);
			try {
				trace = readTrace(in);
			} finally {
				in.close();
			}
		}
		
		FileOutputStream traceFile = new FileOutputStream(traceFilename);
		try {
			Trace.writeTrace(traceFile, trace);
		} finally {
			traceFile.close();	
		}
	}
	

	private static List<Trace> readTrace(Reader in) throws IOException {
		List<Trace> trace = new ArrayList<>();
		
		TokenStream tokenStream = new TokenStream(in);
		while (true) {
			Token token = tokenStream.nextToken();
			if (token == null) {
				break;
			}
			System.out.println(token);
			
			String opcode = ((TokenString) token).value.toLowerCase();
			if (opcode.equals("flip")) {
				trace.add(Trace.flip);
				
			} else if (opcode.equals("halt")) {
				trace.add(Trace.halt);
				
			} else if (opcode.equals("wait")) {
				trace.add(Trace.wait);
				
			} else if (opcode.equals("smove")) {
				TokenVector lld = (TokenVector) tokenStream.nextToken();
				trace.add(new SMove(lld.value));
				
			} else if (opcode.equals("fill")) {
				TokenVector nd = (TokenVector) tokenStream.nextToken();
				trace.add(new Fill(nd.value));
				
			} else if (opcode.equals("fission")) {
				TokenVector nd = (TokenVector) tokenStream.nextToken();
				TokenNumber m = (TokenNumber) tokenStream.nextToken();
				trace.add(new Fission(nd.value, m.value));
				
			} else if (opcode.equals("fusionp")) {
				TokenVector nd = (TokenVector) tokenStream.nextToken();
				trace.add(new FusionP(nd.value));
				
			} else if (opcode.equals("fusions")) {
				TokenVector nd = (TokenVector) tokenStream.nextToken();
				trace.add(new FusionS(nd.value));
				
			} else {
				throw new IOException("unknown opcode \"" + opcode + "\"");
			}
		}

		System.out.println("--------");
		Trace.printTrace(System.out, trace);
		return trace;
	}
}
