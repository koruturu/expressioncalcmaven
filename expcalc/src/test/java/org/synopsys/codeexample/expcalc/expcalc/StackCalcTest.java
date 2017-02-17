package org.synopsys.codeexample.expcalc.expcalc;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.synopsys.codeexample.expcalc.InvalidExpressionException;
import org.synopsys.codeexample.expcalc.StackImplExpressionEvalCalculator;

public class StackCalcTest {
	

	private static final double DELTA = 1e-15;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testDelta() throws Exception {
		StackImplExpressionEvalCalculator exprCalc = new StackImplExpressionEvalCalculator();

		double value = exprCalc.evaluateExp("add(1,2)");
		assertEquals(3.0, value, DELTA);
		value = exprCalc.evaluateExp("add(1, mult(2, 3))");
		assertEquals(7.0, value, DELTA);
		value = exprCalc.evaluateExp("mult(add(2, 2), div(9, 3))");
		assertEquals(12.0, value, DELTA);
		value = exprCalc.evaluateExp("let(a, 5, add(a, a))");
		assertEquals(10.0, value, DELTA);
		value = exprCalc.evaluateExp("let(a, 5, let(b, mult(a, 10), add(b, a)))");
		assertEquals(55.0, value, DELTA);
		value = exprCalc.evaluateExp("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))");
		assertEquals(40.0, value, DELTA);
		value = exprCalc.evaluateExp("add(1,-9)");
		assertEquals(-8.0, value, DELTA);
		value = exprCalc.evaluateExp("add(-1,3.0)");
		assertEquals(2.0, value, DELTA);
		value = exprCalc.evaluateExp("div(2,0)");
		assertEquals(Double.POSITIVE_INFINITY, value, DELTA);
		value = exprCalc.evaluateExp("div(-2,0)");
		assertEquals(Double.NEGATIVE_INFINITY, value, DELTA);
		value = exprCalc.evaluateExp("div(-2.6,7.0)");
		assertEquals(-0.37142857142857144, value, DELTA);
		
		
	}

	@Test
	public void throwsExceptionWhennotclosingBracesAreGiven() throws Exception {
		StackImplExpressionEvalCalculator exprCalc = new StackImplExpressionEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Please enter a valid expression, check for braces whether it is closed properly or not.");
		// act
		exprCalc.evaluateExp("add(-1,)");
	}

	@Test
	public void throwsExceptionWhennotpassingAnyValue() throws Exception {
		StackImplExpressionEvalCalculator exprCalc = new StackImplExpressionEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("You had given an empty expression, please provide a valid expression.");
		// act
		exprCalc.evaluateExp("");
	}

	@Test
	public void throwsExceptionWhennotpassingNotCorrectOperator() throws Exception {
		StackImplExpressionEvalCalculator exprCalc = new StackImplExpressionEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("You had given an invalid operator, Only add,mult,div,let are allowed!");
		// act
		exprCalc.evaluateExp("test(1,2)");
	}

	@Test
	public void throwsExceptionWhennotpassingCamaValue() throws Exception {
		StackImplExpressionEvalCalculator exprCalc = new StackImplExpressionEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Please enter a valid expression, check for braces whether it is closed properly or not.");
		// act
		exprCalc.evaluateExp("add(1,");
	}

	@Test
	public void throwsExceptionWhennotpassingnullValue() throws Exception {
		StackImplExpressionEvalCalculator exprCalc = new StackImplExpressionEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("You had given an empty expression, please provide a valid expression.");
		// act
		exprCalc.evaluateExp(null);
	}

	@Test
	public void throwsExceptionWhennotpassingDoubleMaxalue() throws Exception {
		StackImplExpressionEvalCalculator exprCalc = new StackImplExpressionEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage(
				"Please give a valid expression, you might have given maximum or minimum double values allowed or had passed a wrong expression.");
		// act
		exprCalc.evaluateExp("add(" + Double.MAX_VALUE + 1 + "," + "2)");
	}

	@Test
	public void throwsExceptionWhennotpassingcharinAddMethod() throws Exception {
		StackImplExpressionEvalCalculator exprCalc = new StackImplExpressionEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage(
				"Please give a valid expression, you might have given maximum or minimum double values allowed or had passed a wrong expression.");
		// act
		exprCalc.evaluateExp("add(a,1)");
	}

	@Test
	public void throwsExceptionWhennotpassingdiffrentOperator() throws Exception {
		StackImplExpressionEvalCalculator exprCalc = new StackImplExpressionEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("You had given an invalid operator, Only add,mult,div,let are allowed!");
		// act
		exprCalc.evaluateExp("let(a, test(b, 10, add(b, b)), let(b, 20, add(a, b))");
	}

	@Test
	public void throwsExceptionWhennotpassingdchar() throws Exception {
		StackImplExpressionEvalCalculator exprCalc = new StackImplExpressionEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Please enter a valid expression, check for braces whether it is closed properly or not.");
		// act
		exprCalc.evaluateExp("let(a, add(b, 10, add(b, b)), let(b, 5, add(a, c))");
	}

	@Test
	public void throwsExceptionWhennotpassingdMoreThanOnechar() throws Exception {
		StackImplExpressionEvalCalculator exprCalc = new StackImplExpressionEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage(
				"Please give a valid expression, you might have given maximum or minimum double values allowed or had passed a wrong expression.");
		exprCalc.evaluateExp("let(bc,10,add(bc,bc))");
	}
}
