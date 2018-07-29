package com.srsw.icfp2018.assembler;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

import com.srsw.icfp2018.assembler.Token.TokenNumber;
import com.srsw.icfp2018.assembler.Token.TokenString;
import com.srsw.icfp2018.assembler.Token.TokenVector;
import com.srsw.icfp2018.model.Vector3;

public class TokenStream {

	private PushbackReader in;
	
	public TokenStream(Reader in) {
		this.in = new PushbackReader(in);
	}

	public Token nextToken() throws IOException, ParseException {
		while (true) {
			int c = in.read();
			if (c == -1) {
				return null;
			}
			if (Character.isWhitespace(c)) {
				continue;
			}
			if (c == '<') {
				return parseVector3();
			}
			if ((c == '-') || (c == '+') || ((c >= '0') && (c <= '9'))) {
				int i = parseNumber(c);
				return new TokenNumber(i);
			}
			if (c == '#') {
				parseComment();
				continue;
			}
			in.unread(c);
			return parseString();
		}
	}

	private void parseComment() throws IOException {
		while (true) {
			int c = in.read();
			if (c == -1) {
				return;
			}
			if (c == '\n') {
				return;
			}
		}
	}

	private int parseNumber() throws IOException, ParseException {
		int c = in.read();
		if ((c == '-') || (c == '+') || ((c >= '0') && (c <= '9'))) {
			return parseNumber(c);
		} else {
			throw new ParseException("bad number char: " + c);
		}
	}
	
	private int parseNumber(int c) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append((char) c);
		while (true) {
			c = in.read();
			if ((c < '0') || (c > '9')) {
				in.unread(c);
				break;
			}
			sb.append((char) c);
		}
		return Integer.parseInt(sb.toString());
	}

	private Token parseString() throws IOException {
		StringBuilder sb = new StringBuilder();
		while (true) {
			int c = in.read();
			if ((c == -1) || Character.isWhitespace(c)) {
				break;
			}
			if (c == '<') {
				in.unread(c);
				break;
			}
			sb.append((char) c);
		}
		
		return new TokenString(sb.toString());
	}

	private Token parseVector3() throws IOException, ParseException {
		int x = parseNumber();
		expect(',');
		int y = parseNumber();
		expect(',');
		int z = parseNumber();
		expect('>');

		return new TokenVector(new Vector3(x, y, z));
	}

	private void expect(int expected) throws IOException {
		int c = in.read();
		if (c != expected) {
			throw new IOException("expected '" + (char)expected + "; got '" + (char)c + "'");
		}
	}
}
