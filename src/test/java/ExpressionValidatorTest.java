import geje1017.logic.postfix.ExpressionValidator;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;

public class ExpressionValidatorTest {

    @Test
    @DisplayName("Valid expression should not throw an exception")
    public void testValidExpression() {
        String expression = "a+b*(c|d)";
        Assertions.assertDoesNotThrow(() -> ExpressionValidator.validateInfix(expression));
    }

    @Test
    @DisplayName("Misplaced operators should throw InvalidExpressionException")
    public void testValidExpression2() {
        String expression = "a+*b";
        Assertions.assertDoesNotThrow(() -> ExpressionValidator.validateInfix(expression));
    }

    @Test
    @DisplayName("Misplaced operators should throw InvalidExpressionException")
    public void testValidParentheses() {
        String expression = "()";
        Assertions.assertDoesNotThrow(() -> ExpressionValidator.validateInfix(expression));
    }
    @Test
    @DisplayName("Misplaced operators should throw InvalidExpressionException")
    public void testValidParentheses2() {
        String expression = "(())";
        Assertions.assertDoesNotThrow(() -> ExpressionValidator.validateInfix(expression));
    }
    @Test
    @DisplayName("Misplaced operators should throw InvalidExpressionException")
    public void testValidParentheses3() {
        String expression = "a()b";
        Assertions.assertDoesNotThrow(() -> ExpressionValidator.validateInfix(expression));
    }

    @Test
    @DisplayName("Null expression should throw InvalidExpressionException")
    public void testInvalidParentheses() {
        Assertions.assertThrows(ExpressionValidator.InvalidExpressionException.class,
                () -> ExpressionValidator.validateInfix("()+"));
    }

    @Test
    @DisplayName("Empty expression should throw InvalidExpressionException")
    public void testEmptyExpression() {
        Assertions.assertDoesNotThrow(() -> ExpressionValidator.validateInfix(""));
    }

    @Test
    @DisplayName("Unbalanced parentheses should throw InvalidExpressionException")
    public void testUnbalancedParentheses() {
        Assertions.assertThrows(ExpressionValidator.InvalidExpressionException.class,
                () -> ExpressionValidator.validateInfix("a+(b*c"));
    }

    @Test
    @DisplayName("Expression with invalid characters should throw InvalidExpressionException")
    public void testInvalidCharacters() {
        Assertions.assertThrows(ExpressionValidator.InvalidExpressionException.class,
                () -> ExpressionValidator.validateInfix("a+b#c"));
    }

    @Test
    @DisplayName("Consecutive operators should throw InvalidExpressionException")
    public void testConsecutiveOperators() {
        Assertions.assertThrows(ExpressionValidator.InvalidExpressionException.class,
                () -> ExpressionValidator.validateInfix("a||b"));
    }

    @Test
    @DisplayName("Leading operator should throw InvalidExpressionException")
    public void testLeadingOperator() {
        Assertions.assertThrows(ExpressionValidator.InvalidExpressionException.class,
                () -> ExpressionValidator.validateInfix("|a+b"));
    }

    @Test
    @DisplayName("Trailing operator should throw InvalidExpressionException")
    public void testTrailingOperator() {
        Assertions.assertThrows(ExpressionValidator.InvalidExpressionException.class,
                () -> ExpressionValidator.validateInfix("a+b|"));
    }
}
