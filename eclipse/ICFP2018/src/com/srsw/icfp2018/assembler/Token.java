package com.srsw.icfp2018.assembler;

import com.srsw.icfp2018.model.Vector3;

public class Token {


	public enum TokenType { TOK_STRING, TOK_NUMBER, TOK_VECTOR3 };
	
	public final TokenType tokenType;

	public Token(TokenType tokenType) {
		this.tokenType = tokenType;
	}
	

	public static class TokenNumber extends Token {
		public final int value;

		public TokenNumber(int value) {
			super(TokenType.TOK_NUMBER);
			this.value = value;
		}
		@Override
		public String toString() {
			return "{Number:" + value + "}";
		}
	}
	

	public static class TokenString extends Token {
		public final String value;

		public TokenString(String value) {
			super(TokenType.TOK_STRING);
			this.value = value;
		}
		@Override
		public String toString() {
			return "{String:\"" + value + "\"}";
		}
	}
	

	public static class TokenVector extends Token {
		public final Vector3 value;

		public TokenVector(Vector3 value) {
			super(TokenType.TOK_VECTOR3);
			this.value = value;
		}
		@Override
		public String toString() {
			return "{Vector:" + value + "}";
		}
	}
}
