package org.synopsys.codeexample.expcalc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
//This is stack implementation, stack implementation is using more pattern match comparison which will degrade the performance.
//I have also implemented the recursive approach which I prefer, I have assumed let assignment is in only one character i.e. let(a, 5, add(a, a))
//the program will fail if let(ab, 5, add(ab, ab)).
public class StackImplExpressionEvalCalculator {
	private static final Logger logger = Logger.getLogger(StackImplExpressionEvalCalculator.class);
	//checking whether let assignment operator is number or a character, taking the context accordingly.
	private Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
	private Hashtable<Object, Double> context_dict = new Hashtable<Object, Double>();

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
						"Please give a valid expression, you might have given maximum or minimum double values allowed or had passed a wrong expression.",
						e);
			}
		}
		ArrayList<String> exp_list = (ArrayList) expList;
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
				"You had given an invalid operator, Only add,mult,div,let are allowed!" + operator);
	}

	public double evaluateExp(String exp_str) throws Exception {
		if (StringUtils.isEmpty(exp_str)) {
			throw new InvalidExpressionException("You had given an empty expression, please provide a valid expression.");
		}
		Stack<String> stack = new Stack<String>();
		exp_str = exp_str.trim();
		exp_str = exp_str.replaceAll(" ", "");
		double result = 0.0;
		String tmp_str = "";
		try {
			for (int index = 0; index < exp_str.length() || !stack.isEmpty(); ++index) {
				char currentChar = exp_str.charAt(index);
				if (currentChar == '(') {
					stack.push("function");
					if (!tmp_str.isEmpty()) {
						stack.push(tmp_str);
					}
					tmp_str = "";
					continue;
				}
				if (currentChar == ')') {
					ArrayList<String> a = new ArrayList<String>();
					if (!tmp_str.isEmpty() && !this.pattern.matcher(tmp_str.toString()).matches()) {
						if (this.context_dict != null && this.context_dict.get(tmp_str) != null) {
							stack.push(this.context_dict.get(tmp_str).toString());
						}
					} else if (!tmp_str.isEmpty()) {
						stack.push(tmp_str);
					}
					while (!((String) stack.peek()).equalsIgnoreCase("function")) {
						a.add(stack.pop());
					}
					stack.pop();
					Collections.reverse(a);
					if (index + 1 >= exp_str.length()) {
						result = this.evaluate(a);
						if (stack.size() > 2) {
							stack.push(String.valueOf(result));
							while (!((String) stack.peek()).equalsIgnoreCase("function")) {
								a.add(stack.pop());
							}
							stack.pop();
							result = this.evaluate(a);
						}
					} else {
						double element = this.evaluate(a);
						if (!this.pattern.matcher((CharSequence) stack.peek()).matches()
								&& ((String) stack.peek()).length() == 1) {
							this.context_dict.put(stack.peek(), element);
						}
						stack.push(String.valueOf(element));
					}
					tmp_str = "";
					continue;
				}
				if (currentChar == ',') {
					if (tmp_str.length() > 0) {
						if (this.pattern.matcher(tmp_str.toString()).matches()
								&& !this.pattern.matcher(((String) stack.peek()).toString()).matches()
								&& ((String) stack.peek()).length() == 1) {
							this.context_dict.put(stack.peek(), Double.valueOf(tmp_str.toLowerCase()));
							stack.push(tmp_str);
						} else if (!this.pattern.matcher(tmp_str.toString()).matches() && tmp_str.length() == 1
								&& !((String) stack.peek()).equalsIgnoreCase("let")) {
							if (this.context_dict != null && this.context_dict.get(tmp_str) != null) {
								stack.push(this.context_dict.get(tmp_str).toString().toLowerCase());
							} else {
								stack.push(tmp_str);
							}
						} else {
							stack.push(tmp_str);
						}
					}
					tmp_str = "";
					continue;
				}
				tmp_str = tmp_str + exp_str.charAt(index);
			}
		} catch (InvalidExpressionException e) {
			throw e;
		} catch (Exception e) {
			throw new InvalidExpressionException(
					"Please enter a valid expression, check for braces whether it is closed properly or not.", e);
		}
		return result;
	}

	public static void main(String[] args) {
		String expresssion;
		if (args.length <= 0) {
			System.out.println(
					"Please enter the calculator expression here or you can add as arguments while running the program.");
			Scanner sc = new Scanner(System.in);
			expresssion = sc.nextLine();
			sc.close();
		} else {
			expresssion = args[0];
		}
		try {
			if (StringUtils.isEmpty((CharSequence) expresssion)) {
				throw new InvalidExpressionException("Empty expression provided, please provide a valid expression.");
			}
			StackImplExpressionEvalCalculator impl = new StackImplExpressionEvalCalculator();
			long startTime = System.nanoTime();
			double result = impl.evaluateExp(expresssion);
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