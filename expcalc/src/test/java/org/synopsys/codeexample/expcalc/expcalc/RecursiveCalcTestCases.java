package org.synopsys.codeexample.expcalc.expcalc;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.synopsys.codeexample.expcalc.InvalidExpressionException;
import org.synopsys.codeexample.expcalc.RecurssiveImplExpEvalCalculator;

public class RecursiveCalcTestCases {

	private static final double DELTA = 1e-15;
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testDelta() throws Exception {
		RecurssiveImplExpEvalCalculator exprCalc = new RecurssiveImplExpEvalCalculator();

		double value = exprCalc.evaluateExpression("add(1,2)");
		assertEquals(3.0, value, DELTA);
		value = exprCalc.evaluateExpression("add(1, mult(2, 3))");
		assertEquals(7.0, value, DELTA);
		value = exprCalc.evaluateExpression("mult(add(2, 2), div(9, 3))");
		assertEquals(12.0, value, DELTA);
		value = exprCalc.evaluateExpression("let(a, 5, add(a, a))");
		assertEquals(10.0, value, DELTA);
		value = exprCalc.evaluateExpression("let(a, 5, let(b, mult(a, 10), add(b, a)))");
		assertEquals(55.0, value, DELTA);
		value = exprCalc.evaluateExpression("let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))");
		assertEquals(40.0, value, DELTA);
		value = exprCalc.evaluateExpression("add(1,-9)");
		assertEquals(-8.0, value, DELTA);
		value = exprCalc.evaluateExpression("add(-1,3.0)");
		assertEquals(2.0, value, DELTA);

	}

	@Test
	public void throwsExceptionWhennotclosingBracesAreGiven() throws Exception {
		RecurssiveImplExpEvalCalculator exprCalc = new RecurssiveImplExpEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Please enter a valid expression, check for braces whether it is closed properly or not.");
		// act
		exprCalc.evaluateExpression("add(-1,)");
	}

	@Test
	public void throwsExceptionWhennotpassingAnyValue() throws Exception {
		RecurssiveImplExpEvalCalculator exprCalc = new RecurssiveImplExpEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Empty expression provided, please provide valid expression.");
		// act
		exprCalc.evaluateExpression("");
	}

	@Test
	public void throwsExceptionWhennotpassingNotCorrectOperator() throws Exception {
		RecurssiveImplExpEvalCalculator exprCalc = new RecurssiveImplExpEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("You had given a invalid operator, Only add,mult,div,let are allowed!");
		// act
		exprCalc.evaluateExpression("test(1,2)");
	}

	@Test
	public void throwsExceptionWhennotpassingCamaValue() throws Exception {
		RecurssiveImplExpEvalCalculator exprCalc = new RecurssiveImplExpEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Please enter a valid expression, check for braces whether it is closed properly or not.");
		// act
		exprCalc.evaluateExpression("add(1,");
	}

	@Test
	public void throwsExceptionWhennotpassingnullValue() throws Exception {
		RecurssiveImplExpEvalCalculator exprCalc = new RecurssiveImplExpEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Empty expression provided, please provide valid expression.");
		// act
		exprCalc.evaluateExpression(null);
	}

	@Test
	public void throwsExceptionWhennotpassingDoubleMaxalue() throws Exception {
		RecurssiveImplExpEvalCalculator exprCalc = new RecurssiveImplExpEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage(
				"Please give valid expression, you might have had given maximum or minimum double values allowed or had passed a wrong expression.");
		// act
		exprCalc.evaluateExpression("add(" + Double.MAX_VALUE + 1 + "," + "2)");
	}

	@Test
	public void throwsExceptionWhennotpassingcharinAddMethod() throws Exception {
		RecurssiveImplExpEvalCalculator exprCalc = new RecurssiveImplExpEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage(
				"Please give valid expression, you might have had given maximum or minimum double values allowed or had passed a wrong expression.");
		exprCalc.evaluateExpression("add(a,1)");
	}

	@Test
	public void throwsExceptionWhennotpassingdiffrentOperator() throws Exception {
		RecurssiveImplExpEvalCalculator exprCalc = new RecurssiveImplExpEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Please enter a valid expression, check for braces whether it is closed properly or not.");
		// act
		exprCalc.evaluateExpression("let(a, test(b, 10, add(b, b)), let(b, 20, add(a, b))");
	}

	@Test
	public void throwsExceptionWhennotpassingdchar() throws Exception {
		RecurssiveImplExpEvalCalculator exprCalc = new RecurssiveImplExpEvalCalculator();
		thrown.expect(InvalidExpressionException.class);
		thrown.expectMessage("Please enter a valid expression, check for braces whether it is closed properly or not.");
		// act
		exprCalc.evaluateExpression("let(a, add(b, 10, add(b, b)), let(b, 5, add(a, c))");
	}

}
