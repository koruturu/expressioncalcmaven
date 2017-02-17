package org.synopsys.codeexample.expcalc;

public class InvalidExpressionException extends Exception {
	public InvalidExpressionException(String exp, Exception e) {
		super("Inavlid Expresion :" + exp);
	}

	public InvalidExpressionException(String msg) {
		super("Inavlid Expresion :" + msg);
		
	}
}