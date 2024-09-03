package geje1017.logic.postfix;

/**
 * Provides a utility to validate the syntactic correctness of an infix expression.
 * This class checks the expression against a set of predefined rules,
 * ensuring that it contains only allowed characters, balanced parentheses,
 * correctly placed operators, and adheres to special syntax rules.
 */
public class ExpressionValidator {

    /**
     * Validates an entire infix expression using multiple helper methods to check
     * different aspects of the expression's syntax.
     *
     * @param expression The expression to validate.
     * @throws InvalidExpressionException If any part of the expression violates the syntax rules.
     */
    public static void validateInfix(String expression) throws InvalidExpressionException {
        expression = ExpressionConverter.removeEmptyParentheses(expression);
        validateCharacters(expression);
        validateParenthesesBalance(expression);
        validateUnaryOperatorPlacement(expression);
        validateBinaryOperatorPlacement(expression);
    }

    /**
     * Validates that the expression contains only allowed characters.
     *
     * @param expression The expression to check.
     * @throws InvalidExpressionException If the expression contains illegal characters.
     */
    private static void validateCharacters(String expression) throws InvalidExpressionException {
        if (expression == null || !expression.matches("[a-zA-Z0-9|+*()\\\\]{0,30}")) {
            throw new InvalidExpressionException("Invalid characters in expression.");
        }
    }

    /**
     * Checks that all parentheses in the expression are balanced.
     *
     * @param expression The expression to check.
     * @throws InvalidExpressionException If the parentheses are unbalanced.
     */
    private static void validateParenthesesBalance(String expression) throws InvalidExpressionException {
        int balance = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
                if (balance < 0) {
                    throw new InvalidExpressionException("More closing than opening parentheses.");
                }
            }
        }
        if (balance != 0) {
            throw new InvalidExpressionException("Unbalanced parentheses.");
        }
    }

    /**
     * Ensures that operators are placed correctly within the expression,
     * such as not starting with an operator unless permitted by syntax rules.
     *
     * @param expression The expression to check.
     * @throws InvalidExpressionException If operators are misplaced.
     */
    private static void validateUnaryOperatorPlacement(String expression) throws InvalidExpressionException {
        if (expression.matches(".*([(][+*]).*")
                || expression.matches(".*([|][+*]).*")
                || expression.startsWith("+")
                || expression.startsWith("*")) {
            throw new InvalidExpressionException("Invalid unary operator placement.");
        }
    }

    /**
     * Checks for violations of special syntax rules, such as consecutive '|' characters,
     * or '|' characters at the beginning or end of the expression.
     *
     * @param expression The expression to check.
     * @throws InvalidExpressionException If special syntax rules are violated.
     */
    private static void validateBinaryOperatorPlacement(String expression) throws InvalidExpressionException {
        if (expression.matches(".*([|]{2,}).*") 
                || expression.startsWith("|") 
                || expression.endsWith("|")) {
            throw new InvalidExpressionException("Invalid unary operator placement.");
        }
    }

    /**
     * Custom exception class for invalid expressions. This provides more specific error
     * information than a generic exception class.
     */
    public static class InvalidExpressionException extends Exception {
        public InvalidExpressionException(String message) {
            super(message);
        }
    }
}
