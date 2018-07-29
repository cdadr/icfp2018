package com.srsw.icfp2018;


import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.srsw.icfp2018.assembler.ParseException;
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
import com.srsw.icfp2018.model.traceops.GFill;
import com.srsw.icfp2018.model.traceops.GVoid;
import com.srsw.icfp2018.model.traceops.SMove;
import com.srsw.icfp2018.model.traceops.Void;

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
	

	private static List<Trace> readTrace(Reader in) throws ParseException, IOException {
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
				try {
					TokenVector lld = (TokenVector) tokenStream.nextToken();
					trace.add(new SMove(lld.value));
				} catch (ClassCastException e) {
					throw new ParseException("SMove: lld");
				}
				
			} else if (opcode.equals("fill")) {
				try {
					TokenVector nd = (TokenVector) tokenStream.nextToken();
					trace.add(new Fill(nd.value));
				} catch (ClassCastException e) {
					throw new ParseException("Fill: nd");
				}
				
			} else if (opcode.equals("void")) {
				try {
					TokenVector nd = (TokenVector) tokenStream.nextToken();
					trace.add(new Void(nd.value));
				} catch (ClassCastException e) {
					throw new ParseException("Void: nd");
				}
				
			} else if (opcode.equals("fission")) {
				try {
					TokenVector nd = (TokenVector) tokenStream.nextToken();
					TokenNumber m = (TokenNumber) tokenStream.nextToken();
					trace.add(new Fission(nd.value, m.value));
				} catch (ClassCastException e) {
					throw new ParseException("Fission: nd  m");
				}
				
			} else if (opcode.equals("fusionp")) {
				try {
					TokenVector nd = (TokenVector) tokenStream.nextToken();
					trace.add(new FusionP(nd.value));
				} catch (ClassCastException e) {
					throw new ParseException("FusionP: nd");
				}
				
			} else if (opcode.equals("fusions")) {
				try {
					TokenVector nd = (TokenVector) tokenStream.nextToken();
					trace.add(new FusionS(nd.value));
				} catch (ClassCastException e) {
					throw new ParseException("FusionS: nd");
				}
				
			} else if (opcode.equals("gfill")) {
				try {
					TokenVector nd = (TokenVector) tokenStream.nextToken();
					TokenVector fd = (TokenVector) tokenStream.nextToken();
					trace.add(new GFill(nd.value, fd.value));
				} catch (ClassCastException e) {
					throw new ParseException("GFill: nd  fd");
				}
				
			} else if (opcode.equals("gvoid")) {
				try {
					TokenVector nd = (TokenVector) tokenStream.nextToken();
					TokenVector fd = (TokenVector) tokenStream.nextToken();
					trace.add(new GVoid(nd.value, fd.value));
				} catch (ClassCastException e) {
					throw new ParseException("GVoid: nd  fd");
				}
				
			} else {
				throw new IOException("unknown opcode \"" + opcode + "\"");
			}
		}

		System.out.println("--------");
		Trace.printTrace(System.out, trace);
		return trace;
	}
}
