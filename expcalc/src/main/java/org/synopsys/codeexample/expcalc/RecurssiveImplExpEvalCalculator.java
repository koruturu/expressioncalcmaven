package org.synopsys.codeexample.expcalc;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class RecurssiveImplExpEvalCalculator {
	private static final Logger logger = Logger.getLogger(RecurssiveImplExpEvalCalculator.class);
	private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
	private Hashtable<Object, Double> context_dict = new Hashtable();

	private double evaluate(Object expList) throws InvalidExpressionException {
		if (expList instanceof String) {
			String exp = expList.toString();
			try {
				if (this.pattern.matcher(expList.toString()).matches()) {
					return Double.parseDouble(exp);
				}
				return this.context_dict.get(expList.toString());
			} catch (Exception e) {
				throw new InvalidExpressionException(
						"Please give valid expression, you might have had given maximum or minimum double values allowed or had passed a wrong expression.",
						e);
			}
		}
		try {
			ArrayList exp_list = (ArrayList) expList;
			String operator = exp_list.get(0).toString();
			Object exp = exp_list.get(1);
			if (operator.equalsIgnoreCase("root")) {
				return this.evaluate(exp);
			}
			if (operator.equalsIgnoreCase("add")) {
				return this.evaluate(exp) + this.evaluate(exp_list.get(2));
			}
			if (operator.equalsIgnoreCase("mult")) {
				return this.evaluate(exp) * this.evaluate(exp_list.get(2));
			}
			if (operator.equalsIgnoreCase("div")) {
				return this.evaluate(exp) / this.evaluate(exp_list.get(2));
			}
			if (operator.equalsIgnoreCase("let")) {
				this.context_dict.put(exp, this.evaluate(exp_list.get(2)));
				return this.evaluate(exp_list.get(3));
			}
			throw new InvalidExpressionException(
					"You had given a invalid operator, Only add,mult,div,let are allowed!" + operator);
		} catch (InvalidExpressionException e) {
			throw e;
		} catch (Exception e) {
			throw new InvalidExpressionException(
					"Please enter a valid expression, check for braces whether it is closed properly or not.", e);
		}
	}

	private Object[] parse_exp_str(String exp_str, String func_name, int index) throws Exception {
		ArrayList<Object> exp_list;
		exp_list = new ArrayList<Object>();
		exp_list.add(func_name);
		String tmp_str = "";
		try {
			do {
				char currentChar;
				if ((currentChar = exp_str.charAt(index)) == '(') {
					Object[] returned = this.parse_exp_str(exp_str, tmp_str, index + 1);
					index = (Integer) returned[1];
					exp_list.add(returned[0]);
					tmp_str = "";
					if (func_name.equalsIgnoreCase("root")) {
						break;
					}
				} else {
					if (currentChar == ')') {
						if (tmp_str.length() > 0) {
							exp_list.add(tmp_str);
						}
						break;
					}
					if (currentChar == ',') {
						if (tmp_str.length() > 0) {
							exp_list.add(tmp_str);
						}
						tmp_str = "";
					} else {
						tmp_str = tmp_str + exp_str.charAt(index);
					}
				}
				++index;
			} while (true);
		} catch (InvalidExpressionException e) {
			throw e;
		} catch (Exception e) {
			throw new InvalidExpressionException(
					"Please enter a valid expression, check for braces whether it is closed properly or not.", e);
		}
		return new Object[] { exp_list, index };
	}

	public double evaluateExpression(String expression) throws Exception {
		if (StringUtils.isEmpty((CharSequence) expression)) {
			throw new InvalidExpressionException("Empty expression provided, please provide valid expression.");
		}
		return this.evaluate(this.parse_exp_str(expression.replaceAll(" ", ""), "root", 0)[0]);
	}

	public static void main(String[] args) {
		String expresssion;
		if (args.length <= 0) {
			System.out.println(
					"Please enter the calculator expression here or you can add as arguments while running the program.");
			Scanner sc = new Scanner(System.in);
			expresssion = sc.nextLine();
		} else {
			expresssion = args[0];
		}
		if (expresssion != null) {
			RecurssiveImplExpEvalCalculator impl = new RecurssiveImplExpEvalCalculator();
			try {
				long startTime = System.nanoTime();
				double result = impl.evaluateExpression(expresssion);
				long endTime = System.nanoTime();
				logger.info((Object) ("Total Time took for the Expression( " + expresssion + " ) Evaluation Process : "
						+ (endTime - startTime)));
				logger.info((Object) (expresssion + " ==> " + result));
				System.out.println(expresssion + " ==> " + result);
			} catch (Exception e) {
				System.out.println(e.getMessage());
				logger.error((Object) ("Exception on Expression : " + expresssion + "  " + e));
			}
		}
	}
}